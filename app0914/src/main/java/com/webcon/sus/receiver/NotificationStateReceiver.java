package com.webcon.sus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.webcon.wp.utils.WPApplication;
import com.webcon.wp.utils.WPConstant;

/**
 * Created by Administrator on 15-11-24.
 */
public class NotificationStateReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        Log.i("Notification",""+action);
//        Bundle bundle = intent.getExtras();
        if (null!=action){
            if (action.equals(WPConstant.ALARM_NOTIFICATION_POP)) {
                WPApplication.getInstance().notificationStatus=WPConstant.ALARM_NOTIFICATION_ON;
                Log.i("Notification","-----notification is showing------");
            }
            else if (action.equals(WPConstant.ALARM_NOTIFICATION_DISMISS)) {
                WPApplication.getInstance().notificationStatus=WPConstant.ALARM_NOTIFICATION_OFF;
                Log.i("Notification","-----notification is not showing------");
            }
        }

    }
}
