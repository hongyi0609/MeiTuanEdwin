package com.utils;

import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Edwin,CHEN on 2020/4/24.
 */

public class AnimalUtil {


	/**
	 * 对指定view沿着x,y方向缩放
	 * @param v
	 * @param to
	 * @param <V>
	 */
	public static <V extends View> void startZoomAnim(@NonNull final V v, int to) {
		startValAnim(v.getWidth(), to, new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
            /* 主要还是通过获取布局参数设置宽高 */
				ViewGroup.LayoutParams lp = v.getLayoutParams();
				//获取改变时的值
				int size = Integer.valueOf(animation.getAnimatedValue().toString());
            /* 因为我的需求是一个正方形，如果不规则可以等比例设置，这里我就不写了 */
				lp.width = size;
				lp.height = size;
				v.setLayoutParams(lp);
			}
		}, 200);
	}

	public static void startValAnim(int from, int to, ValueAnimator.AnimatorUpdateListener listener,
									long duration) {
		ValueAnimator animator = ValueAnimator.ofInt(from, to);
		//设置动画时长
		animator.setDuration( duration );
		//设置插值器，当然你可以不用
		animator.setInterpolator(new DecelerateInterpolator());
		//回调监听
		animator.addUpdateListener( listener );
		//启动动画
		animator.start();
	}
}
