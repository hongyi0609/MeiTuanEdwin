package com.edwin

import android.content.Context
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.common.Constants
import com.common.adapter.HomeViewPagerFragmentAdapter
import com.meituan.R

/**
 * Created by Edwin,CHEN on 2020/4/26.
 */

class HomeFragment : Fragment() {

    private var mContext: Context? = null

    private var homeViewPager : ViewPager? = null

    private var homeTabLayout : TabLayout? = null

    override fun onAttach(context: Context) {
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

        val titles = arrayOf("美团First","Interview","饿了么","RxJava","Algorithm","Flutter")
        for ( i in 1 .. titles.size ) {
            homeTabLayout!!.getTabAt(i-1)!!.text = titles[i-1]
            if (i == 3){
                homeTabLayout!!.getTabAt(i-1)!!.select()
            }
        }
    }

    private fun initViewPager(){
        homeViewPager!!.adapter = HomeViewPagerFragmentAdapter(childFragmentManager)
        homeViewPager!!.offscreenPageLimit = Constants.PAGE_SCREEN_LIMIT
        homeViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {

            }

        })
    }

    companion object {
        fun createHomeFragment() : Fragment {
            return HomeFragment()
        }
    }
}