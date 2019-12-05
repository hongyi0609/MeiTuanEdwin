package com.common.react.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Edwin,CHEN on 2019/10/31.
 */

public class ReactTextView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener {


    private CommonListener commonListener;

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

    @Override
    public void onClick(View v) {
        // 点击事件在这里是无效的，无法传递给RN
    }

    public void setCommonListener(CommonListener commonListener) {
        if (this.commonListener == null) {
            this.commonListener = commonListener;
        }
    }

    public interface CommonListener {

        /**
         * 状态变化：被选中、取消选中
         * @param newState
         */
        void stateChanged(int newState);

    }
}
