package com.base;

import android.os.Handler;
import android.view.View;

public class DebouncedOnClickListener implements View.OnClickListener {
    private static final long DEBOUNCE_DELAY = 500; // 500毫秒的防抖动延迟
    private View clickedView;

    // 在这里处理点击事件
    private final Runnable clickRunnable = () -> onClickAction(clickedView);
    private final Handler handler = new Handler();

    @Override
    public void onClick(View v) {
        // 保存被点击的View对象
        clickedView = v;
        // 移除之前可能存在的Runnable（如果有的话）
        handler.removeCallbacks(clickRunnable);
        // 创建一个新的Runnable并将其添加到Handler中，延迟执行
        handler.postDelayed(clickRunnable, DEBOUNCE_DELAY);
    }

    public void onClickAction(View view) {
        // 实际的点击处理逻辑
        // 例如：Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show();
    }
}
