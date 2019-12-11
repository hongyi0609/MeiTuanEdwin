package com.common.react;


import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Created by Edwin,CHEN on 2019/12/11.
 */

public class HomeMessageEventModule extends ReactContextBaseJavaModule {

	private static ReactApplicationContext reactApplicationContext;

	private static HomeMessageEventModule homeMessageEventModule;

	private String eventName;

	private WritableMap params = Arguments.createMap();

	public static HomeMessageEventModule newInstance() {
		return new HomeMessageEventModule(reactApplicationContext);
	}

	public HomeMessageEventModule(ReactApplicationContext reactContext) {
		super(reactContext);
		this.reactApplicationContext = reactContext;
	}

	/**
	 * 发送本地时间给JS，相应的JavaScript代码要做好监听
	 * <p>
	 *     WritableMap params = Arguments.createMap();
	 *     params.putString("eventProperty", "someValue");
	 *     String eventName = "EventReminder";
	 *     sendEvent(reactContext, eventName, params);
	 * </p>
	 * @param eventName
	 * @param params
	 */
	public void sendEventToJs(String eventName,
						    @Nullable WritableMap params) {
		if (reactApplicationContext != null) {
			reactApplicationContext
					.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
					.emit(eventName, params);
		}
	}

	public void  sendEventToJs() {
		if (reactApplicationContext != null) {
			reactApplicationContext
					.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
					.emit(eventName, params);
		}
	}


	public HomeMessageEventModule putEventName(String eventName) {
		this.eventName = eventName;
		return this;
	}

	public HomeMessageEventModule putParam(String key, Object value) {
		if (key != null) {
			if (value instanceof String) {
				params.putString(key, (String) value);
			} else if (value instanceof Integer) {
				params.putInt(key,(Integer) value);
			} else if (value instanceof Double || value instanceof Long) {
				params.putDouble(key,(Double) value);
			} else if (value instanceof WritableArray) {
				params.putArray(key, (WritableArray) value);
			} else if (value instanceof Boolean) {
				params.putBoolean(key, (Boolean) value);
			} else if (value instanceof Map) {
				params.putMap(key, (WritableMap) value);
			} else {
				params.putNull(key);
			}
		}
		return this;
	}

	@Override
	public String getName() {
		return HomeMessageEventModule.class.getSimpleName();
	}
}
