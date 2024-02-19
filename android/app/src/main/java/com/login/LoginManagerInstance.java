package com.login;

import org.greenrobot.eventbus.EventBus;

/**
 * 这段代码演示了如何使用全局变量 isLoggedIn 来保存和读取用户的登录状态。全局变量可以在应用的任何地方访问，但需要注意一些
 * 问题：
 * <p>
 *     线程安全性：全局变量在多线程环境下可能会存在竞态条件（race condition）的问题，需要确保对全局变量的访问是线程安全
 *     的。通过单例模式解决
 * </p>
 * <p>
 *     生命周期管理：全局变量的生命周期和应用的生命周期相同，如果应用被销毁，全局变量也会被销毁，需要谨慎管理。
 * </p>
 * <p>
 *     代码可维护性：过多使用全局变量会导致代码的可维护性下降，因为全局变量使得数据流动变得不可控，不利于代码的理解和调试。
 * </p>
 */
public class LoginManagerInstance {
    private static LoginManagerInstance instance;
    private boolean isLoggedIn = false;

    private LoginManagerInstance() {
        // 私有构造函数，避免外部实例化
    }

    public static synchronized LoginManagerInstance getInstance() {
        if (instance == null) {
            instance = new LoginManagerInstance();
        }
        return instance;
    }

    public void saveLoginStatus(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
        // 发送登录状态变化事件
        EventBus.getDefault().post(new LoginStatusChangeEvent(isLoggedIn));
    }

    public boolean getLoginStatus() {
        return isLoggedIn;
    }

    public static class LoginStatusChangeEvent {
        private boolean isLoggedIn;

        public LoginStatusChangeEvent(boolean isLoggedIn) {
            this.isLoggedIn = isLoggedIn;
        }

        public boolean isLoggedIn() {
            return isLoggedIn;
        }
    }
}

