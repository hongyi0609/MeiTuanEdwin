package com.common.reactComponents;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by Edwin,CHEN on 2019/10/29.
 * RN通过Module方法向原生Native发送通知
 */

public class HomeReactNativeMessageEventModule extends ReactContextBaseJavaModule {

    NativeMessageEventListener nativeMessageEventListener;

    public HomeReactNativeMessageEventModule(ReactApplicationContext reactContext, NativeMessageEventListener nativeMessageEventListener) {
        super(reactContext);
        this.nativeMessageEventListener = nativeMessageEventListener;
    }

    @Override
    public String getName() {
        return HomeReactNativeMessageEventModule.class.getSimpleName();
    }

    @ReactMethod
    public void sendEventToNative(String eventType, ReadableMap readableMap) {
        nativeMessageEventListener.sendEventToNative(eventType,readableMap);
    }

    /**
     * 本地消息事件监听接口
     */
    public interface NativeMessageEventListener {
        /**
         * RN间接通过该方法与原生端通信，注意：readableMap参数对应React Native中的Object或Android中的ReadableMap
         * <p>
         *     let data = {
         *         param1 = 1;
         *         param1 = "1";
         *     }
         *     NativeModules.JDReactNativeMessageEventModule.sendEventToNative(eventName, data);
         *
         *     Android 原生端
         *     {
         *         ReadableMap map = (ReadableMap)readableMap;
         *     }
         * </p>
         * @param eventType
         * @param readableMap
         */
        void sendEventToNative(String eventType, ReadableMap readableMap);
    }


}
