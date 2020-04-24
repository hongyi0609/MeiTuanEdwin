package com.edwin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.common.HomeReactInstanceManager;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.meituan.R;

/**
 * Created by Edwin,CHEN on 2019/10/14.
 */

public class HomeActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private final int OVERLAY_PERMISSION_REQ_CODE = 1;  // Choose any value
    private ReactInstanceManager mReactInstanceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
//            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                    Uri.parse("package:" + getPackageName()));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
//        } else {
//            initReactInstanceManager();
//            if (savedInstanceState == null) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.home_container, HomeFragment.createHomeFragment()).commit();
//            }
//        }

        initReactInstanceManager();
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.home_container, HomeFragment.createHomeFragment()).commit();
        }

    }

    private void initReactInstanceManager() {
        HomeReactInstanceManager homeReactInstanceManager = HomeReactInstanceManager.newInstance();
        mReactInstanceManager = homeReactInstanceManager.init(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(this, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted
                    Toast.makeText(this, "请打开显示悬浮窗权限", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "请打开显示悬浮窗权限");
                } else {
                    Toast.makeText(this, "授权成功", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "授权成功");

                }
            }
        }
        mReactInstanceManager.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
            mReactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
