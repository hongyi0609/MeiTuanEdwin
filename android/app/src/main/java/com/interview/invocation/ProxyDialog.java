package com.interview.invocation;

import android.util.Log;

/**
 * Created by Edwin on 2020/9/10.
 *
 * @author Edwin
 */

public class ProxyDialog implements DialogApi {

    private static final String TAG = ProxyDialog.class.getSimpleName();
    @Override
    public void handleDialog() {
        Log.d(TAG, "handleDialog ");
    }

    @Override
    public void handleDialogTitle(String title) {
        Log.d(TAG, "handleDialogTitle " + title);
    }
}
