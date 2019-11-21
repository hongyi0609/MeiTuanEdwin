package com.common.reactComponents.viewManager;

import com.common.reactComponents.components.ReactTextView;
import com.common.reactComponents.events.TextViewStateChangedEvent;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;

import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by Edwin,CHEN on 2019/10/31.
 */

public class ReactTextViewManager extends SimpleViewManager<ReactTextView> {

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
                TextViewStateChangedEvent.EVENT_NAME, MapBuilder.of("registrationName", "onStateChanged"));
    }

    @Override
    protected void addEventEmitters(final ThemedReactContext reactContext, final ReactTextView view) {
        view.setStateChangedListener(new EventEmitter(reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher(), view));
    }

    private static class EventEmitter implements ReactTextView.StateChangedListener{

        private EventDispatcher eventDispatcher;
        private ReactTextView reactTextView;

        public EventEmitter(EventDispatcher eventDispatcher, ReactTextView reactTextView) {
            this.eventDispatcher = eventDispatcher;
            this.reactTextView = reactTextView;
        }

        @Override
        public void stateChanged(int newState) {
            eventDispatcher.dispatchEvent(new TextViewStateChangedEvent(reactTextView.getId(), newState));
        }
    }
}
