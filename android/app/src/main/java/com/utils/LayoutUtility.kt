package com.utils

import android.app.Activity
import android.util.DisplayMetrics
import android.view.ViewGroup
import java.lang.ref.WeakReference

class LayoutUtility private constructor() {

    init {
        println("LayoutUtility instance is created.")
    }

    companion object {
        val instance: LayoutUtility by lazy { Holder.INSTANCE }

        private object Holder {
            val INSTANCE = LayoutUtility()
        }
    }

    fun calculateExposureRatio(viewGroup: ViewGroup): Float {
        val location = IntArray(2)
        viewGroup.getLocationOnScreen(location)
        val top = location[1] // ViewGroup 在屏幕上的顶部位置
        // 弱引用
        val weakReference = WeakReference(viewGroup.context as Activity)
        val screenHeight = getScreenHeight(weakReference) // 获取屏幕高度
        val visibleHeight = screenHeight - top // 计算 ViewGroup 在屏幕中可见的高度
        return if (visibleHeight <= 0 || screenHeight <= 0) {
            0f
        } else visibleHeight.toFloat() / viewGroup.height
    }

    private fun getScreenHeight(reference: WeakReference<Activity>): Int {
        val displayMetrics = DisplayMetrics()
        reference.get()!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}