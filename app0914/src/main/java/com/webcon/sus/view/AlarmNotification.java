package com.webcon.sus.view;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.webcon.sus.activities.AlarmDetailsActivityCompat;
import com.webcon.sus.demo.R;

/**
 * 报警通知
 * @author Vieboo
 * ##TODO：报警通知
 */
public class AlarmNotification {
	
	private Notification mNotification;
	private Context context;
	
	public AlarmNotification(Context context){
		this.context = context;
	}
	
	public Notification getNotificationInit(String name, String date, String time, short level){
		String notifMessage = context.getResources().getString(R.string.alarm_notification_info1)
				+ name + context.getResources().getString(R.string.alarm_notification_info2);
		mNotification = new Notification();
		mNotification.icon = R.drawable.notification_icon;
		mNotification.when = System.currentTimeMillis();
		mNotification.tickerText = notifMessage;
		mNotification.defaults = Notification.DEFAULT_ALL;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		
		Intent notifIntent = new Intent();
		notifIntent.setClass(context, AlarmDetailsActivityCompat.class);
		String[] alarmInfos = new String[]{name, date, time, level + ""};
		notifIntent.putExtra("alarmInfos", alarmInfos);
		notifIntent.setAction(Intent.CATEGORY_LAUNCHER);
		PendingIntent pendingIntent = PendingIntent.getActivity(
				context, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		mNotification.setLatestEventInfo(
				context, 
				context.getResources().getString(R.string.alarm_notification_title), 
				notifMessage, 
				pendingIntent);
		return mNotification;
	}
}
