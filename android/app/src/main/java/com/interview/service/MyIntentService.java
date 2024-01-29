package com.interview.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService {
    private static final String TAG = "MyIntentService";
    public MyIntentService() {
        super("MyIntentService");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "IntentService created");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "IntentService started");
        // 模拟一个耗时任务
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Task running: " + i);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "IntentService destroyed");
    }
}
