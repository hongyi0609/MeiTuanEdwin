package com.rxjava.entity

/**
 * Created by Edwin,CHEN on 2020/5/12.
 */
data class SwordMan constructor(var name: String, var level: String){

    var school :String? = null

    init {
        // 先走构造方法，再走初始化
        school = "门派"
    }
}