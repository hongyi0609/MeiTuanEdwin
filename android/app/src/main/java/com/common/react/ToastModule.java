package com.common.react;

import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by Edwin,CHEN on 2019/10/28.
 */

public class ToastModule extends ReactContextBaseJavaModule {

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    public ToastModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /**
     * 这个方法非常重要，其实就相当于告诉js，ToastModule的名字，因为js里边可能有很多JavaModule，js
     * 需要识别出ToastModule，才能调用方法
     */
    @Override
    public String getName() {
        return "ToastUtils";
    }

    /**
     * 用来传递一个静态变量或者数值，其实如果需要，可以通过它来传递一些变量，用事件通知js，然后更新其中的value,
     * 让js来取变化的数据
     * @return
     */
    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

      /**
      * 这个是js真正调用的方法，注意需要添加@ReactMethod的注解
      * */
    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), TextUtils.isEmpty(message) ? "消息异常" : message, duration).show();
    }


    /**
     * 这个是js真正调用的方法，注意需要添加@ReactMethod的注解
     * <p>
     *     callBack是一种特殊的回调参数，将在原生中的函数结果回传给js.
     *     A native module is supposed to invoke its callback only once. It can, however, store the callback and invoke it later.

     It is very important to highlight that the callback is not invoked immediately after the native function completes - remember that bridge communication is asynchronous, and this too is tied to the run loop.
     * </p>
     * @param message
     * @param duration
     * @param callback
     */
    @ReactMethod
    public void showWithCallback(String message, int duration, Callback callback/*, Callback callback2*/) {
        Toast.makeText(getReactApplicationContext(), TextUtils.isEmpty(message) ? "消息异常" : message, duration).show();
        String toastModuleName = ToastModule.class.getSimpleName();
        String toastModulePackageName = ToastModule.class.getPackage().getName();
        callback.invoke(toastModuleName,toastModulePackageName);
//        callback2.invoke(toastModuleName);
    }

    @ReactMethod
    public void showWithCallback2(String message, int duration, Callback callback) {
        Toast.makeText(getReactApplicationContext(), TextUtils.isEmpty(message) ? "消息异常" : message, duration).show();
        String toastModuleName = ToastModule.class.getSimpleName();
        String toastModulePackageName = ToastModule.class.getPackage().getName();
        callback.invoke(toastModuleName,toastModulePackageName);
    }

    @ReactMethod
    public void showWithPromise(String message, int duration, Promise promise) {
        Toast.makeText(getReactApplicationContext(), TextUtils.isEmpty(message) ? "消息异常" : message, duration).show();

        WritableMap map = Arguments.createMap();
        if (TextUtils.isEmpty(message)) {
            promise.reject(new Exception("No message!"));
        } else {
            map.putString("message", message);
            promise.resolve(map);
        }
    }
}
