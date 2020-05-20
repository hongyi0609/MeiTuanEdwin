package com.flutter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.common.Constants
import com.meituan.R
import io.flutter.facade.Flutter


/**
 * Created by Edwin,CHEN on 2020/4/24.
 */
class FlutterInterfaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.flutter_container_layout)

        // 通过FlutterFragment引入Flutter
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flutter_container, Flutter.createFragment(Constants.FLUTTER_INITIAL_ROUTE)).commit()

        // 1.2版本不再支持FlutterView的方式
//        val flutterView = Flutter.createView(this, lifecycle, "route1")
//        val layout = FrameLayout.LayoutParams(600, 800)
//        layout.leftMargin = 100
//        layout.topMargin = 200
//        addContentView(flutterView, layout)
    }
}