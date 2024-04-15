package com.base

import android.annotation.SuppressLint
import android.app.Application

/**
 * Created by Edwin on 2020/9/25.
 * @author Edwin
 */
@SuppressLint("Registered")
open class BaseApplication :Application(){

    override fun onCreate() {
        super.onCreate()
    }
}