package com.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.algorithm.AlgorithmFragment
import com.base.BaseFragment
import com.common.Constants
import com.ele.EleFragment
import com.flutter.FlutterFragment2
import com.flutter.ProxyFlutterFragment
import com.interview.InterviewFragment
import com.meituan.ReactNativeFragment
import com.rxjava.RxJavaFragment

/**
 * Created by Edwin,CHEN on 2020/4/26.
 */
open class HomeViewPagerFragmentAdapter : FragmentPagerAdapter {

    private val list = ArrayList<Fragment>()

    private val titles = ArrayList<String>()

    private var currentSelectedPage :BaseFragment? = null


    constructor(fm: FragmentManager) :super(fm){
        if (list.size <= 0){
            list.add(ReactNativeFragment.createFragment())
            list.add(InterviewFragment.createFragment())
            list.add(EleFragment.createFragment())
            list.add(RxJavaFragment.createFragment())
            list.add(AlgorithmFragment.createFragment())

            list.add(ProxyFlutterFragment.createFragment())
//            list.add(FlutterFragment2.createFragment())
        }
//        if (titles.size <= 0) {
//            titles.add("美团0")
//            titles.add("美团1")
//            titles.add("美团2")
//            titles.add("Flutter Demo")
//        }
    }

//    override fun getPageTitle(position: Int): CharSequence? {
//        return titles[position]
//    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
        var isCurrentSelectedPageChanged = false
        if (obj != currentSelectedPage) {
            isCurrentSelectedPageChanged = true
            if (currentSelectedPage != null) {
                currentSelectedPage!!.onUnPagedSelected()
            }
        }
        super.setPrimaryItem(container, position, obj)
        if (isCurrentSelectedPageChanged) {
            currentSelectedPage = obj as BaseFragment
            currentSelectedPage
            if (currentSelectedPage != null) {
                currentSelectedPage!!.onPageSelected()
            }
        }
    }

    override fun getItem(index: Int): Fragment {
        return list[index]
    }

    override fun getCount(): Int {
        return list.size
    }

}