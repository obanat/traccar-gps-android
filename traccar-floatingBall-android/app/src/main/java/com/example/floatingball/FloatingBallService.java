package com.example.floatingball;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import android.support.v4.app.NotificationCompat;

public class FloatingBallService extends Service {
    private static final int NOTIFICATION_ID = 1;//配置为前台服务

    public FloatingBallService() {
    }

    //1. service的生命周期 onBind作用
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    private FloatingBallView mFloatingBall;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mBallParams;
    private FloatingBallController mFloatingBallController;

    private TrackingController trackingController;
    private PowerManager.WakeLock wakeLock;
    @Override
    public void onCreate() {
        super.onCreate();
        //创建并显示前台服务通知
        startForeground(NOTIFICATION_ID, createNotification());
        /*
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mFloatingBall= new FloatingBallView(this, mWindowManager);
        //初始化悬浮球 加载到窗口中
        mFloatingBall.initFloatingBall();*/
        //悬浮球的交互功能
        //悬浮球和面板的交互
        initFloatingBall();
        trackingController = new TrackingController(this);
        trackingController.start();

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Position.KEY_WAKELOCK, true)) {
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            wakeLock.acquire();
        }

        StatusActivity.addMessage(getString(R.string.status_service_create));
    }
    private Notification createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 处理 API 26+ 的逻辑
            NotificationChannel channel = new NotificationChannel("FloatingBallChannel", "Floating Ball Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }

            return new Notification.Builder(this, "FloatingBallChannel").setContentTitle("悬浮球运行中").setContentText("点击关闭").setSmallIcon(R.mipmap.ic_launcher).build();
        } else {
            // 处理 API 24-25 的逻辑：使用旧版通知构建方式（无渠道）
            return new NotificationCompat.Builder(this).setContentTitle("悬浮球运行中").setContentText("点击关闭").setSmallIcon(R.mipmap.ic_launcher).setPriority(NotificationCompat.PRIORITY_LOW).build();
        }
    }
    private void initFloatingBall() {
        mFloatingBall = new FloatingBallView(this);
        mFloatingBall.setImageResource(R.drawable.floatingball);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        mBallParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, type, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        mBallParams.gravity = Gravity.TOP | Gravity.START;
        mBallParams.x = screenWidth - 150;
        mBallParams.y = 300;
        // mBallParams.x /y 距离屏幕左边和上边的距离 表示窗口的左上角坐标
        //以屏幕左上角为原点 建立坐标系
        mWindowManager.addView(mFloatingBall, mBallParams);
        //setupDragListener();
        Context applicationContext = getApplicationContext();
        mFloatingBallController = new FloatingBallController(applicationContext, mFloatingBall, mWindowManager, mBallParams);
        mFloatingBallController.setupDragListener();
    }


    @Override
    public void onDestroy() {
        if (mFloatingBall != null && mWindowManager != null) {
            mWindowManager.removeView(mFloatingBall);
        }
        if (mFloatingBallController.getFloatingPanel()!= null && mWindowManager != null) {
            mWindowManager.removeView(mFloatingBallController.getFloatingPanel());
        }
        StatusActivity.addMessage(getString(R.string.status_service_destroy));
        super.onDestroy();
    }

}
