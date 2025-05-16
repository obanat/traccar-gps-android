package com.example.floatingball;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

//实现接口FloatingButtonAction
public class VisitWebButton implements FloatingButtonAction {
    private Context mContext;
    public OnButtonClickListener mListener;
    VisitWebButton(Context context) {
        this.mContext = context;
    }
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mListener=listener;
    }

    @Override
    public int getIconResourceId() {
        return R.mipmap.icon1_foreground;
    }

    @Override
    public void onButtonClick() {
        Log.d("SampleButtonAction", "Button clicked!");
        // 执行按钮点击事件
        // 访问百度页面
       // String url = "https://www.baidu.com";
      //  mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

        String url = "https://www.baidu.com";
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 必须添加此标志
        //mContext.startActivity(intent);

        Intent intent = new Intent(mContext, StatusActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 必须添加此标志
        mContext.startActivity(intent);

        if(mListener!=null){
            mListener.onButtonClicked();
        }
    }

}
