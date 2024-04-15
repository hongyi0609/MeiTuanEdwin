package com.flutter

import android.content.Intent
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.meituan.R
import io.flutter.embedding.android.FlutterFragment


/**
 * Created by Edwin,CHEN on 2020/4/24.
 */
class FlutterInterfaceActivity : FragmentActivity() {
    companion object {
        // Define a tag String to represent the FlutterFragment within this
        // Activity's FragmentManager. This value can be whatever you'd like.
        private const val TAG_FLUTTER_FRAGMENT = "flutter_fragment"
    }

    // Declare a local variable to reference the FlutterFragment so that you
    // can forward calls to it later.
    private var flutterFragment: FlutterFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.flutter_container_layout_activity)

        // 通过FlutterFragment引入Flutter
        // Get a reference to the Activity's FragmentManager to add a new
        // FlutterFragment, or find an existing one.
        val fragmentManager: FragmentManager = supportFragmentManager

        // Attempt to find an existing FlutterFragment, in case this is not the
        // first time that onCreate() was run.
        flutterFragment =
            fragmentManager.findFragmentByTag(TAG_FLUTTER_FRAGMENT) as FlutterFragment?

        // Create and attach a FlutterFragment if one does not exist.
        if (flutterFragment == null) {
//            val newFlutterFragment = FlutterFragment.createDefault()
            val newFlutterFragment =
                FlutterFragment.withCachedEngine("my_engine_id").build<FlutterFragment>()
            flutterFragment = newFlutterFragment
            fragmentManager.beginTransaction().add(
                R.id.flutter_container,
                newFlutterFragment,
                TAG_FLUTTER_FRAGMENT
            ).commit()
        }
        // 1.2版本不再支持FlutterView的方式
//        val flutterView = Flutter.createView(this, lifecycle, "route1")
//        val layout = FrameLayout.LayoutParams(600, 800)
//        layout.leftMargin = 100
//        layout.topMargin = 200
//        addContentView(flutterView, layout)
    }

    override fun onPostResume() {
        super.onPostResume()
        flutterFragment!!.onPostResume()
    }

    override fun onNewIntent(@NonNull intent: Intent) {
        super.onNewIntent(intent)
        flutterFragment!!.onNewIntent(intent)
    }

    override fun onBackPressed() {
        flutterFragment!!.onBackPressed()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        flutterFragment!!.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        flutterFragment!!.onActivityResult(
            requestCode,
            resultCode,
            data
        )
    }

    override fun onUserLeaveHint() {
        flutterFragment!!.onUserLeaveHint()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        flutterFragment!!.onTrimMemory(level)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (flutterFragment != null) {
            flutterFragment = null
        }
    }
}