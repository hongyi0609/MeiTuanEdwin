package com.edwin.flutter_show

import android.content.Context
import android.view.View
import android.widget.TextView
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView


/**
 * Created by Edwin,CHEN on 2020/6/1.
 */
class NativeTextView :PlatformView,MethodChannel.MethodCallHandler{

    private var myNativeView: TextView? = null

    constructor(context: Context?,binaryMessenger: BinaryMessenger?,id:Int,params:Map<String,Any>){
        val textView = TextView(context)
        textView.text = "我是来自Android的原生TextView"
        this.myNativeView = textView
        if (params.containsKey("content")) {
            val content = params["content"] as String
            myNativeView?.text = content
        }
        val methodChannel = MethodChannel(binaryMessenger,"nativeTextView_$id")
        methodChannel.setMethodCallHandler(this)
    }

    // 在接口的回调方法中可以接收到来自Flutter的调用
    override fun onMethodCall(call: MethodCall?, result: MethodChannel.Result?) {
        if ("setText" == call!!.method){
            val text = call.arguments as String
            myNativeView!!.text = text
            result!!.success(null)
        }
    }

    override fun getView(): View? {
        return myNativeView
    }

    override fun dispose() {

    }
}