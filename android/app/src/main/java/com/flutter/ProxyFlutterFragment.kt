package com.flutter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.base.BaseFragment
import com.meituan.R

/**
 * Created by Edwin,CHEN on 2020/5/19.
 * 为了使用统一的BaseFragment接口，重新定义了FlutterFragment
 * 第一种方案，该方案侵入性太强，使用aar包
 *
 */
open class ProxyFlutterFragment : BaseFragment() {

    private var root: View? = null;

    companion object {

        val TAG: String = ProxyFlutterFragment::class.java.simpleName

        fun createFragment(): ProxyFlutterFragment {
            return ProxyFlutterFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fluttter_container_fragment, container, false);
//		mContext.startActivity(new Intent(mContext, FlutterInterfaceActivity.class));
        root?.findViewById<TextView>(R.id.open_flutter_activity)?.setOnClickListener {
            val intent = Intent(context, FlutterInterfaceActivity::class.java)
            context?.startActivity(intent)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (root != null) {
            root = null
        }
    }
}