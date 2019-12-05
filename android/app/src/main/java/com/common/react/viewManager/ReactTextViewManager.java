package com.common.react.viewManager;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.common.react.components.ReactTextView;
import com.common.react.events.TextViewStateChangedEvent;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by Edwin,CHEN on 2019/10/31.
 */

public class ReactTextViewManager extends SimpleViewManager<ReactTextView> {

	private Context mContext;
	private final static String NATIVE_CLICKED_EVENT_NAME = "topClicked";
	private final static String JS_CLICKED_EVENT_NAME = "onClicked";

	private boolean clicked = false;

	private final static String NATIVE_METHOD_HANDLE_TASK = "handleTask";
	private final static int NATIVE_METHOD_HANDLE_TASK_ID = 0;

	@Override
	public String getName() {
		return "ReactTextView";
	}

	@Override
	protected ReactTextView createViewInstance(ThemedReactContext reactContext) {
		mContext = reactContext;
		return new ReactTextView(reactContext);
	}

	@ReactProp(name = "title")
	public void setTitle(ReactTextView view, String title) {
		view.setReactTitle(title);
	}

	@Nullable
	@Override
	public Map<String, Integer> getCommandsMap() {
		// 获得从RN发送来Commands，通过Map将RN方法名映射成方法ID，供receiveCommand接收
		Map<String, Integer> commands = MapBuilder.of();
		commands.put(NATIVE_METHOD_HANDLE_TASK, NATIVE_METHOD_HANDLE_TASK_ID);
		return commands;
	}

	@Override
	public void receiveCommand(ReactTextView root, int commandId, @Nullable ReadableArray args) {
		// 根据getCommandsMap映射回的方法ID，解析从RN传递来的信息args，执行相关命令
		switch (commandId) {
			case NATIVE_METHOD_HANDLE_TASK_ID:
				if (args != null && args.size() > 0) {
					String nativeMethodHandleTask = args.getString(0);
					Toast.makeText(mContext, "从RN发送来的命令是：" + nativeMethodHandleTask, Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	}

	@Nullable
	@Override
	public Map getExportedCustomDirectEventTypeConstants() {
		return MapBuilder.of(
				TextViewStateChangedEvent.EVENT_NAME, MapBuilder.of("registrationName", "onStateChanged"),
				NATIVE_CLICKED_EVENT_NAME, MapBuilder.of("registrationName", JS_CLICKED_EVENT_NAME));
	}

	@Override
	protected void addEventEmitters(final ThemedReactContext reactContext, final ReactTextView view) {
		final ReactTextView.CommonListener commonListener = new EventEmitter(reactContext, view);
		view.setCommonListener(commonListener);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 发送点击事件给JS
				WritableMap params = Arguments.createMap();
				params.putString("msg", "点击TextView");
				reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
						v.getId(),
						NATIVE_CLICKED_EVENT_NAME,
						params
				);
				// 发送状态更改信息给JS
				clicked = !clicked;
				commonListener.stateChanged(clicked ? 0 : 1);
			}
		});
	}

	private static class EventEmitter implements ReactTextView.CommonListener {

		private ThemedReactContext reactContext;
		private EventDispatcher eventDispatcher;
		private ReactTextView reactTextView;

		public EventEmitter(ThemedReactContext reactContext, ReactTextView reactTextView) {
			this.reactContext = reactContext;
			this.eventDispatcher = reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
			this.reactTextView = reactTextView;
		}

		@Override
		public void stateChanged(int newState) {
			eventDispatcher.dispatchEvent(new TextViewStateChangedEvent(reactTextView.getId(), newState));
		}
	}
}
