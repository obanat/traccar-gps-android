package com.example.floatingball;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class HomeButton implements FloatingButtonAction {
    private Context mContext;
    public OnButtonClickListener mListener;
    HomeButton(Context context) {
        this.mContext = context;
    }

    @Override
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mListener=listener;
    }
    @Override
    public int getIconResourceId() {
        return R.mipmap.icon2_foreground;
    }

    @Override
    public void onButtonClick() {
        // 执行按钮点击事件
        // 返回屏幕首页
        //Intent intent= new Intent(mContext, MainActivity.class);
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        //intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        //Uri uri = Uri.fromParts("package",mContext.getPackageName(), null);
        //intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 必须添加此标志
        mContext.startActivity(intent);
        if(mListener!=null){
            mListener.onButtonClicked();
        }
    }

}

