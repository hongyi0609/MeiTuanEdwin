package com.common.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.common.Constants
import com.meituan.ReactNativeFragment
import io.flutter.facade.Flutter

/**
 * Created by Edwin,CHEN on 2020/4/26.
 */
open class HomeViewPagerFragmentAdapter : FragmentPagerAdapter {

    private val list = ArrayList<Fragment>()

    constructor(fm:FragmentManager) :super(fm){
        if (list.size <= 0){
            list.add(ReactNativeFragment.createHomeFragment())
            list.add(ReactNativeFragment.createHomeFragment())
            list.add(ReactNativeFragment.createHomeFragment())

            list.add(Flutter.createFragment(Constants.FLUTTER_INITIAL_ROUTE))
        }
    }

    override fun getItem(index: Int): Fragment {
        return list[index]
    }

    override fun getCount(): Int {
        return list.size
    }

}