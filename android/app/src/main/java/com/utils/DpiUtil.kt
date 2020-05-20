package com.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.meituan.BuildConfig

/**
 * Created by Edwin,CHEN on 2020/5/20.
 */
class DpiUtil {
    companion object {
        val TAG = DpiUtil::class.java.simpleName

        /**
         * 获取屏幕显示结构信息
         */
        fun getDisplayMitrics(context: Context?):DisplayMetrics? {
            return if (context == null) {
                null
            } else {
                val metrics = DisplayMetrics()
                val manager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                manager.defaultDisplay.getMetrics(metrics)
                metrics
            }
        }

        /**
         * 获取屏幕宽度
         */
        fun getWidth(context: Context?):Int {
            return if (context == null) {
                0
            } else {
                val displayMetrics = getDisplayMitrics(context) as DisplayMetrics
                displayMetrics.widthPixels
            }
        }

        /**
         * 获取屏幕高度
         */
        fun getHeight(context: Context?): Int {
            return if (context == null) {
                0
            } else {
                val displayMetrics = getDisplayMitrics(context) as DisplayMetrics
                displayMetrics.heightPixels
            }
        }

        /**
         * 获取屏幕密度
         * 屏幕密度，density和dpi的关系为 density = dpi/160
         */
        fun getDensity(context: Context?): Float {
            return if (context == null) {
                2.0f // 这里默认每单位2个逻辑像素
            } else {
                val displayMetrics = context.resources.displayMetrics as DisplayMetrics
                displayMetrics.density
            }
        }
        /**
         * 获取字体缩放密度
         *
         */
        fun getFontDensity(context: Context?): Float {
            return if (context == null) {
                2.0f // 这里默认每单位2个逻辑像素
            } else {
                val displayMetrics = context.resources.displayMetrics as DisplayMetrics
                displayMetrics.scaledDensity
            }
        }

        fun dp2Px(context: Context?, dp: Int):Int {
            return ((getDensity(context)*dp).toDouble() + 0.5).toInt()
        }

        fun px2Dp(context: Context?, px: Int):Int {
            return (px/(getDensity(context)).toDouble() + 0.5).toInt()
        }
        fun sp2Px(context: Context?, sp: Int):Int {
            return ((getFontDensity(context)* sp).toDouble() + 0.5).toInt()
        }

        fun px2Sp(context: Context?, px: Int):Int {
            return (px/(getFontDensity(context)).toDouble() + 0.5).toInt()
        }

        /**
         * contentView相关
         */
        private fun getContentView(activity: Activity?): View? {
            return activity?.window?.decorView?.findViewById(Window.ID_ANDROID_CONTENT)
        }

        /**
         * contentView高度
         */
        fun getContentViewHeight(activity: Activity?): Int? {
            val contentView = getContentView(activity)
            return contentView?.height
        }


        private var outSize :Point? = null
        private var defaultDisplay :Display? = null

        /**
         * 获取应用宽度
         */
        fun getAppWidth(activity: Activity?): Int? {
            if (activity != null) {
                try {
                    val point = Point()
                    activity.windowManager.defaultDisplay.getSize(point)
                    return point.x
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG,"e = " + e.printStackTrace())
                    }
                }

            }

            if (outSize == null) {
                synchronized(DpiUtil::class.java) {
                    if (outSize == null) {
                        getPxSize(activity)
                    }
                }
            }

            return outSize?.x
        }

        private fun getPxSize(context: Context?) {
            val display = getDefaultDisplay(context)
            outSize = Point()
            display?.getSize(outSize)
        }

        private fun getDefaultDisplay(context: Context?): Display? {
            if (null == defaultDisplay) {
                val windowManager = context!!.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                defaultDisplay = windowManager.defaultDisplay
            }

            return defaultDisplay
        }
    }
}