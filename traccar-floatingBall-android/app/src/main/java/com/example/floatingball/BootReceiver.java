package com.example.floatingball;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // 创建启动 Activity 的 Intent
            Intent launchIntent = new Intent(context, MainActivity.class);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 关键 Flag
            launchIntent.putExtra("mode", "boot");
            context.startActivity(launchIntent);

            //Intent serviceIntent = new Intent(context, FloatingBallService.class);

            //context.startService(serviceIntent);// 2. 为什么要用startService()？他们的区别

        }
    }
}