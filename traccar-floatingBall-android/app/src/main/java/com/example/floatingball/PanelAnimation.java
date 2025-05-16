package com.example.floatingball;

import static android.view.View.SCALE_X;
import static android.view.View.SCALE_Y;
import static android.view.View.TRANSLATION_X;
import static android.view.View.TRANSLATION_Y;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PanelAnimation {
    private FloatingPanelView mFloatingPanelView;
    private float mScreenWidth;
    private float mScreenHeight;
    private boolean isAmination=false;
    private LinearLayout mLayoutView;
    PanelAnimation(FloatingPanelView mFloatingPanelView, float mScreenWidth, float mScreenHeight) {
       // this.mFloatingPanelView = mFloatingPanelView;
        this.mScreenWidth = mScreenWidth;
        this.mScreenHeight = mScreenHeight;
       // this.isAmination=isAmination;
      //  this.mLayoutView=mLayoutView;
    }
    public void playOpenAnimator(float startX, float startY, FloatingPanelView mFloatingPanelView) {

        Log.d("openPanelAnimation", "playOpenAnimator: ");
        // 实现面板展开动画
        this.mFloatingPanelView=mFloatingPanelView;
        mFloatingPanelView.setVisibility(View.VISIBLE);
        //获取屏幕高度
       // float screenHeight = getResources().getDisplayMetrics().heightPixels;
        //获取屏幕宽度
      //  float screenWidth = getResources().getDisplayMetrics().widthPixels;
        //获取view宽度
        // float panelWidth = mFloatingPanelView.getWidth();
        // float panelHeight= mFloatingPanelView.getHeight();
        float panelOriginalX = mScreenWidth/2;// X 轴起始位置（相对于父容器）
        float panelOriginalY = mScreenHeight/2;   // Y 轴起始位置（相对于父容器）

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
               // isAmination=false;
            }
            @Override
            public void onAnimationStart(Animator animation) {
               // isAmination=true;
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
    public void playCloseAnimator(float endX, float endY, ImageView mFloatingBall, FloatingPanelView mFloatingPanel, WindowManager mWindowManager) {
        //获取屏幕高度
        //获取屏幕高度
      //  float screenHeight = getResources().getDisplayMetrics().heightPixels;
        //获取屏幕宽度
      //  float screenWidth = getResources().getDisplayMetrics().widthPixels;
        //获取view宽度
        // float panelWidth = mFloatingPanelView.getWidth();
        // float panelHeight= mFloatingPanelView.getHeight();
        float panelOriginalX = mScreenWidth/2;// X 轴起始位置（相对于父容器）
        float panelOriginalY = mScreenHeight/2;   // Y 轴起始位置（相对于父容器）

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
                mFloatingBall.setVisibility(View.VISIBLE);
                mWindowManager.removeView(mFloatingPanel);
                // mFloatingPanelView.setVisibility(View.INVISIBLE);
                //isAmination=false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                //isAmination=true;
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

        //注销监听器
       // mLayoutView.setOnTouchListener(null);
    }

}
