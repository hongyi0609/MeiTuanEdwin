package com.common.reactComponents.components;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Edwin,CHEN on 2019/10/31.
 */

public class ReactTextView extends android.support.v7.widget.AppCompatTextView {
    public ReactTextView(Context context) {
        super(context);
    }

    public ReactTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReactTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setReactTitle(String title) {
        setText(title);
    }
}
