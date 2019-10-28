package com.meituan;

import android.app.Application;

import com.common.reactComponents.HomeReactPackage;
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
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

public class MainApplication extends Application implements ReactApplication {

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected ReactInstanceManager createReactInstanceManager() {
//            return super.createReactInstanceManager();
            ReactInstanceManagerBuilder builder = ReactInstanceManager.builder().
                    setApplication(MainApplication.this).
                    setJSMainModulePath(this.getJSMainModuleName()).
                    setUseDeveloperSupport(this.getUseDeveloperSupport()).
                    setRedBoxHandler(this.getRedBoxHandler()).
                    setJavaScriptExecutorFactory(this.getJavaScriptExecutorFactory()).
                    setUIImplementationProvider(this.getUIImplementationProvider()).
                    setInitialLifecycleState(LifecycleState.BEFORE_CREATE);

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
            return Arrays.asList(
                    new MainReactPackage(),
                    new HomeReactPackage()
            );
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
    public void onCreate() {
        super.onCreate();
        SoLoader.init(this, /* native exopackage */ false);
    }
}
