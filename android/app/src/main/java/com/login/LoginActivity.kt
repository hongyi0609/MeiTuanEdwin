package com.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.login.LoginManagerInstance.LoginStatusChangeEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * 登录界面
 */
class LoginActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 注册事件
        EventBus.getDefault().register(this)
    }

    public override fun onDestroy() {
        super.onDestroy()

        // 取消注册事件
        EventBus.getDefault().unregister(this)
    }

    // 订阅事件
    @Subscribe
    fun onLoginStatusChanged(event: LoginStatusChangeEvent) {
        val isLoggedIn = event.isLoggedIn
        // 处理登录状态变化事件
        handleLoginStateChanged()
    }

    private fun handleLoginStateChanged() {}
}