package com.nfc.plugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyFirstReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context,"received in MyBroadcastReceiver",Toast.LENGTH_SHORT).show();
        String action = intent.getAction();
        if("android.intent.action.SCREEN_ON".equals(action)){
            System.out.println("onReceive ：屏幕解锁了");

        }else if("android.intent.action.SCREEN_OFF".equals(action)){
            System.out.println("onReceive：屏幕锁定了");
        }
    }
}
