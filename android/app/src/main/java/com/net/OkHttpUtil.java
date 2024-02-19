package com.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public final class OkHttpUtil {

    private static final String TAG = OkHttpUtil.class.getSimpleName();
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    /**
     * 1. 新建一个Okhttp客户端，okHttpClient
     * 2. 创建 request 请求对象
     * 3. 将请求对象传入 okhttp客户端，创建 Call 对象
     * 4. 通过 call.execute 执行同步请求
     */
    private void executeDemo() {
        Request request = new Request.Builder().url("www.baidu.com").build();

        String res;
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            res = response.toString();
            Log.d(TAG, "response = " + res);
        } catch (IOException e) {
            Log.d(TAG, "response = " + e.getMessage());
        }

    }

    /**
     * 1. 新建一个Okhttp客户端，okHttpClient
     * 2. 创建 request 请求对象
     * 3. 将请求对象传入 okhttp客户端，创建 Call 对象
     * 4. 通过 call.enqueue 执行同步请求
     */
    private void enqueueDemo() {
        Request request = new Request.Builder().url("www.baidu.com").build();
        okHttpClient.newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) {
                Request.Builder builder = chain.request().newBuilder();
                if (!builder.build().url().isHttps()) {
                    String url = builder.build().url().toString().replaceAll("http", "https");
                    builder.url(url);
                }
                try {
                    return chain.proceed(builder.build());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "response = " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 从回调中通过响应体获取数据
                String res = response.body().string();
                Log.d(TAG, "response = " + res);
            }
        });
    }
}
