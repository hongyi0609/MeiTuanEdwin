package com.flutter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.base.BaseFragment
import com.common.Constants
import com.edwin.HomeActivity
import com.meituan.R
import com.utils.DpiUtil
import io.flutter.facade.Flutter

/**
 * Created by Edwin,CHEN on 2020/5/20.
 * 该类是第二种兼容viewPager的Flutter实现方式
 */
class FlutterFragment2 : BaseFragment() {
    companion object {
        val TAG = FlutterFragment2::class.java.simpleName
        fun createFragment(): FlutterFragment2 {
            return FlutterFragment2()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (mContext == null) {
            mContext = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: ViewGroup = inflater.inflate(R.layout.flutter_container_layout,container,false) as ViewGroup
        // 通过FlutterFragment引入Flutter
/*        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.flutter_container, Flutter.createFragment(Constants.FLUTTER_INITIAL_ROUTE))?.commit()*/
        // 1.2版本不再支持FlutterView的方式
        val flutterView = Flutter.createView(mContext as HomeActivity, lifecycle, "route1")
        val layout = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        layout.setMargins(30, 30, 30, 30)
        val margin = DpiUtil.dp2Px((mContext as HomeActivity).applicationContext,30)
        layout.setMargins(margin, margin, margin, margin)
        view.addView(flutterView)
        return view
    }

    override fun onFirstFetchData() {
        Log.d(TAG,"*****onFirstFetchData******")
    }
}