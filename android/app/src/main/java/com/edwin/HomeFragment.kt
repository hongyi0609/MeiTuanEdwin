package com.edwin

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.adapter.HomeViewPagerFragmentAdapter
import com.meituan.R

/**
 * Created by Edwin,CHEN on 2020/4/26.
 */

class HomeFragment : Fragment() {

    private var mContext: Context? = null

    private var homeViewPager :ViewPager? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.home_fragment, container, false)
        homeViewPager = root.findViewById(R.id.home_view_pager)
        initViewPager()
        return root
    }

    private fun initViewPager(){
        homeViewPager!!.adapter = HomeViewPagerFragmentAdapter(childFragmentManager)
        homeViewPager!!.offscreenPageLimit = Companion.PAGE_SCREEN_LIMIT
    }

    companion object {
        private val PAGE_SCREEN_LIMIT :Int = 3
        fun createHomeFragment() :Fragment{
            return HomeFragment()
        }
    }
}