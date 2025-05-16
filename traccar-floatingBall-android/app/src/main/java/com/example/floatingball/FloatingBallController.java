package com.example.floatingball;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FloatingBallController {
    private FloatingBallView mFloatingBall;
    private WindowManager.LayoutParams mBallParams;
    private WindowManager mWindowManager;
    private FloatingPanelView mFloatingPanel;
    private Context mContext;
    private Context mApplicationContext;
    private float mScreenWidth;
    private float mScreenHeight;
    private LinearLayout mLayoutView;

    FloatingBallController(Context applicationContext, FloatingBallView floatingBall, WindowManager windowManager, WindowManager.LayoutParams ballParams) {
        mContext = applicationContext;
        mFloatingBall = floatingBall;
        mWindowManager = windowManager;
        mBallParams = ballParams;
       // mApplicationContext = applicationContext;
       // mLayoutView = layoutView;
        //获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mFloatingPanel = new FloatingPanelView(mContext,mFloatingBall,mScreenWidth,mScreenHeight,mWindowManager);
        addButtonToPanel(mContext);
    }

    public FloatingPanelView getFloatingPanel() {
        return this.mFloatingPanel;
    }
    public void setupDragListener() {
        mFloatingBall.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("clickedFloatingBall", "openPanel");

                    }
                }
        );
    mFloatingBall.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float touchX, touchY;
            private boolean isDragging = false;
            //获取窗口的中心位置
            private float floatingBallX;
            private float floatingBallY;
            private int slopTouch = ViewConfiguration.get(mContext).getScaledTouchSlop();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //获取mFloatingBall的宽度

                floatingBallX = mBallParams.x+ (float) mFloatingBall.getWidth() /2;
                floatingBallY = mBallParams.y+ (float) mFloatingBall.getHeight()/2;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = mBallParams.x;
                        initialY = mBallParams.y;
                        // 获取触摸事件的原始屏幕坐标
// getRawX/Y返回的是相对于屏幕左上角的绝对坐标，而不是相对于视图的相对坐标
                        touchX = event.getRawX();
                        touchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float deltaX = event.getRawX() - touchX;
                        float deltaY = event.getRawY() - touchY;
                        Log.d("slopTouch", String.valueOf(slopTouch));
                        if (!isDragging && (Math.abs(deltaX) > slopTouch || Math.abs(deltaY) > slopTouch)) {//像素
                            isDragging = true;
                        }
                        if (isDragging) {
                            mBallParams.x = initialX + (int) deltaX;
                            mBallParams.y = initialY + (int) deltaY;
                            mWindowManager.updateViewLayout(mFloatingBall, mBallParams);
                            floatingBallX = mBallParams.x+ (float) mFloatingBall.getWidth() /2;
                            floatingBallY = mBallParams.y+ (float) mFloatingBall.getHeight()/2;
                            //floatingBallX = touchX;
                            // floatingBallY = touchY;
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (isDragging) {
                            autoAttachToEdge();
                            isDragging = false;
                        } else {
                            Log.d("openPanel", "openPanel");
                            //出现面板动画
                            //初始化PanelAnimation
                            openPanel(floatingBallX, floatingBallY);
                            v.performClick();
                        }
                        return true;
                }
                return false;
            }
        });
    }
    private void autoAttachToEdge() {
//        DisplayMetrics metrics = new DisplayMetrics();
//        mWindowManager.getDefaultDisplay().getMetrics(metrics);
//        int screenWidth = metrics.widthPixels;
        int centerX = mBallParams.x + mFloatingBall.getWidth() / 2;
        boolean isLeft = centerX < mScreenWidth / 2;
        mBallParams.x = isLeft ? 0 : (int) (mScreenWidth - mFloatingBall.getWidth());
        mWindowManager.updateViewLayout(mFloatingBall, mBallParams);
    }
    private void openPanel(float startX, float startY) {
//        if (mFloatingPanel != null) {
//            mWindowManager.removeView(mFloatingBall);
//        }
        //将悬浮球移除windowManager
        //将窗口中的悬浮球设置为不可见
        mFloatingBall.setVisibility(View.INVISIBLE); //modify
        //把面板加入到windowManager 初始状态设置为invisible
       // mFloatingPanel = new FloatingPanelView(mContext,mFloatingBall,mScreenWidth,mScreenHeight);
        //添加按钮到面板
       // addButtonToPanel(mContext);
        //面板windows参数
        int type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        WindowManager.LayoutParams panelParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        //加入到windowManager
        panelParams.gravity = Gravity.CENTER;
        mWindowManager.addView(mFloatingPanel, panelParams);

        //属性动画面板放大
        // test((int)startX, (int)startY);
        mFloatingPanel.playOpenAnimator(startX, startY);//以屏幕左上角为原点的坐标
        mFloatingPanel.setListenerOnOutsideTouch(startX, startY, mFloatingBall, this.mFloatingPanel, mWindowManager);

//        if(mFloatingPanel.getPanelAnimation()!=null){
//            Log.d("openPanelAnimation", "openPanel");
//            mFloatingPanel.getPanelAnimation().playOpenAnimator(startX, startY,mFloatingPanel);
//            mFloatingPanel.setListenerOnOutsideTouch(startX, startY, mFloatingBall, mFloatingPanel, mWindowManager);
//        }
    }
    private void addButtonToPanel(Context context){
        if(mFloatingPanel != null){
            mFloatingPanel.addButton(new VisitWebButton(context){
                @Override
                public void setOnButtonClickListener(OnButtonClickListener listener) {
                    super.setOnButtonClickListener(listener);
//                    listener.onButtonClicked(() -> {
//                        mFloatingBall.setVisibility(View.VISIBLE);
//                        mFloatingPanel.setVisibility(View.INVISIBLE);
//                        Log.d("clickedButton1", "closePanel");
//                    });
                }
            });
            mFloatingPanel.addButton(new HomeButton(context){
                @Override
                public void setOnButtonClickListener(OnButtonClickListener listener) {
                    super.setOnButtonClickListener(listener);
//                    listener.onButtonClicked(() -> {
//                        mFloatingBall.setVisibility(View.VISIBLE);
//                        mFloatingPanel.setVisibility(View.INVISIBLE);
//                        Log.d("clickedButton1", "closePanel");
//                    });
                }
            });

        }
    }
}
