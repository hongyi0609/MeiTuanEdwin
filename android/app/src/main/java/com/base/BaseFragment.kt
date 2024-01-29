package com.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

/**
 * Created by Edwin,CHEN on 2020/5/19.
 */
open class BaseFragment : Fragment() {
    companion object {
        val TAG = BaseFragment::class.java.simpleName
    }

    private lateinit var mContext :Context

    private lateinit var reference: WeakReference<Context>

    private var hasSelected :Boolean = false

    private var hasCalledFirstFetchData = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        reference = WeakReference<Context>(mContext)
    }

    override fun getContext(): Context? {
        return reference.get()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!hasCalledFirstFetchData) {
            callFirstFetchData()
        }
    }

    /**
     * 当前页面被选中
     */
    fun onPageSelected(){
        if (!hasSelected) {
            hasSelected = true
            callFirstFetchData()
        }
    }

    /**
     * 当前页面取消选中
     */
    fun onUnPagedSelected() {

    }

    /**
     * 第一获取数据
     */
    private fun callFirstFetchData() {
        hasCalledFirstFetchData = if (isAdded && hasSelected) {
            onFirstFetchData()
            true
        } else {
            false
        }
    }

    /**
     * 获取数据接口
     */
    open fun onFirstFetchData() {

    }

}