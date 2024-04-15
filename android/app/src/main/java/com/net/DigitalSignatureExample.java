package com.net;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.IOException;
import java.security.*;
import java.util.Base64;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 数字签名使用私钥进行签名是因为私钥只有签名方（通常是数据的发送方）持有，其他人无法获取私钥，因此可以保证签名的唯一性和真实性。服务端校验签名的过程是使用公钥对签名进行验证，公钥是由签名方公开的，任何人都可以获取到公钥，因此可以用于验证签名的有效性。
 *
 * 具体的签名和验证过程如下：
 *
 * 1. **签名（由发送方完成）**：发送方使用自己的私钥对数据进行签名，生成一个数字签名。签名过程使用的是一种哈希算法，它能够根据输入数据生成一个固定长度的摘要，然后使用私钥对摘要进行加密，生成数字签名。
 *
 * 2. **验证（由接收方或服务端完成）**：接收方或服务端使用发送方的公钥对数字签名进行解密，得到签名的摘要。然后，接收方对接收到的数据再次进行哈希计算，得到一个新的摘要。最后，接收方将用公钥解密后的摘要与新计算的摘要进行比较，如果两者相同，则说明签名有效，数据完整性和真实性得到保证。
 *
 * 总之，签名使用私钥是为了确保签名的唯一性和真实性，而验证签名使用公钥是为了使任何人都能验证签名的有效性，从而保证数据的完整性和真实性。
 */
public class DigitalSignatureExample {

    private static final String TAG = DigitalSignatureExample.class.getSimpleName();

    @SuppressLint("NewApi")
    public static void signatureDemo() throws Exception {
        // 生成密钥对
        KeyPair keyPair = generateKeyPair();

        // 待签名的数据
        String data = "Hello, world!";
        Log.d(TAG,"原始数据：" + data);

        // 签名
        byte[] signature = signData(data, keyPair.getPrivate());
        Log.d(TAG,"签名数据：" + Base64.getEncoder().encodeToString(signature));

        // 验证签名
        boolean verified = verifySignature(data, signature, keyPair.getPublic());
        Log.d(TAG,"签名验证结果：" + verified);
    }

    // 生成密钥对
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    // 签名数据
    public static byte[] signData(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return signature.sign();
    }

    // 验证签名
    public static boolean verifySignature(String data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        return sig.verify(signature);
    }


    /**
     * 签名拦截器
     */
    private static class SignatureInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            // 对请求数据进行签名
            String signedData = signData(request.body().toString());

            // 将签名添加到请求头中
            Request signedRequest = request.newBuilder()
                    .header("Signature", signedData)
                    .build();

            // 继续处理请求
            return chain.proceed(signedRequest);
        }

        private String signData(String data) {
            // 使用私钥对数据进行签名
            // 省略实现细节
            return "signedData";
        }
    }

}

