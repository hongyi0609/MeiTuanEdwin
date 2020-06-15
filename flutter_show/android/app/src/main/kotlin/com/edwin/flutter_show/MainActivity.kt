package com.edwin.flutter_show

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity : FlutterActivity() {

    companion object {
        private const val METHOD_CHANNEL = "com.edwin.flutter_show/battery"
        private const val FLUTTER_METHOD_CONTENT = "getFlutterContent"
    }

    private lateinit var methodChannel: MethodChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = FrameLayout(this)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        container.layoutParams = lp
        setContentView(container)

        initView(container)

        GeneratedPluginRegistrant.registerWith(this)
        // 自定义view
        NativeFlutterPluginRegistrant().registerWith(this)
        // MethodChannel
        methodChannel = MethodChannel(flutterView, METHOD_CHANNEL)
        methodChannel.setMethodCallHandler({ call, result ->
            // 在主线程中执行
            if ("getBatteryLevel" == call.method) {
                // 获取电量
                val batteryLevel = fetchBatterLevel()
                if (batteryLevel != -1) {
                    // 将电量返回给 Flutter 调用
                    result.success(batteryLevel)
                } else {
                    result.error("UNAVAILABLE", "Battery level not available.", null)
                }
            } else {
                result.notImplemented()
            }
        })
    }

    private fun initView(container: ViewGroup) {
        // 添加flutterView
        container.addView(flutterView)

        // textView 用于点击获取Flutter数据
        val textView = TextView(this)
        textView.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        (textView.layoutParams as FrameLayout.LayoutParams).gravity = (Gravity.LEFT or Gravity.CENTER_VERTICAL)
        textView.text = "获取Flutter数据"
        container.addView(textView)
        textView.setOnClickListener {
            // Native 调用 Flutter 的 getFlutterContent 函数
            methodChannel.invokeMethod(FLUTTER_METHOD_CONTENT, null, object : MethodChannel.Result {
                override fun notImplemented() {
                    Log.e("BatteryPlugin", "Dart getFlutterContent() notImplemented")
                    asyncShow("Dart getFlutterContent() notImplemented")
                }

                override fun error(error: String?, message: String?, p2: Any?) {
                    Log.e("BatteryPlugin", "Dart getFlutterContent() error : " + error)
                    asyncShow("Dart getFlutterContent() error :$error")
                }

                override fun success(o: Any?) {
                    Log.e("BatteryPlugin", "Dart getFlutterContent() result : " + o)
                    asyncShow("Dart getFlutterContent() result : $o")
                }
            })
        }
    }


    @SuppressLint("InlinedApi")
    private fun fetchBatterLevel(): Int {
        val batteryLevel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        }
        return batteryLevel
    }


    private fun asyncShow(msg:String){
        Handler().post {
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
        }
    }
}
