package com.login;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 要实现单点登录（SSO）机制，您需要一个中心化的认证服务器，用户在认证服务器上登录后，会生成一个令牌（Token），然后在访问
 * 其他应用时，将这个令牌发送给其他应用进行验证。以下是一个简单的示例，结合OkHttp实现SSO机制：
 *
 * <p>假设认证服务器的地址为 https://auth.example.com，提供了以下接口：</p>
 *
 * <li>
 * 1. /login：用户登录接口，需要提供用户名和密码，成功登录后返回Token。
 * </li>
 * <li>
 * 2. /validateToken：验证Token接口，用于验证Token的有效性。
 * </li>
 */
public class SsoTokenManager {

    private static final String PREF_NAME = "login_status";
    private static final String KEY_AUTH_TOKEN = "token";
    private static final String KEY_REFRESH_TOKEN = "token";

    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY = "your_secret_key_here"; // 需要更换为自己的密钥
    private static final String AUTH_SERVER_URL = "https://auth.example.com"; // 认证服务器URL

    private WeakReference<Context> contextRef;
    private OkHttpClient httpClient;

    public SsoTokenManager(Context context) {
        this.contextRef = new WeakReference<>(context);
        this.httpClient = createHttpClient();
    }

    private OkHttpClient createHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .build();
    }

    /**
     * 保存认证令牌和刷新令牌
     * @param authToken
     * @param refreshToken
     */
    public void saveToken(String authToken, String refreshToken) {
        try {
            String encodedAuthToken = EncryptionUtils.encrypt(authToken);
            String encodedRefreshToken = EncryptionUtils.encrypt(refreshToken);

            Context context = contextRef.get();
            if (contextRef != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_AUTH_TOKEN, encodedAuthToken);
                editor.putString(KEY_REFRESH_TOKEN, encodedRefreshToken);
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        try {
            Context context = contextRef.get();

            if (context != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                String encodedToken = sharedPreferences.getString(KEY_AUTH_TOKEN, null);
                if (encodedToken != null) {
                    return EncryptionUtils.decrypt(encodedToken);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通常在发起需要验证登录状态的网络请求前调用。例如，在发起每个网络请求之前，您可以先调用 isTokenValid 方法来检查当
     * 前保存的 Token 是否有效，如果有效则继续发起网络请求，如果无效则需要重新登录获取新的 Token。
     * 举例:
     * public void makeAuthenticatedRequest() {
     * if (tokenManager.isTokenValid()) {
     * // Token 有效，可以发起网络请求
     * Request request = new Request.Builder()
     * .url("https://api.example.com/data")
     * .build();
     * tokenManager.getHttpClient().newCall(request).enqueue(new Callback() {
     *
     * @return
     * @Override public void onFailure(Call call, IOException e) {
     * e.printStackTrace();
     * }
     * @Override public void onResponse(Call call, Response response) throws IOException {
     * if (response.isSuccessful()) {
     * // 处理请求成功的情况
     * } else {
     * // 处理请求失败的情况
     * }
     * }
     * });
     * } else {
     * // Token 无效，需要重新登录
     * // 这里可以跳转到登录页面或者执行其他操作
     * }
     * }
     */
    public boolean isTokenValid() {
        String token = getToken();
        if (token == null) {
            return false;
        }
        // 向认证服务器发送请求验证Token是否有效
        Request request = new Request.Builder()
                .url(AUTH_SERVER_URL + "/validateToken")
                .header("Authorization", "Bearer " + token)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void refreshAuthToken(String refreshToken) {
        // 使用刷新令牌获取新的认证令牌
        requestToken("username","password", refreshToken);
    }

    /**
     * 在需要用户登录的地方，调用 SsoTokenManager 的 requestToken 方法来获取Token，并保存到本地
     *
     * @param username
     * @param password
     */
    public void requestToken(String username, String password, String refreshToken) {
        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        // 构建请求
        Request request = new Request.Builder()
                .url(AUTH_SERVER_URL + "/login")
                .header("Authorization", "Bearer " + refreshToken)
                .post(requestBody)
                .build();

        // 发送请求
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        String authToken = jsonObject.getString("auth_token");
                        String refreshToken = jsonObject.getString("refresh_token");
                        saveToken(authToken, refreshToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 处理登录失败的情况
                }
            }
        });
    }


    private class AuthInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            String token = getToken();
            if (token != null) {
                request = request.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();
            }
            return chain.proceed(request);
        }
    }
}
