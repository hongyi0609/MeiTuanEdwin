package com.net;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import android.util.Base64;
import android.util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 仅仅使用 AES 对称加密算法
 */
public class EncryptedOkHttpClient {
    public static OkHttpClient createEncryptedOkHttpClient() {
        // 创建一个OkHttpClient.Builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // 添加证书校验
        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add("example.com", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
                .build();
        builder.certificatePinner(certificatePinner);


        // 添加自定义拦截器，用于加密请求内容
        builder.addInterceptor(new EncryptionInterceptor());

        // 添加自定义拦截器，用服务器非对称加密的公钥加密对称加密的密钥，然后用对称加密密钥加密请求内容
        builder.addInterceptor(new MixtureEncryptionInterceptor());

        // 创建OkHttpClient实例
        return builder.build();
    }

    /**
     * 非对称+对称加密拦截器
     */
    private static class MixtureEncryptionInterceptor implements Interceptor {
        private static final String SERVER_PUBLIC_KEY = "YOUR_SERVER_PUBLIC_KEY"; // 替换为服务器的公钥
        private static final String TAG = MixtureEncryptionInterceptor.class.getSimpleName();

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder();
            // 加密请求体
            RequestBody encryptedRequestBody = encryptRequestBody(originalRequest.body());

            // 将加密后的请求体设置到新的 Request.Builder 中
            requestBuilder.method(originalRequest.method(), encryptedRequestBody);

            // 构建新的请求
            Request newRequest = requestBuilder.build();

            // 继续处理请求
            return chain.proceed(newRequest);
        }

        private RequestBody encryptRequestBody(RequestBody originalRequestBody) throws IOException {
            // 生成对称加密的密钥
            byte[] secretKeyBytes = generateSecretKey();
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "AES");
            // 使用服务器的公钥加密对称加密的密钥
            byte[] encryptedSecretKey = encryptWithPublicKey(secretKeyBytes, SERVER_PUBLIC_KEY);
            // 从原始 RequestBody 中读取字节数据
            Buffer buffer = new Buffer();
            originalRequestBody.writeTo(buffer);
            byte[] bytes = buffer.readByteArray();

            // 使用对称加密算法（AES）加密请求体
            byte[] encryptedRequestBodyBytes = encryptWithAES(bytes, secretKeySpec);

            // 创建新的 RequestBody，将加密后的密钥和请求体一并传输
            return RequestBody.create(null, concatenateArrays(encryptedSecretKey, encryptedRequestBodyBytes));
        }

