package com.flutter

import io.flutter.embedding.android.FlutterFragment

/**
 * Created by Edwin,CHEN on 2020/5/20.
 * 该类是第二种兼容viewPager的Flutter实现方式
 */
class FlutterFragment2 : FlutterFragment() {
    companion object {
        val TAG = FlutterFragment2::class.java.simpleName
        fun createFragment(): FlutterFragment2 {
            return FlutterFragment2()
        }
    }


//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view: ViewGroup = inflater.inflate(R.layout.flutter_container_layout,container,false) as ViewGroup
//        // 通过FlutterFragment引入Flutter
//        val fragmentTransaction = fragmentManager?.beginTransaction()
//        fragmentTransaction?.replace(R.id.flutter_container, Flutter.createFragment(Constants.FLUTTER_SECOND_ROUTE))?.commit()
//        // 1.2版本不再支持FlutterView的方式
//        /*val flutterView = Flutter.createView(mContext as HomeActivity, lifecycle, "route1")
//        val layout = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
////        layout.setMargins(30, 30, 30, 30)
//        val margin = DpiUtil.dp2Px((mContext as HomeActivity).applicationContext,30)
//        layout.setMargins(margin, margin, margin, margin)
//        view.addView(flutterView)*/
//        return view
//    }

}