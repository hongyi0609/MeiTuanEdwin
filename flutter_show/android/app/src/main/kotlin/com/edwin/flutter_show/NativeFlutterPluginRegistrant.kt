package com.edwin.flutter_show

import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.StandardMessageCodec

/**
 * Created by Edwin,CHEN on 2020/6/1.
 */
class NativeFlutterPluginRegistrant :PluginRegistry.PluginRegistrantCallback {
    override fun registerWith(registry: PluginRegistry?) {
        val key:String = NativeFlutterPluginRegistrant::class.java.canonicalName
        if (registry!!.hasPlugin(key)) return
        val registrar: PluginRegistry.Registrar = registry.registrarFor(key)
        // 上面代码中使用了plugins.nightfarmer.top/myview这样一个字符串，这是组件的注册名称，
        // 在Flutter调用时需要用到，你可以使用任意格式的字符串。
        registrar.platformViewRegistry().registerViewFactory("nativeTextView",NativePlatformViewFactory(registrar.messenger(),StandardMessageCodec.INSTANCE))
    }
}