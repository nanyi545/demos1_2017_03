package com.webcon.sus.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.webcon.sus.demo.R;

/**
 * @author m
 */
public class AliveNotification {
    private static final String TAG = "AliveNotification";

    public static final int NOTI_FLAG_NEW   = 1;
    public static final int NOTI_FLAG_MAIN  = 2;
    public static final String NOTI_FLAG = "Noti_Broadcast_Flag";
    public static final String NOTI_ACTION = "action.aliveNotification";
    public static final int NOTI_ID = 0xAAA;
    private static final String NOTI_EXTRA = "extra";
    private static final String ALARM_DETAIL_CLASS_NAME = "com.webcon.sus.activities.AlarmDetailsActivityCompat";
    private static final String MAIN_CLASS_NAME = "com.webcon.sus.activities.MainActivityCompat";
    private static final String LOGIN_CLASS_NAME = "com.webcon.sus.activities.LoginActivityCompat";

    private NotificationManager mManager;
    private Notification mNotification;
    private RemoteViews rview;
    private Context context;
    private NoticeReceiver mReceiver;

    public AliveNotification(Context context){
        this.context = context;
        init();
    }

    private void init(){
        mManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        //广播接收
        mReceiver = new NoticeReceiver();
        IntentFilter filter = new IntentFilter(NOTI_ACTION);
        context.registerReceiver(mReceiver, filter);

        createNotice();
        mManager.notify(NOTI_ID, mNotification);
    }

    private void createNotice(){
        Log.i(TAG, "package name:" + context.getPackageName());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        rview = new RemoteViews(context.getPackageName(), R.layout.remote_layout_notification_1);

//        //点击跳转-->报警消息页面
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        ComponentName cn = new ComponentName(context.getPackageName(), ALARM_DETAIL_CLASS_NAME);
//        intent.setComponent(cn);
//        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        rview.setOnClickPendingIntent(R.id.tv_remote_notification_news, pi);
//
//        //点击跳转-->主界面
//        Intent intent2 = new Intent();
//        ComponentName cn2 = new ComponentName(context.getPackageName(), MAIN_CLASS_NAME);
//        intent2.setAction(Intent.ACTION_MAIN);
//        intent2.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent2.setComponent(cn2);
//        PendingIntent pi2 = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
//        rview.setOnClickPendingIntent(R.id.linear_remote_container, pi2);
//
//        //清除&退出 后台进程
//        Intent intent3 = new Intent(NOTI_ACTION);
//        intent3.putExtra(NOTI_EXTRA, 0x99);
//        PendingIntent pi3 = PendingIntent.getBroadcast(context, 0x99, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
//        rview.setOnClickPendingIntent(R.id.bn_remote_notification_quit, pi3);

        builder.setContentTitle(context.getResources().getString(R.string.app_name))
                .setContent(rview)
                .setTicker("有新报警消息")
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.icon_c_red);

        mNotification = builder.build();
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
    }


    public void newNotice(){

    }

    public void release(){
        context.unregisterReceiver(mReceiver);
        context = null;
    }

    public class NoticeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            int flag = intent.getIntExtra(NOTI_EXTRA, -1);
            Log.i(TAG, "onReceive:" + flag);
            switch(flag){
                case 0x99:
                    mManager.cancelAll();
                    break;
                default:
                    break;
            }
        }
    }
}
