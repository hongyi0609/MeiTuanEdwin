package com.common.reactComponents;

import android.util.Log;

import com.common.BaseEvent;
import com.facebook.react.bridge.ReadableMap;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Edwin,CHEN on 2019/10/29.
 */

public class HomeNativeMessageEventListener implements HomeReactNativeMessageEventModule.NativeMessageEventListener {
    @Override
    public void sendEventToNative(String eventType, ReadableMap readableMap) {
        /**
         * baseEvent 必须跟EventBus注册处@Subscribe修饰方法中的参数类，完全一致。派生类都也不行！！！
         */
        final BaseEvent baseEvent = new BaseEvent();
        baseEvent.setEventType(eventType);
        baseEvent.setMap(readableMap);
        Log.d("MessageEventListener", "Current Thread is " + Thread.currentThread().getName());
        EventBus.getDefault().post(baseEvent);
    }
}
