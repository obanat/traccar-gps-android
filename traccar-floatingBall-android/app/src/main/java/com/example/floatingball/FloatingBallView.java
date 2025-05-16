package com.example.floatingball;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;


public class FloatingBallView extends ImageView {
    private ImageView mFloatingBall;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mBallParams;
    private Context mContext;
    private PositionProvider positionProvider;
    private PositionProvider.PositionListener listener;
    public FloatingBallView(Context context) {
        super(context);
        mContext = context;
       // this.mWindowManager = windowManager;
        listener = new PositionProvider.PositionListener() {
            @Override
            public void onPositionUpdate(Position position) {
                StatusActivity.addMessage("FloatingBallView pos update");
            }

            @Override
            public void onPositionError(Throwable error) {

            }
        };

        /*positionProvider = new AndroidPositionProvider(context, listener);
        try {
            positionProvider.startUpdates();
        } catch (SecurityException e) {
            Log.w("TAG", e);
        }*/
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return false;
    }

    public void initFloatingBall() {
        mFloatingBall = new ImageView(mContext);
        mFloatingBall.setImageResource(R.drawable.floatingball);
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

//        Context applicationContext = getApplicationContext();
//        mFloatingBallController = new FloatingBallController(applicationContext, mFloatingBall, mWindowManager, mBallParams);
//        mFloatingBallController.setupDragListener();
    }

}