        private byte[] concatenateArrays(byte[] a, byte[] b) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                outputStream.write(a);
                outputStream.write(b);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return outputStream.toByteArray();
        }

        private byte[] encryptWithAES(byte[] input, SecretKeySpec secretKeySpec) {
            try {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                return cipher.doFinal(input);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                     BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                return new byte[0];
            }

        }

        /**
         * <p>注意：PKCS1 因为安全问题废弃了，建议使用 OAEP</p>
         *
         * Cipher.getInstance("RSA/ECB/PKCS1Padding") 表示获取一个Cipher对象，该对象使用RSA算法、ECB模式和PKCS1填充方式。
         * Cipher 类是Java加密框架（Java Cryptography Extension，JCE）中的一个类，用于实现加密和解密操作。
         * "RSA" 是一种非对称加密算法，使用公钥进行加密，私钥进行解密。
         * "ECB" (Electronic Codebook) 是一种分组密码的工作模式，将明文分成固定大小的块进行加密。
         * "PKCS1Padding" 是一种填充方式，用于将明文填充到合适的长度以满足加密算法的要求。
         * @param input
         * @param publicKeyString
         * @return
         */
        private byte[] encryptWithPublicKey(byte[] input, String publicKeyString) {
            try {
                byte[] keyBytes = Base64.decode(publicKeyString, Base64.DEFAULT);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey publicKey = keyFactory.generatePublic(keySpec);

                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);

                return cipher.doFinal(input);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                     InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                return new byte[0];
            }
        }

        /**
         * try block 里使用的是加密算法和随机数生成器，生成的较为复杂的密钥
         * catch block 里使用的是示范性的非安全密钥
         * @return
         */
        private byte[] generateSecretKey() {
            // 生成对称加密的密钥
            try {
                // 创建KeyGenerator对象，指定使用AES算法
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

                // 初始化KeyGenerator对象，设置密钥长度为128位
                keyGenerator.init(128, new SecureRandom());

                // 生成密钥
                SecretKey secretKey = keyGenerator.generateKey();

                // 获取密钥的字节数组表示形式
                byte[] keyBytes = secretKey.getEncoded();

                // 打印密钥的字节数组表示形式
                for (byte b : keyBytes) {
                    Log.d(TAG,b + " ");
                }
                return keyBytes;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                // 这里简单地示范了生成密钥的过程，实际上可以使用更复杂的方法来生成密钥
                return "YourSecretKey".getBytes(StandardCharsets.UTF_8);
            }

        }
    }

    /**
     * 对称加密拦截器
     */
    private static class EncryptionInterceptor implements Interceptor {
        private static final String AES_ALGORITHM = "AES";
        private static final String SECRET_KEY = "YourSecretKey"; // 替换为实际的密钥

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder();

            // 在这里添加加密逻辑，例如将RequestBody进行加密处理
            RequestBody encryptedRequestBody = encryptRequestBody(originalRequest.body());

            // 将加密后的RequestBody设置到新的Request.Builder中
            requestBuilder.method(originalRequest.method(), encryptedRequestBody);

            // 构建新的Request
            Request newRequest = requestBuilder.build();

            // 继续处理请求
            return chain.proceed(newRequest);
        }

        private RequestBody encryptRequestBody(RequestBody originalRequestBody) throws IOException {
            // 从原始RequestBody中读取字节数据
            // Read the byte data from the original RequestBody using Okio
            Buffer buffer = new Buffer();
            originalRequestBody.writeTo(buffer);
            byte[] bytes = buffer.readByteArray();

            // 使用对称加密算法（AES）加密字节数据
            byte[] encryptedBytes = encryptWithAES(bytes, SECRET_KEY);

            // 创建新的RequestBody
            return RequestBody.create(originalRequestBody.contentType(), encryptedBytes);
        }

        /**
         * 加密算法
         *
         * @param input
         * @param key
         * @return
         */
        private byte[] encryptWithAES(byte[] input, String key) {
            try {
                SecretKey secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
                Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                return cipher.doFinal(input);
            } catch (Exception e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
    }

    /**
     * 模仿服务端解密
     */
    private static class DecryptionUtil {

        private static final String PRIVATE_KEY = "YOUR_SERVER_PRIVATE_KEY"; // 服务端私钥，用于解密对称密钥

        /**
         * 解密混合加密
         * @param encryptedKey
         * @param encryptedRequestBody
         * @return
         * @throws IOException
         */
        public static String decrypt(byte[] encryptedKey, byte[] encryptedRequestBody) throws IOException {
            // 使用服务端私钥解密对称密钥
            byte[] decryptedKey = decryptWithPrivateKey(encryptedKey, PRIVATE_KEY);

            // 使用解密后的对称密钥解密请求体
            String decryptedRequestBody = decryptWithAES(encryptedRequestBody, decryptedKey);

            return decryptedRequestBody;
        }

        private static String decryptWithAES(byte[] encryptedData, byte[] key) throws IOException {
            try {
                SecretKey secretKey = new SecretKeySpec(key, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, secretKey);

                byte[] decryptedBytes = cipher.doFinal(encryptedData);
                return new String(decryptedBytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Failed to decrypt request body", e);
            }
        }

        private static byte[] decryptWithPrivateKey(byte[] encryptedKey, String privateKeyString) throws IOException {
            try {
                byte[] keyBytes = android.util.Base64.decode(privateKeyString, android.util.Base64.DEFAULT);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);

                return cipher.doFinal(encryptedKey);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("Failed to decrypt symmetric key", e);
            }
        }


        private static final String AES_ALGORITHM = "AES";
        private static final String SECRET_KEY = "YourSecretKey"; // 与客户端相同的密钥
        /**
         * 解密对称加密数据
         * @param encryptedText
         * @return
         */
        public static String decrypt(String encryptedText) {
            try {
                byte[] encryptedData = Base64.decode(encryptedText, Base64.DEFAULT);

                SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
                Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, secretKey);

                byte[] decryptedBytes = cipher.doFinal(encryptedData);

                return new String(decryptedBytes, StandardCharsets.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 针对客户端单项验证，CertificatePinner
     * 服务端 校验证书有效性
     */
    private static class CertificateValidator {

        private static final String KEYSTORE_PATH = "/path/to/your/keystore.jks";
        private static final String KEYSTORE_PASSWORD = "your_keystore_password";

        public static void validate() throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
            // 加载服务端密钥库
            KeyStore keyStore = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream(KEYSTORE_PATH);
            keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());

            // 初始化密钥管理器
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

            // 初始化信任管理器，用于校验客户端证书
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            // 初始化 SSL 上下文
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            // 创建 SSLServerSocketFactory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

            // 创建 SSLServerSocket
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(443);

            // 设置客户端认证模式为需要
            sslServerSocket.setNeedClientAuth(true);

            // 等待客户端连接
            SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

            // 获取客户端证书链
            SSLSession session = sslSocket.getSession();
            javax.security.cert.X509Certificate[] clientCertificates = session.getPeerCertificateChain();

            // 在这里进行客户端证书的校验，例如验证证书链、证书有效性等
            validateCertificateChain(clientCertificates);

            // 关闭连接
            sslSocket.close();
            sslServerSocket.close();
        }

        public static boolean validateCertificateChain(X509Certificate[] certificateChain) {
            // 检查证书链是否为空
            if (certificateChain == null || certificateChain.length == 0) {
                return false;
            }

            // 验证证书链的完整性
            for (int i = 0; i < certificateChain.length - 1; i++) {
                if (!certificateChain[i].getSubjectDN().equals(certificateChain[i + 1].getIssuerDN())) {
                    return false; // 证书链不完整
                }
            }

            // 验证每个证书的有效期
            for (X509Certificate certificate : certificateChain) {
                try {
                    certificate.checkValidity(); // 检查证书有效期
                } catch (Exception e) {
                    return false; // 证书过期
                }
            }

            // 其他校验步骤（如验证签名、检查吊销状态等）

            return true; // 所有校验通过
        }
    }

}
