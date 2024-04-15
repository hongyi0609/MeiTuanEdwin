package com.login;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 模仿切换角色时保持登录状态
 */
public class LoginManager {

    private static final String PREF_NAME = "login_status";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private Context context;

    public LoginManager(Context context) {
        this.context = context;
    }

    /**
     * 使用SharedPreferences：SharedPreferences是Android提供的一种轻量级的数据存储方式，适合用于保存一些简单的配置
     * 信息或状态，例如用户的登录状态。
     * @param isLoggedIn
     */
    public void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        String encryptedStatus = EncryptionUtils.encrypt(String.valueOf(isLoggedIn));
        editor.putString(KEY_IS_LOGGED_IN, encryptedStatus);
        editor.apply();
    }

    public boolean getLoginStatus() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        String encryptedStatus = sharedPreferences.getString(KEY_IS_LOGGED_IN, null);
        if (encryptedStatus != null) {
            String decryptedStatus = EncryptionUtils.decrypt(encryptedStatus);
            return Boolean.parseBoolean(decryptedStatus);
        }
        return false;
    }
}


