package com.meituan;

import com.facebook.react.ReactActivity;

/**
 * @author Edwin
 * React Native应用容器，依赖ReactMainApplication
 */

public class ReactMainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "MeiTuan";
    }
}
