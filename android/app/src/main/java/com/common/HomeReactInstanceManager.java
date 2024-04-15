package com.common;

import androidx.appcompat.app.AppCompatActivity;

import com.common.react.HomeReactPackage;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.shell.MainReactPackage;
import com.meituan.BuildConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            List<ReactPackage> reactPackages = new ArrayList<>();
            reactPackages.add(new MainReactPackage());
            reactPackages.add(new HomeReactPackage());
            reactInstanceManager = ReactInstanceManager.builder()
                    .setApplication(activity.getApplication())
                    .setCurrentActivity(activity)
                    // 调试时放开，用于创建RN环境，方便debug
//                    .setBundleAssetName("assets://" + "react" + File.separator + "index.android.bundle")
                    .setJSBundleFile("assets://" + "react" + File.separator + "index.android.bundle")
                    .setJSMainModulePath("index")
//                    .addPackage(new MainReactPackage())
                    .addPackages(reactPackages)
                    .setUseDeveloperSupport(BuildConfig.DEBUG)
                    .setInitialLifecycleState(LifecycleState.RESUMED).build();
        }
        return reactInstanceManager;
    }
}
