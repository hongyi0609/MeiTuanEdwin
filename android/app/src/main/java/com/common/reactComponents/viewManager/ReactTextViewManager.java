package com.common.reactComponents.viewManager;

import com.common.reactComponents.components.ReactTextView;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

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
}
