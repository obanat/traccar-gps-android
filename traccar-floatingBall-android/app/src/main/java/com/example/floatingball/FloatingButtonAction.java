package com.example.floatingball;

import android.content.Context;

public interface FloatingButtonAction {
    Context mContext = null;
    interface OnButtonClickListener {
        void onButtonClicked();
    }
    void setOnButtonClickListener(OnButtonClickListener listener);
    //获取按钮图标资源
    int getIconResourceId();
    //执行按钮点击事件
    void onButtonClick();

}
