package com.edwin

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
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

    private var homeTabLayout :TabLayout? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.home_fragment, container, false)
        homeTabLayout = root.findViewById(R.id.home_tab_layout)
        homeViewPager = root.findViewById(R.id.home_view_pager)
        initView()
        return root
    }

    /**
     * ViewPager要在TabLayout之前优化，防止覆盖
     */
    private fun initView(){
        initViewPager()
        initTabLayout()
    }

    private fun initTabLayout(){
        homeTabLayout!!.setupWithViewPager(homeViewPager)

        val titles = arrayOf("美团First","美团Second","RxJava","Flutter")
        for ( i in 1 .. titles.size ) {
            homeTabLayout!!.getTabAt(i-1)!!.text = titles[i-1]
            if (i == 3){
                homeTabLayout!!.getTabAt(i-1)!!.select()
            }
        }
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