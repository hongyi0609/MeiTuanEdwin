package com.common.reactComponents.viewManager;

import android.view.View;

import com.common.reactComponents.components.ReactTextView;
import com.common.reactComponents.events.TextViewStateChangedEvent;
import com.facebook.react.bridge.Arguments;
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

    private final static String NATIVE_CLICKED_EVENT_NAME = "topClicked";
    private final static String JS_CLICKED_EVENT_NAME = "onClicked";

    private boolean clicked = false;

    @Override
    public String getName() {
        return "ReactTextView" ;
    }

    @Override
    protected ReactTextView createViewInstance(ThemedReactContext reactContext) {
        return new ReactTextView(reactContext);
    }

    @ReactProp(name = "title")
    public void setTitle(ReactTextView view,  String title) {
        view.setReactTitle(title);
    }

    @Nullable
    @Override
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
                TextViewStateChangedEvent.EVENT_NAME, MapBuilder.of("registrationName", "onStateChanged"),
                NATIVE_CLICKED_EVENT_NAME,MapBuilder.of("registrationName",JS_CLICKED_EVENT_NAME));
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
                params.putString("msg","点击TextView");
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
