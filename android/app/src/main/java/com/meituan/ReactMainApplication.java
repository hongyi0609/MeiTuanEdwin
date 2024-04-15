package com.meituan;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.base.BaseApplication;
import com.common.Constants;
import com.common.react.HomeReactPackage;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactInstanceManagerBuilder;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class ReactMainApplication extends BaseApplication implements ReactApplication {
    // Somewhere in your app, before your FlutterFragment is needed,
    // like in the Application class ...
    // Instantiate a FlutterEngine.
    private FlutterEngine flutterEngine;
    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected ReactInstanceManager createReactInstanceManager() {
//            return super.createReactInstanceManager();
            ReactInstanceManagerBuilder builder = ReactInstanceManager.builder().setApplication(ReactMainApplication.this).setJSMainModulePath(this.getJSMainModuleName()).setUseDeveloperSupport(this.getUseDeveloperSupport()).setRedBoxHandler(this.getRedBoxHandler()).setJavaScriptExecutorFactory(this.getJavaScriptExecutorFactory()).setUIImplementationProvider(this.getUIImplementationProvider()).setInitialLifecycleState(LifecycleState.BEFORE_CREATE);

            for (ReactPackage reactPackage : this.getPackages()) {
                builder.addPackage(reactPackage);
            }

            String jsBundleFile = this.getJSBundleFile();
            if (jsBundleFile != null) {
                builder.setJSBundleFile("assets://" + jsBundleFile);
            } else {
                builder.setBundleAssetName(Assertions.assertNotNull(this.getBundleAssetName()));
            }

            return builder.build();
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.asList(new MainReactPackage(), new HomeReactPackage());
        }

        @Override
        protected String getJSMainModuleName() {
            return "index";
        }

        @Nullable
        @Override
        protected String getJSBundleFile() {
//            String relBundleBasePath = "jdreact" + File.separator + pluginName;

            String jsBundleFile = "react" + File.separator + "index.android.bundle";
            InputStream inputStream = null;
            try {
                inputStream = getAssets().open(jsBundleFile);
                if (inputStream != null && inputStream.available() > 0) {
                    return jsBundleFile;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return super.getJSBundleFile();
        }
    };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
        flutterEngine = new FlutterEngine(this);
        // Configure an initial route.
        flutterEngine.getNavigationChannel().setInitialRoute(/*"your/route/here"*/Constants.FLUTTER_INITIAL_ROUTE);
        // Start executing Dart code in the FlutterEngine.
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );
        // Cache the pre-warmed FlutterEngine to be used later by FlutterFragment.
        FlutterEngineCache
                .getInstance()
                .put("my_engine_id", flutterEngine);
    }
}
