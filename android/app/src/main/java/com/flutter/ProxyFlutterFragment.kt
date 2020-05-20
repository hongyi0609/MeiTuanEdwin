package com.flutter

import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.base.BaseFragment
import io.flutter.facade.Flutter
import io.flutter.facade.FlutterFragment.ARG_ROUTE
import io.flutter.view.FlutterView

/**
 * Created by Edwin,CHEN on 2020/5/19.
 * 为了使用统一的BaseFragment接口，重新定义了FlutterFragment
 * 第一种方案，该方案侵入性太强
 */
open class ProxyFlutterFragment : BaseFragment() {
    private var mRoute: String? = "/"

    companion object {

        val TAG: String = ProxyFlutterFragment::class.java.simpleName

        fun createFragment(initialRoute: String): ProxyFlutterFragment {
            val fragment = ProxyFlutterFragment()
            val args = Bundle()
            args.putString(ARG_ROUTE, initialRoute)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mRoute = arguments!!.getString(ARG_ROUTE)
        }
    }

    override fun onInflate(context: Context?, attrs: AttributeSet, savedInstanceState: Bundle) {
        super.onInflate(context, attrs, savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FlutterView? {
        val flutterView :FlutterView =  Flutter.createView(activity!!, lifecycle, mRoute)
        flutterView.setZOrderOnTop(true)
        flutterView.holder.setFormat(PixelFormat.TRANSLUCENT)
        return flutterView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onFirstFetchData() {
        super.onFirstFetchData()
        Log.d(TAG,"****onFirstFetchData()***")
    }
}