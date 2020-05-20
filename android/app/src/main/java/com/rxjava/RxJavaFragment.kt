package com.rxjava

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import com.base.BaseFragment
import com.meituan.R

/**
 * Created by Edwin,CHEN on 2020/4/28.
 */
class RxJavaFragment : BaseFragment() {

//    private var mContext: Context? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.rxjava_fragment, container, false)
        view.findViewById<TextView>(R.id.create_operator_text_view).setOnClickListener(l)
        view.findViewById<TextView>(R.id.map_operator_text_view).setOnClickListener(l)
        view.findViewById<TextView>(R.id.filter_operator_text_view).setOnClickListener(l)
        view.findViewById<TextView>(R.id.merge_operator_text_view).setOnClickListener(l)
        view.findViewById<TextView>(R.id.auxiliary_operator_text_view).setOnClickListener(l)
        view.findViewById<TextView>(R.id.catch_error_operator_text_view).setOnClickListener(l)
        view.findViewById<TextView>(R.id.boolean_operator_text_view).setOnClickListener(l)
        view.findViewById<TextView>(R.id.conditional_operator_text_view).setOnClickListener(l)
        view.findViewById<TextView>(R.id.to_operator_text_view).setOnClickListener(l)

        return view
    }

    private val l = OnClickListener { v ->
        run {
            when (v.id) {
            // 创建操作符
                R.id.create_operator_text_view -> {
                    OperatorCallManager.createOperatorCall()
                }
            // 变换操作符
                R.id.map_operator_text_view -> {
                    OperatorCallManager.mapOperatorCall()
                }
            // 筛选操作符filter
                R.id.filter_operator_text_view -> {
                    OperatorCallManager.filterOperatorCall()
                }
            // 组合操作符
                R.id.merge_operator_text_view -> {
                    OperatorCallManager.mergeOperatorCall()
                }
            // 辅助操作符
                R.id.auxiliary_operator_text_view -> {
                    OperatorCallManager.auxiliaryOperatorCall()
                }
            // 错误处理操作符
                R.id.catch_error_operator_text_view -> {
                    OperatorCallManager.catchErrorOperatorCall()
                }
            // 布尔操作符
                R.id.boolean_operator_text_view -> {
                    OperatorCallManager.booleanOperatorCall()
                }
            // 条件操作符
                R.id.conditional_operator_text_view -> {
                    OperatorCallManager.conditionalOperatorCall()
                }
            // 转换操作符
                R.id.to_operator_text_view -> {
                    OperatorCallManager.toOperatorCall()
                }
                else -> {

                }
            }
        }
    }

    override fun onFirstFetchData() {
        super.onFirstFetchData()
        Log.d(TAG,"****onFirstFetchData()***")
    }

    companion object {
        val TAG: String = RxJavaFragment::class.java.simpleName

        fun createFragment(): RxJavaFragment {
            return RxJavaFragment()
        }
    }

}
