package com.common.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.common.Constants
import com.meituan.ReactNativeFragment
import com.rxjava.RxJavaFragment
import io.flutter.facade.Flutter

/**
 * Created by Edwin,CHEN on 2020/4/26.
 */
open class HomeViewPagerFragmentAdapter : FragmentPagerAdapter {

    private val list = ArrayList<Fragment>()

    private val titles = ArrayList<String>()


    constructor(fm:FragmentManager) :super(fm){
        if (list.size <= 0){
            list.add(ReactNativeFragment.createFragment())
            list.add(ReactNativeFragment.createFragment())
            list.add(RxJavaFragment.createFragment())

            list.add(Flutter.createFragment(Constants.FLUTTER_INITIAL_ROUTE))
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

    override fun getItem(index: Int): Fragment {
        return list[index]
    }

    override fun getCount(): Int {
        return list.size
    }

}