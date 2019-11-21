package com.common.reactComponents.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Edwin,CHEN on 2019/10/31.
 */

public class ReactTextView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener {

    private boolean clicked = false;

    private StateChangedListener stateChangedListener;

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
        stateChangedListener.stateChanged(clicked ? 0 : 1);
    }

    @Override
    public void onClick(View v) {
        // 点击事件时无效的，必须传递给RN才能生效
        clicked = !clicked;
        stateChangedListener.stateChanged(clicked ? 0 : 1);
    }

    public void setStateChangedListener(StateChangedListener stateChangedListener) {
        if (this.stateChangedListener == null) {
            this.stateChangedListener = stateChangedListener;
        }
    }

    public interface StateChangedListener{

        /**
         * 状态变化：被选中、取消选中
         * @param newState
         */
        void stateChanged(int newState);
    }
}
