package com.example.floatingball;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FloatingPanelView extends LinearLayout {
    private final Context mContex;
    private final LinearLayout mFloatingPanelView;
    private final LinearLayout mLayoutView;
    private boolean isAmination=false;

    private ImageView mFloatingBall;
    private PanelAnimation mPanelAnimation;
    private float mScreenWidth;
    private float mScreenHeight;
    private WindowManager mWindowManager;
    public PanelAnimation getPanelAnimation() {
        Log.d("openPanelAnimation", "getPanelAnimation");
        return mPanelAnimation;
    }
    public FloatingPanelView(Context context, ImageView floatingBall,float screenWidth,float screenHeight,WindowManager windowManager) {
        super(context);
        mContex=context;
        mWindowManager=windowManager;
        LayoutInflater inflater = LayoutInflater.from(mContex);
        inflater.inflate(R.layout.panel_layout,this,true);
        mFloatingPanelView = findViewById(R.id.floating_panel);
        mLayoutView = findViewById(R.id.panel_layout);
        mFloatingBall=floatingBall;
        mScreenWidth=screenWidth;
        mScreenHeight=screenHeight;
        //初始化PanelAnimation
        //mPanelAnimation=new PanelAnimation(this,mScreenWidth,mScreenHeight);
    }
    //添加按钮
    public void addButton(FloatingButtonAction buttonAction){
        ImageView button = new ImageView(mContex);// 创建按钮到面板的context
        button.setImageResource(buttonAction.getIconResourceId());
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAmination){
                    buttonAction.onButtonClick();
                    mFloatingPanelView.setVisibility(View.INVISIBLE);
                    mFloatingBall.setVisibility(View.VISIBLE);
                }
            }
        });

        //设置按钮布局参数
        int size = (int) getResources().getDimension(R.dimen.button_size);
        int margin = (int) getResources().getDimension(R.dimen.button_margin);
        LinearLayout.LayoutParams buttonParams =  new LinearLayout.LayoutParams(size,size);
        buttonParams.setMargins(margin,margin,margin,margin);
        mFloatingPanelView.addView(button,buttonParams);
    }
    public void playOpenAnimator(float startX, float startY) {


        // 实现面板展开动画
        mFloatingPanelView.setVisibility(View.VISIBLE);
        //获取屏幕高度
        float screenHeight = getResources().getDisplayMetrics().heightPixels;
        //获取屏幕宽度
        float screenWidth = getResources().getDisplayMetrics().widthPixels;
         //获取view宽度
       // float panelWidth = mFloatingPanelView.getWidth();
       // float panelHeight= mFloatingPanelView.getHeight();
        float panelOriginalX = screenWidth/2;// X 轴起始位置（相对于父容器）
        float panelOriginalY = screenHeight/2;   // Y 轴起始位置（相对于父容器）

        float startTransactionX= startX -panelOriginalX;
        float startTransactionY= startY-panelOriginalY;

        // 实现面板展开动画
        ObjectAnimator openAnimator = ObjectAnimator.ofPropertyValuesHolder(
                mFloatingPanelView,
                PropertyValuesHolder.ofFloat(SCALE_X,0f,1f),
                PropertyValuesHolder.ofFloat(SCALE_Y,0f,1f),
                PropertyValuesHolder.ofFloat(TRANSLATION_X,startTransactionX,0f),//translationX中f表示偏移的像素 100f表示向右偏移100像素 -100f表示向左偏移100像素
                PropertyValuesHolder.ofFloat(TRANSLATION_Y,startTransactionY,0f)// translationY中f表示偏移的像素 100f表示向下偏移100像素 -100f表示向上偏移100像素
        );
        openAnimator.setDuration(300);
        openAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        openAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAmination=false;
            }
            @Override
            public void onAnimationStart(Animator animation) {
                isAmination=true;
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        openAnimator.start();

    }
    public void playCloseAnimator(float endX, float endY, ImageView floatingBall, FloatingPanelView floatingPanel, WindowManager windowManager) {
        //获取屏幕高度
        //获取屏幕高度
        float screenHeight = getResources().getDisplayMetrics().heightPixels;
        //获取屏幕宽度
        float screenWidth = getResources().getDisplayMetrics().widthPixels;
        //获取view宽度
        // float panelWidth = mFloatingPanelView.getWidth();
        // float panelHeight= mFloatingPanelView.getHeight();
        float panelOriginalX = screenWidth/2;// X 轴起始位置（相对于父容器）
        float panelOriginalY = screenHeight/2;   // Y 轴起始位置（相对于父容器）

        float endTransactionX= endX -panelOriginalX;
        float endTransactionY= endY-panelOriginalY;

        ObjectAnimator closeAnimator = ObjectAnimator.ofPropertyValuesHolder(
                mFloatingPanelView,
                PropertyValuesHolder.ofFloat(SCALE_X,1f,0f),//scale中f表示倍数 1f表示1倍 0f表示消失看不到
                PropertyValuesHolder.ofFloat(SCALE_Y,1f,0f),
                PropertyValuesHolder.ofFloat(TRANSLATION_X,0f,endTransactionX),
                PropertyValuesHolder.ofFloat(TRANSLATION_Y,0f,endTransactionY)
        );
        closeAnimator.setDuration(300);
        closeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        closeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                floatingBall.setVisibility(View.VISIBLE);
                windowManager.removeView(floatingPanel);
               // mFloatingPanelView.setVisibility(View.INVISIBLE);
                isAmination=false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                isAmination=true;
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        closeAnimator.start();
       // mFloatingPanelView.setVisibility(View.INVISIBLE);
     //   mWindowManager.removeView(mFloatingPanel);
        //注销监听器
       // mLayoutView.setOnTouchListener(null);
    }
    //注销监听器
    public void setListenerOnOutsideTouchNull() {
        mLayoutView.setOnTouchListener(null);
    }
    public void setListenerOnOutsideTouch(float endX, float endY, ImageView floatingBall, FloatingPanelView floatingPanel, WindowManager windowManager) {
        mLayoutView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Action_down
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    //获取view的坐标
                    int[] location = new int[2];
                    mFloatingPanelView.getLocationOnScreen(location);
                    int x = (int) event.getRawX();
                    int y = (int) event.getRawY();
                    int panelWidth = mFloatingPanelView.getWidth();
                    int panelHeight= mFloatingPanelView.getHeight();
                    //判断是否点击的面板外部
                    if(x<location[0]||x>location[0]+panelWidth||y<location[1]||y>location[1]+panelHeight){
                        Log.d("closePanel", "closePanel");
                        playCloseAnimator(endX, endY, floatingBall, floatingPanel, windowManager);
                       // mPanelAnimation.playCloseAnimator(endX, endY, mFloatingBall, mFloatingPanel, mWindowManager);
                        mFloatingBall.setVisibility(View.VISIBLE);
                        //注销监听器
                        setListenerOnOutsideTouchNull();
                        //  mWindowManager.removeView(mFloatingPanel);
                        // mWindowManager.addView(mFloatingBall, mBallParams);
                    }

                    return true;
                }
                return false;
            }
        });
    }


}
