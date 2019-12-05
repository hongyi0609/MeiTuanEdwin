package com.common.react;

import com.common.react.viewManager.ReactTextViewManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin,CHEN on 2019/10/28.
 */

public class HomeReactPackage implements ReactPackage {

    /**
     * 该方法通过创建原生方法供JS调用
     * @param reactContext
     * @return
     */
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {

        List<NativeModule> modules = new ArrayList<>();
        modules.add(new ToastModule(reactContext));
        modules.add(new HomeReactNativeMessageEventModule(reactContext, new HomeNativeMessageEventListener()));
        return modules;
    }

    /**
     * 该方法通过创建原生的View组件供JS调用，相对复杂
     * 注意不要返回null,js会报错
     * @param reactContext
     * @return
     */
    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        List<ViewManager> viewManagerList = new ArrayList<>();
        viewManagerList.add(new ReactTextViewManager());
        return viewManagerList;
    }
}
