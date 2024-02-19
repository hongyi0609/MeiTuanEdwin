package com.login;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 当用户登录成功后，可以调用saveToken方法将Token保存到SharedPreferences中。当用户切换角色并重新进入应用时，可以调用
 * getToken方法从SharedPreferences中读取Token，并根据返回的值来恢复用户的登录状态。
 */
public class TokenManager {

    private static final String PREF_NAME = "login_status";
    private static final String KEY_TOKEN = "token";

    private Context context;

    public TokenManager(Context context) {
        this.context = context;
    }

    public void saveToken(String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, EncryptionUtils.encrypt(token));
        editor.apply();
    }

    public String getToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return EncryptionUtils.decrypt(sharedPreferences.getString(KEY_TOKEN, null));
    }

    public OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .build();
    }

    private class AuthInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            String token = getToken();
            if (token != null) {
                request = request.newBuilder()
                        // 这里使用了Bearer令牌（Bearer token）的形式来传递Token。Bearer令牌是OAuth 2.0中定义
                        // 的一种访问令牌（Access Token）类型，用于在客户端和资源服务器之间进行身份验证。
                        .header("Authorization", "Bearer " + token)
                        .build();
            }
            return chain.proceed(request);
        }
    }
}

