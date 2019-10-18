package com.common;

import android.support.v7.app.AppCompatActivity;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.shell.MainReactPackage;
import com.meituan.BuildConfig;

import java.io.File;

/**
 * Created by Edwin,CHEN on 2019/10/18.
 */

public class HomeReactInstanceManager {

    private static HomeReactInstanceManager manager;
    private ReactInstanceManager reactInstanceManager;

    private HomeReactInstanceManager() {

    }

    public static HomeReactInstanceManager newInstance() {
        if (manager == null) {
            manager = new HomeReactInstanceManager();
        }
        return manager;
    }

    public ReactInstanceManager getReactInstanceManager() {
        return reactInstanceManager;
    }

    public ReactInstanceManager init(AppCompatActivity activity) {
        if (reactInstanceManager == null) {
            reactInstanceManager = ReactInstanceManager.builder()
                    .setApplication(activity.getApplication())
                    .setCurrentActivity(activity)
                    .setJSBundleFile("assets://" + "react" + File.separator + "index.android.bundle")
                    .setJSMainModulePath("index")
                    .addPackage(new MainReactPackage())
                    .setUseDeveloperSupport(BuildConfig.DEBUG)
                    .setInitialLifecycleState(LifecycleState.RESUMED).build();
        }
        return reactInstanceManager;
    }
}
