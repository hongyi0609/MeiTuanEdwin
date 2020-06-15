package com.edwin.flutter_show

import android.content.Context
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

/**
 * Created by Edwin,CHEN on 2020/6/1.
 */
class NativePlatformViewFactory : PlatformViewFactory {

    private var messenger :BinaryMessenger? = null

    constructor(messenger: BinaryMessenger,createArgsCodec: MessageCodec<Any>) : super(createArgsCodec){
        this.messenger = messenger
    }

    @SuppressWarnings("unchecked")
    override fun create(context: Context?, id: Int, args: Any?): PlatformView {

        return NativeTextView(context,messenger,id,args as Map<String, Any>)
    }

}