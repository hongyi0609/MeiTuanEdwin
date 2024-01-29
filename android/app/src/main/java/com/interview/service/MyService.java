package com.interview.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 处理启动命令
        Log.d(TAG, "onStartCommand() called");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "sleep called");
                    Thread.sleep(1000*10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    Log.d(TAG, "stopSelf() called");
                    stopSelf();
                }
            }
        }).start();
        return START_STICKY; // Service被杀死后，重新创建并启动
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被停止时执行的操作
        Log.d(TAG, "onDestroy() called");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;// 返回null表示不绑定任何数据到Service
    }
}
