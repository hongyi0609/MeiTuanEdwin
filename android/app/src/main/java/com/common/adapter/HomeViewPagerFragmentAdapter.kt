package com.common.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.base.BaseFragment
import com.common.Constants
import com.flutter.FlutterFragment2
import com.flutter.ProxyFlutterFragment
import com.meituan.ReactNativeFragment
import com.rxjava.RxJavaFragment

/**
 * Created by Edwin,CHEN on 2020/4/26.
 */
open class HomeViewPagerFragmentAdapter : FragmentPagerAdapter {

    private val list = ArrayList<Fragment>()

    private val titles = ArrayList<String>()

    private var currentSelectedPage :BaseFragment? = null


    constructor(fm:FragmentManager) :super(fm){
        if (list.size <= 0){
            list.add(ReactNativeFragment.createFragment())
            list.add(ReactNativeFragment.createFragment())
            list.add(RxJavaFragment.createFragment())

            list.add(ProxyFlutterFragment.createFragment(Constants.FLUTTER_INITIAL_ROUTE))
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