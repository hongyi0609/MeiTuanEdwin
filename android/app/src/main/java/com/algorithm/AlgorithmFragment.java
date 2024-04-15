package com.algorithm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.BaseFragment;
import com.base.DebouncedOnClickListener;
import com.meituan.R;

/**
 * Created by Edwin,CHEN on 2020/8/11.
 */

public class AlgorithmFragment extends BaseFragment {

	private static final String TAG = AlgorithmFragment.class.getSimpleName();
	private TextView reverseListTv;
	private TextView reverseListDisplayTv;

	public static BaseFragment createFragment(){
		return new AlgorithmFragment();
	}

	@Override
	public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("ClickableViewAccessibility")
	@androidx.annotation.Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.algorithm_fragment, container, false);
		reverseListTv = view.findViewById(R.id.linked_list_text_view);
		reverseListTv.setOnClickListener(listener1);
		reverseListTv.setOnTouchListener(new View.OnTouchListener() {
			private static final int MAX_CLICK_DURATION = 200;
			private long startClickTime;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						startClickTime = SystemClock.elapsedRealtime();
						break;
					}

					case MotionEvent.ACTION_UP: {
						long clickDuration = SystemClock.elapsedRealtime() - startClickTime;
						if (clickDuration <= MAX_CLICK_DURATION) { // 防止抖动
							// Perform click action
							v.performClick();
						}
						break;
					}
				}

				return true;
			}
		});
		reverseListDisplayTv = view.findViewById(R.id.reverse_list_display_text_view);
		view.findViewById(R.id.array_demo_text_view).setOnClickListener(listener1);
		view.findViewById(R.id.binary_tree_text_view).setOnClickListener(listener1);
		view.findViewById(R.id.double_pointers_text_view).setOnClickListener(listener1);
		view.findViewById(R.id.dynamic_programing_text_view).setOnClickListener(listener1);
		view.findViewById(R.id.array_text_view).setOnClickListener(listener1);
		view.findViewById(R.id.sliding_window_text_view).setOnClickListener(listener1);
		view.findViewById(R.id.huawei_text_view).setOnClickListener(listener1);
		view.findViewById(R.id.classical_algorithm_text_view).setOnClickListener(listener1);
		return view;
	}

	private DebouncedOnClickListener listener1 = new DebouncedOnClickListener(){
		@Override
		public void onClickAction(View view) {
			switch (view.getId()){
				case R.id.linked_list_text_view:
					NodeDemo.impl();
					break;
				case R.id.array_demo_text_view:
					ArrayDemo.impl();
					break;
				case R.id.binary_tree_text_view:
					BinaryTree.binaryTreeDemo();
					break;
				case R.id.double_pointers_text_view:
					DoublePointers.demo();
					break;
				case R.id.dynamic_programing_text_view:
					DynamicProgrammingDemo.impl();
					break;
				case R.id.sliding_window_text_view:
					SlidingWindowDemo.impl();
					break;
				case R.id.huawei_text_view:
					HWInterview.getInstance().impl();
					break;
				default:
					ClassicalAlgorithm.getINSTANCE().impl();
					break;
			}
		}
	};
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

}
