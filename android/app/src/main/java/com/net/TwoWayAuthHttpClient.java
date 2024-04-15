package com.net;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 双向认证（双向证书验证）涉及到服务器和客户端双方都验证对方的证书。在 OkHttp 中，双向认证的实现通常需要使用 SSLSocketFactory
 * 和 TrustManager。下面是一个简单的示例，展示如何在 OkHttp 中进行双向认证：
 */
public class TwoWayAuthHttpClient {

    private static final String TAG = TwoWayAuthHttpClient.class.getSimpleName();

    public static void main(String[] args) {
        try {
            // 创建带有双向认证的 OkHttpClient
            OkHttpClient okHttpClient = createTwoWayAuthClient();

            // 发送网络请求
            Request request = new Request.Builder()
                    .url("https://example.com/api/data")
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            // 处理响应
            if (response.isSuccessful()) {
                Log.d(TAG, "Request successful");
            } else {
                Log.d(TAG, "Request failed with code: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * createTwoWayAuthClient 方法用于创建带有双向认证功能的 OkHttpClient。
     * 请注意，其中使用了服务器证书（/server_certificate.crt）和客户端证书及私钥（/client_certificate.p12）。
     * 确保你在实际应用中使用真实的证书和密钥，并按照实际需求进行配置。
     * @return
     * @throws IOException
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    private static OkHttpClient createTwoWayAuthClient() throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        // 服务器证书
        InputStream serverCertStream = TwoWayAuthHttpClient.class.getResourceAsStream("/server_certificate.crt");
        X509Certificate serverCertificate = readCertificate(serverCertStream);
        if (serverCertStream != null) {
            serverCertStream.close();
        }

        // 客户端证书和私钥
        InputStream clientCertStream = TwoWayAuthHttpClient.class.getResourceAsStream("/client_centificate.p12");
        KeyStore clientKeyStore = KeyStore.getInstance("PKCS12");
        clientKeyStore.load(clientCertStream, "client_password".toCharArray());
        if (clientCertStream != null) {
            clientCertStream.close();
        }

        // 创建 KeyManagerFactory 和 TrustManagerFactory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, "client_password".toCharArray());

        // 创建信任管理器，信任服务器证书
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        trustStore.setCertificateEntry("server", serverCertificate);
        trustManagerFactory.init(trustStore);

        // 初始化 SSL 上下文
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

        // 创建 OkHttpClient
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagerFactory.getTrustManagers()[0])
                .build();
    }

    private static X509Certificate readCertificate(InputStream inputStream) throws CertificateException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) certificateFactory.generateCertificate(inputStream);
    }
}
