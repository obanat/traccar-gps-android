package com.example.floatingball;

import static android.view.View.TRANSLATION_X;
import static android.view.View.TRANSLATION_Y;
import static java.security.AccessController.getContext;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;
import java.util.Set;


import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends PreferenceActivity  {
    private static final int PERMISSIONS_REQUEST_LOCATION = 2;
    private static final int PERMISSIONS_REQUEST_LOCATION2 = 3;
    private static final int MSG_FINISH_SELF = 1000;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        initTopUiHandler();

        if (checkGPSPermission()) {
            startFloatingBallService();
        }
        if ("boot".equals(getIntent().getStringExtra("mode"))) {
            //boot complete start activity ,just finish after 5s
            //this hicar caused limitation
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_SELF, 5000);
        }
    }

    private void startFloatingBallService() {
        Intent serviceIntent = new Intent(this, FloatingBallService.class);

        startService(serviceIntent);// 2. 为什么要用startService()？他们的区别

    }

    private void initTopUiHandler() {
        mHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //执行这个定时任务
                switch (msg.what) {
                    case MSG_FINISH_SELF:
                        finish();
                        break;
                    default:
                        break;
                }

            }
        };
    }



    private boolean checkOverPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //Build.VERSION.SDK_INT 表示当前系统的版本号
            //Build.VERSION_CODES.M 表示当前系统的版本号是6.0
            //!Settings.canDrawOverlays(this) 表示当前应用没有悬浮窗权限
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package" + getPackageName()));//参数的含义
            startActivityForResult(intent, 100);// 如果没有设置过悬浮窗权限，则跳转到设置页面
            return false;
        } else {
            return true;
        }
    }
    private boolean checkGPSPermission() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);

        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSIONS_REQUEST_LOCATION2);

        }

        return  true;
    }


    //从设置页面返回后会调用onActivityResult()方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (checkOverPermission()) {
                startFloatingBallService();
            }
        }
    }

    private void startFloatingBallService2() {
        Intent serviceIntent = new Intent(this, FloatingBallService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);// 3. 为什么要用startForegroundService()？
        } else {
            startService(serviceIntent);// 2. 为什么要用startService()？他们的区别
        }
    }

    //解决内存泄漏的问题
//    @Override
//    protected void onDestroy() {
//        if (mFloatingBall != null && mWindowManager != null) {
//            mWindowManager.removeView(mFloatingBall);
//        }
//        super.onDestroy();
//    }
}