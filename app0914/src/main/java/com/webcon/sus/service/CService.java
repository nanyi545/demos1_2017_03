package com.webcon.sus.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.webcon.sus.activities.AlarmDetailsActivityCompat;
import com.webcon.sus.activities.MainActivityCompat;
import com.webcon.sus.activities.MonitorActivityCompat;
import com.webcon.sus.demo.R;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.entity.BaseDevice;
import com.webcon.sus.entity.DeviceFactory;
import com.webcon.sus.entity.StationNode;
import com.webcon.sus.eventObjects.ErrorEvent;
import com.webcon.sus.eventObjects.MessageEvent;
import com.webcon.sus.eventObjects.ServiceEvent;
import com.webcon.sus.eventObjects.StationEvent;
import com.webcon.sus.utils.AlarmMsgManager;
import com.webcon.sus.utils.CommunicationUtils;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.CCallbackMethod;
import com.webcon.wp.utils.NotificationCancelManager;
import com.webcon.wp.utils.WPApplication;
import com.webcon.wp.utils.WPConstant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 后台服务
 * <p>
 * 1.维护通信库的运行，主要是保持服务器的连接，以及回调监听；<br>
 * 2.维护用户登录状态，以及主界面退出后重新进入的恢复（处理内部请求）；<br>
 * 3.生存周期的始末（主要）：在Login界面登陆线程中启动，在MainActivity的退出线程中结束；<br>
 * 4.类似于一个server端，接收一些组件或者操作的请求，然后做出处理。
 * </p>
 * @author m
 */
public class CService extends Service {
    private static final String TAG = "CService";
    private static final int FORE_NOTI_ID = 0x314;

    private PowerManager.WakeLock mWakeLock;
    private Context mContext;

    private boolean initialized = false;
    private boolean initError = false;

    private static int ret_int = 9999;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, " service created in process ID:" + android.os.Process.myPid());

        EventBus.getDefault().register(this);
        mContext = this;
        WPApplication.getInstance().serviceExist = true;
        WPApplication.getInstance().setAccessable(true);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand");

        // 启动电量控制锁，保持cpu不睡眠
        PowerManager mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WPConstant.TAG_WP);
        if (!mWakeLock.isHeld()) {   //wakelock是否已经获取
            mWakeLock.acquire();
        }

        // ------------启动管理的通知栏前台-------------------
        startForeground(FORE_NOTI_ID, createNotice());

        //
        int callbackObj = 0;
        try{
            callbackObj = CCallbackMethod.getInstance(this).setCallBackObj();
            Log.i(TAG, "callbackObj: "+ callbackObj);
        } catch (UnsatisfiedLinkError e){
            e.printStackTrace();
        }

        if (callbackObj < 0) {
            Toast.makeText(this, R.string.str_dowrokservice_toast_a, Toast.LENGTH_SHORT).show();
        } else {
            initSoundFile();
            new DoworkThread().start();
        }

//        return START_STICKY;
        return START_NOT_STICKY;//NOTE:先不重启，因为没有对崩溃做处理。。。。
    }








    /**
     * doworking....
     */
    private class DoworkThread extends Thread {

        @Override
        public void run() {
            Log.i(TAG, "Dowork-Start"+Thread.currentThread().getName());
//            Log.i("AAA", "Dowork-Start"+Thread.currentThread().getName());
            if(WPApplication.DEBUG_NO_LOGIN){
                //TODO: --then remove test---
                Log.i("AAA","before testDATA"+WPApplication.getInstance().getNewAlarms());
                testData();// 不登录
                Log.i("AAA", "after testDATA"+WPApplication.getInstance().getNewAlarms());
            }else{
                //TODO: 初始化开始
                CommunicationUtils.getInstance().loadInitialData();
            }


            if(!WPApplication.DEBUG_NO_LOGIN) {  //正常执行时候运行---
                ret_int=CCallbackMethod.getInstance(CService.this).doWork(
                        "com/webcon/wp/utils/CCallbackMethod",
                        "onCallback",
                        "callbackBuffer"
                );
                Log.i("AAA","--finally callback"+ret_int);
            }
            Log.w("AAA", "------------------Dowork-Terminated-------------------");
            Log.i(TAG, "------------------Dowork-Terminated-------------------");
        }
    }


    /**
     * stop all previews--> send stop preview request to all devices with type (SUConstant.DEVICE_TYPE_MONITOR)
     *
     * this is needed to prevent spurious video packages...
     */
    private void stopPreview(){
        List<StationNode> mStationList=null;
        while(mStationList==null) {     // loop until the station information is obtained from internet!
            try {
                mStationList = WPApplication.getInstance().getStationList();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
//        Log.i("CCC",""+(null==mStationList));
//        Log.i("CCC",""+(mStationList.size()));
        if (mStationList!=null & mStationList.size()>0){  // get station list
            Iterator<StationNode> stationNodeIterator=mStationList.iterator();
            while (stationNodeIterator.hasNext()){
                StationNode stationNode=stationNodeIterator.next();

                List<BaseDevice> deviceList=stationNode.getDeviceList();
                if (deviceList!=null & deviceList.size()>0 & stationNode.isOnline() ){  // get device list for those online station
                    Iterator<BaseDevice> deviceIterator=deviceList.iterator();
                    while(deviceIterator.hasNext()){
                        BaseDevice currentDevice=deviceIterator.next();
                        if (currentDevice.getType()==SUConstant.DEVICE_TYPE_MONITOR){
                            new Thread(new StopPrevRunnable(stationNode.getId(),currentDevice.getName())).start();
                        }
                    }
                }
            }
        }
    }



    /**
     * 请求线程，包括：播放警告、开始预览、停止预览
     */
    private class StopPrevRunnable implements Runnable {
        private String deviceName;
        private int stationId;
        private short pdu = SUConstant.SUB_REQUEST_STOP_PREVIEW;
        private int extra = 0;

        public StopPrevRunnable(int stationId, String deviceName) {
            this.stationId = stationId;
            this.deviceName=deviceName;
        }

        @Override
        public void run() {
            //--stop preview: pdu=SUConstant.SUB_REQUEST_STOP_PREVIEW,extra=0,

            byte[] req = CommunicationUtils.getInstance().createRequestSys(pdu, stationId, deviceName, extra);
            int count = 0;
            int limited = 3;

            boolean ret = CommunicationUtils.getInstance().requestPtzDevice(SUConstant.PDU_C_CLI_REQ_WIN, req);

            // ret=false 且 count<3        且场站在线
            while (!ret && count < limited && WPApplication.getInstance().getStationNode(stationId).isOnline()) {
                ret = CommunicationUtils.getInstance().requestPtzDevice(SUConstant.PDU_C_CLI_REQ_WIN, req);
                Log.w(TAG, "request:" + pdu + " result:" + ret);
                count++;
            }

        }
    }




    //--------------------

    @Override
    public void onDestroy() {
        // 注销，反初始化通信库
        Log.i(MonitorActivityCompat.TAG,"--service exit"+ret_int);
        Log.w(TAG, "-- 注销退出 --");
//        int aa = CommunicationUtils.getInstance().logout1();
//        Log.i("CCC", "--logout:--" + aa);

        if(!WPApplication.DEBUG_NO_LOGIN && !WPApplication.flag_exit){  // TODO sometime OK, sometimes NOT???????
            // 登录的情况下
            Log.i("CCC", "---------IF?--------");
            int logout_ = CommunicationUtils.getInstance().logout();
            Log.i(TAG, "logout:" + logout_);
            Log.i("CCC", "logout:"+logout_);
        }
        int uninit_ = CommunicationUtils.getInstance().uninit();
        Log.i(TAG, "uninit:" + uninit_);

        // 释放电量锁
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
            mWakeLock = null;
        }
        stopForeground(true);

        // 清理
        WPApplication.getInstance().setAccessable(false);
        CCallbackMethod.getInstance(mContext).release();
        AlarmMsgManager.getInstance().release();
        WPApplication.getInstance().release();

        super.onDestroy();
        Log.w("ServiceExit", "-----------ServiceExit-----------");
    }

    /**
     * 创建一个前台服务通知。。
     */
    private Notification createNotice(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        RemoteViews rview = new RemoteViews(getPackageName(), R.layout.remote_layout_notification_1);
        Intent intent = new Intent(this, MainActivityCompat.class);
        intent.putExtra(MainActivityCompat.SERVICE_TAG, 0);
        PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, 0);

        builder.setContentTitle(getResources().getString(R.string.app_name))
                .setContent(rview)
                .setContentIntent(pintent)
                .setTicker("<--启动无人监控服务-->")
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.shield_blue);

        return builder.build();
    }

    /*------------------------------ EventBus ------------------------------ */
    /**
     * 处理错误事件
     * <br>一般是处理解析客户端软件回应的错误，弹出Toast说明
     */
    public void onEventMainThread(ErrorEvent event){
        switch(event.getType()){
            case ErrorEvent.ERROR_EVENT_TYPE_PARSE_SUB_PDU:
                Toast.makeText(this, event.description, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 后台服务事件
     */
    public void onEventMainThread(ServiceEvent event){
        Log.i(TAG, "get service event:" + event.getType());
        switch (event.getType()){
            // 初始化完成
            case ServiceEvent.SERVICE_EVENT_INIT_OVER:
                initialized = true;
                initError = false;
                boolean reload = event.msg == 0;
                CommunicationUtils.getInstance().refreshInitStationList(1, reload);
                break;
            // 收到初始化站场列表请求
            case ServiceEvent.SERVICE_EVENT_INIT_STATION_REQ:
                if(initialized){
                    Log.w(TAG, "send station event from service");
                    StationEvent m1 = new StationEvent(StationEvent.STATION_EVENT_INIT);
                    m1.msg = 1;
                    EventBus.getDefault().post(m1);
                }else if(initError){
                    EventBus.getDefault().post(new StationEvent(StationEvent.STATION_EVENT_EXCEPTION));
                }
                break;
            // 收到初始化消息列表请求
            case ServiceEvent.SERVICE_EVENT_INIT_ALARM_REQ:
                if(initialized){
                    Log.w(TAG, "send alarm event from service");
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.ALARM_FLAG_REFRESH));
                }
                break;
            // 请求创建新消息通知
            case ServiceEvent.SERVICE_EVENT_CREATE_NOTI:

                AlarmNode alarm = event.getAlarm();
                try{
                popOnlyNoti(alarm);
//                popNotification(alarm);    // TODO 使用新的通知弹出函数
                }
                catch(Exception e){
                    Log.e("AAA",Log.getStackTraceString(e));
                }
                break;
            case ServiceEvent.SERVICE_EVENT_CLEAR_NOTI:
                NotificationCancelManager.getInstance().clearAllNotification(this);
                break;
            // 初始化失败
            case ServiceEvent.SERVICE_EVENT_INIT_ERROR:
                Log.i("Communication","111----");
                initError = true;
                Toast.makeText(this, "初始化远程连接失败", Toast.LENGTH_SHORT).show();
                StationEvent event3= new StationEvent(StationEvent.STATION_EVENT_REFRESH);
                EventBus.getDefault().post(event3);
                break;
            case ServiceEvent.SERVICE_EVENT_INIT_BUSY:
                Toast.makeText(this, "服务器正忙,请稍后再试", Toast.LENGTH_SHORT).show();
                StationEvent event2= new StationEvent(StationEvent.STATION_EVENT_REFRESH);
                EventBus.getDefault().post(event2);
                break;
            default:
                break;
        }
    }

    /*------------------------------ ----------------------------- */
    private File notifSound;


    /**
     * 更新通知
     * 通知显示当前未处理alarm数目
     */
    private void refreshNotification(){

    }


    /**
     * 新的通知弹出函数，只有一条通知
     *
     * @param alarm
     */
    private void popOnlyNoti(AlarmNode alarm) throws Exception {
        String alarmMessage;
        int totalAlarms = WPApplication.getInstance().getNewAlarms();// get current unchecked/new alarms
        if ( null!=alarm) {  //新的alarm
            // 报警通知内容(status bar中)
            alarmMessage = getString(R.string.alarm_notification_info1)
                    + alarm.getDeviceName()
                    + getString(R.string.alarm_notification_info2);
        } else {  //删除alarm--刷新通知
            if (totalAlarms==0) {  //当前通知为0
                //--cancel notification
                NotificationManager mNotificationManager = (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancel(WPConstant.NOTIFICATION_ID);
                return;
            }
            else{
                alarmMessage = "共" + totalAlarms + "条未处理报警消息";
            }
        }

        int notificationID = WPConstant.NOTIFICATION_ID;

        // intent--> 发送broadcast 设置alarm notification status
        Intent mIntent1 = new Intent();//???
        mIntent1.setAction(WPConstant.ALARM_NOTIFICATION_POP);
        sendBroadcast(mIntent1);

        // pending intent broadcast --> 当notification被dismiss以后  设置alarm notification status
        Intent mIntent = new Intent();//???
//            Bundle bundle = new Bundle();
//            bundle.putInt(AlarmDetailsActivityCompat.ALARM_DETAIL_TEMP, 0);
//            bundle.putSerializable(AlarmDetailsActivityCompat.ALARM_DETAIL_DATA, alarm);
//            mIntent.putExtras(bundle);
        mIntent.setAction(WPConstant.ALARM_NOTIFICATION_DISMISS);
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(
                this, notificationID, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);  // flags代表啥？


        // send broadcast to start activity
        Intent mIntent2 = new Intent();//???
        Bundle bundle2 = new Bundle();
        // 保留字段
        bundle2.putInt(AlarmDetailsActivityCompat.ALARM_DETAIL_TEMP, 0);
        // 报警数据
        bundle2.putSerializable(AlarmDetailsActivityCompat.ALARM_DETAIL_DATA, alarm);
        mIntent2.putExtras(bundle2);
        // 由NotificationClickReciver处理点击事件
        mIntent2.setAction(WPConstant.ALARM_NOTIFICATION_CLICK_ACTION);
        PendingIntent startActivityPendingIntent = PendingIntent.getBroadcast(
                this, notificationID, mIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("共" + totalAlarms + "条未处理报警消息")
//                    .setContentTitle("新的警报")
//                    .setContentText("共" + totalAlarms + "条报警消息")
                .setTicker(alarmMessage) // status bar中
                .setWhen(System.currentTimeMillis())
                .setOngoing(false)
                .setOnlyAlertOnce(false)    //--------------是否仅仅 首次 alert
                .setAutoCancel(true)
                .setSound(Uri.fromFile(notifSound))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
//                    .setSmallIcon(R.drawable.ic_warning_black_48dp)
                .setSmallIcon(R.drawable.shield_blue)
//                .addAction(R.drawable.ic_search_black_36dp,
//                        getString(R.string.notif_str1), null)  // intent is needed
//                .addAction(R.drawable.ic_highlight_off_black_36dp,
//                        getString(R.string.notif_str2), null)
                .setDeleteIntent(mPendingIntent)
                .setContentIntent(startActivityPendingIntent)
        ;

        //use Inbox style big view
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("共" + totalAlarms + "条报警消息");

        // Moves events into the big view
        for (int i = 0; i < totalAlarms; i++) {
            inboxStyle.addLine("第" + (i + 1) + "条:" + WPApplication.getInstance().getAllAlarmList().get(i).getAlarmDate());
        }

        // Moves the big view style object into the notification object.
        builder.setStyle(inboxStyle);


        //
        NotificationManager mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        // 发出通知
        Notification notifi = builder.build();
        mNotificationManager.notify(notificationID, notifi);

    }


    /**
     * 弹出一条通知
     * ##TODO: 关于新报警的通知 有待整理
     * @param alarm 收到的新的警报消息结点
     */
    private void popNotification(AlarmNode alarm)throws Exception{
        if(alarm != null){
            if(WPApplication.DEBUG){
                Log.i(TAG, "----pop---pop---nofitification---");
            }
            int notificationID = alarm.getId();
            Log.i("alarmID",""+notificationID);

            Intent mIntent = new Intent();//???
            Bundle bundle = new Bundle();
            // 保留字段
            bundle.putInt(AlarmDetailsActivityCompat.ALARM_DETAIL_TEMP, 0);
            // 报警数据
            bundle.putSerializable(AlarmDetailsActivityCompat.ALARM_DETAIL_DATA, alarm);
            mIntent.putExtras(bundle);
            // 由NotificationClickReciver处理点击事件
            mIntent.setAction(WPConstant.ALARM_NOTIFICATION_CLICK_ACTION);

            PendingIntent mPendingIntent = PendingIntent.getBroadcast(
                    this, notificationID, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            if(alarm.getIsSolved() == (short)0){//未处理
                Log.i(TAG, "未处理消息");
            }

            // 报警通知内容
            String alarmMessage = getString(R.string.alarm_notification_info1)
                    + alarm.getDeviceName()
                    + getString(R.string.alarm_notification_info2);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setContentTitle(getResources().getString(R.string.app_name))
//                    .setContentTitle("新的警报")
                    .setContentText("-发现监控异常-")
                    .setContentIntent(mPendingIntent)
                    .setTicker(alarmMessage)
                    .setWhen(System.currentTimeMillis())
                    .setOngoing(false)
                    .setOnlyAlertOnce(true)
                    .setAutoCancel(true)
                    .setSound(Uri.fromFile(notifSound))
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                    .setSmallIcon(R.drawable.icon_shield_green);

            //
            NotificationManager mNotificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            // 保存报警通知ID
            NotificationCancelManager.getInstance().addNewNotification(
                    notificationID, NotificationCancelManager.TAG_ALARM);
            // 发出通知
            Notification notifi = builder.build();
            mNotificationManager.notify(notificationID, notifi);
            Log.w(TAG, "---notifi over---");
        }
    }

    //~~~
    private void initSoundFile(){
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = this.getAssets().open("light.mp3");
            notifSound = new File(WPConstant.SOUND_PATH  + "notification_sound_light");
            if (!notifSound.exists()) {
                try {
                    File soundDir = new File(WPConstant.SOUND_PATH);
                    soundDir.mkdirs();
                    notifSound.createNewFile();
                    fos = new FileOutputStream(notifSound);
                    byte[] data = new byte[1024];
                    int length = 0;
                    while ((length = is.read(data)) != -1) {
                        fos.write(data, 0, length);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null && fos != null){
                try {
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /* ------------------- --- --- --- test --- --- --- --- --------------------------------- */
    /* ------------------ - - - - - - - test - - - - - - - - -------------------------------- */
    /* ----------------- --- --- --- --- test --- --- --- --- -------------------------------- */
    private BaseDevice getD(int type){
        return DeviceFactory.getInstance().createDevice(type);
    }

    private void testData(){
        Log.i(TAG, "testData");
        //--1--
        StationNode n1 = new StationNode();
//        n1.setId(1);
        n1.setServerId("1");
        n1.setName("上海电信");
        n1.setRemark("fruit");
        n1.setState(SUConstant.FLAG_STATION_CLOSED);
        n1.setRecordSum(3);
        n1.setOnline(true);
//        Log.i("dataTest","上海电信 id="+n1.getId());

        BaseDevice d11 = getD(SUConstant.DEVICE_TYPE_MONITOR);
        d11.setName("摄像头1");
        d11.setIsAvailable(true);
        d11.setIsAlarming(true);
        d11.setStationId(n1.getId());

        BaseDevice d12 = getD(SUConstant.DEVICE_TYPE_RADAR);
        d12.setName("雷达1");
        d12.setIsAlarming(false);
        d12.setIsAvailable(true);
        d12.setStationId(n1.getId());

        BaseDevice d13 = getD(SUConstant.DEVICE_TYPE_ENTRANCE);
        d13.setName("门禁1");
        d13.setIsAlarming(false);
        d13.setIsAvailable(true);
        d13.setStationId(n1.getId());

        BaseDevice d14 = getD(SUConstant.DEVICE_TYPE_MONITOR);
        d14.setName("摄像头2");
        d14.setIsAlarming(false);
        d14.setIsAvailable(true);
        d14.setStationId(n1.getId());

        BaseDevice d15 = getD(SUConstant.DEVICE_TYPE_RADAR);
        d15.setName("雷达2");
        d15.setIsAlarming(false);
        d15.setIsAvailable(true);
        d15.setStationId(n1.getId());

        BaseDevice d16 = getD(SUConstant.DEVICE_TYPE_RADAR);
        d16.setName("雷达3");
        d16.setIsAlarming(false);
        d16.setIsAvailable(true);
        d16.setStationId(n1.getId());

        BaseDevice d17 = getD(SUConstant.DEVICE_TPYE_INFRARED);
        d17.setName("红外1");
        d17.setIsAlarming(false);
        d17.setIsAvailable(true);
        d17.setStationId(n1.getId());

        BaseDevice d18 = getD(SUConstant.DEVICE_TYPE_OTHER);
        d18.setName("其它");
        d18.setIsAlarming(false);
        d18.setIsAvailable(true);
        d18.setStationId(n1.getId());

        ArrayList<BaseDevice> device1 = new ArrayList<>();
        device1.add(d11);
        device1.add(d12);
        device1.add(d13);
        device1.add(d14);
        device1.add(d15);
        device1.add(d16);
        device1.add(d17);
        device1.add(d18);
        n1.setDeviceList(device1);

        AlarmNode i11 = new AlarmNode();
        i11.setIsSolved((short) 0);
        i11.setDeviceName("摄像头1");
        i11.setAlarmDate("2015-07-06 18:40:12");
        i11.setContent("ooo");
        i11.setCapture(true);
        i11.setPositionType(0);
        i11.setPositionSegmentMsg(1);
        i11.setStationId(n1.getId());

        AlarmNode i12 = new AlarmNode();
        i12.setIsSolved((short) 0);
        i12.setDeviceName("摄像头2");
        i12.setAlarmDate("2015-07-03 08:40:12");
        i12.setContent("aaa");
        i12.setCapture(true);
        i12.setPositionType(0);
        i12.setPositionSegmentMsg(2);
        i12.setStationId(n1.getId());

        AlarmNode i13 = new AlarmNode();
        i13.setIsSolved((short) 0);
        i13.setDeviceName("摄像头2");
        i13.setAlarmDate("2015-07-04 06:40:12");
        i13.setContent("aaa");
        i13.setCapture(false);
        i13.setPositionType(0);
        i13.setStationId(n1.getId());

        AlarmNode i14= new AlarmNode();
        i14.setIsSolved((short) 0);
        i14.setDeviceName("摄像头2");
        i14.setAlarmDate("2015-08-04 06:40:12");
        i14.setContent("a-a-a-");
        i14.setCapture(false);
        i14.setPositionType(0);
        i14.setStationId(n1.getId());

        AlarmNode i15= new AlarmNode();
        i15.setIsSolved((short) 0);
        i15.setDeviceName("摄像头2");
        i15.setAlarmDate("2015-09-04 06:40:12");
        i15.setContent("a-a-a-");
        i15.setCapture(false);
        i15.setPositionType(0);
        i15.setStationId(n1.getId());


        ArrayList<AlarmNode> a1 = new ArrayList<>();
        a1.add(i11);
        a1.add(i12);
        a1.add(i13);
        a1.add(i14);
        a1.add(i15);

        n1.setAlarmList(a1);
        n1.setNewAlarm(a1.size());

        //------more stations-----  to test UI collapsing layout
        StationNode n4 = new StationNode();
        n4.setServerId("4");
        n4.setName("上海3G");
        n4.setRemark("tree444");
        n4.setState(SUConstant.FLAG_STATION_ALARM);
        n4.setRecordSum(7);
        n4.setOnline(false);


        StationNode n5 = new StationNode();
        n5.setServerId("5");
        n5.setName("公司前台");
        n5.setRemark("tree55555");
        n5.setState(SUConstant.FLAG_STATION_ALARM);
        n5.setRecordSum(7);
        n5.setOnline(false);


        StationNode n6 = new StationNode();
        n6.setServerId("6");
        n6.setName("南鸿");
        n6.setRemark("tree66666");
        n6.setState(SUConstant.FLAG_STATION_ALARM);
        n6.setRecordSum(7);
        n6.setOnline(false);


        StationNode n7 = new StationNode();
        n7.setServerId("7");
        n7.setName("777777");
        n7.setRemark("tree77777");
        n7.setState(SUConstant.FLAG_STATION_ALARM);
        n7.setRecordSum(7);
        n7.setOnline(false);

        //------more stations-----


        //--2--
        StationNode n2 = new StationNode();
//        n2.setId(2);
        n2.setServerId("2");
        n2.setName("上海第二养老院");
        n2.setRemark("tree");
        n2.setState(SUConstant.FLAG_STATION_ALARM);
        n2.setRecordSum(7);
        n2.setOnline(false);

        BaseDevice d21 = getD(SUConstant.DEVICE_TYPE_MONITOR);
        d21.setID("002001");
        d21.setName("maple");
        d21.setIsAvailable(true);
        d21.setIsAlarming(false);
        d21.setStationId(n2.getId());

        BaseDevice d22 = getD(SUConstant.DEVICE_TYPE_ENTRANCE);
        d22.setName("ginkgo");
        d22.setIsAvailable(false);
        d22.setIsAlarming(false);
        d22.setStationId(n2.getId());

        ArrayList<BaseDevice> device2 = new ArrayList<>();
        device2.add(d21);
        device2.add(d22);
        n2.setDeviceList(device2);

        AlarmNode i21 = new AlarmNode();
        i21.setIsSolved((short) 0);
        i21.setDeviceName("maple");
        i21.setAlarmDate("2015-07-02 10:23:12");
        i21.setContent("mmm");
        i21.setCapture(false);
        i21.setPositionType(0);
        i21.setPositionSegmentMsg(8);
        i21.setStationId(n2.getId());

        AlarmNode i22 = new AlarmNode();
        i22.setIsSolved((short) 0);
        i22.setDeviceName("maple");
        i22.setAlarmDate("2015-07-02 10:12:12");
        i22.setContent("mmm");
        i22.setCapture(false);
        i22.setPositionType(0);
        i22.setStationId(n2.getId());

        AlarmNode i23 = new AlarmNode();
        i23.setIsSolved((short) 0);
        i23.setDeviceName("ginkgo");
        i23.setAlarmDate("2015-07-01 12:10:12");
        i23.setContent("ggg");
        i23.setCapture(true);
        i23.setPositionType(0);
        i23.setPositionSegmentMsg(7);
        i23.setStationId(n2.getId());

        ArrayList<AlarmNode> a2 = new ArrayList<>();
        a2.add(i21);
        a2.add(i22);
        a2.add(i23);

        n2.setAlarmList(a2);
        n2.setNewAlarm(a2.size());


        //--3--
        StationNode n3 = new StationNode();
//        n3.setId(3);
        n3.setServerId("3");
        n3.setName("石家庄井弪3G");
        n3.setRemark("Animal");
        n3.setState(SUConstant.FLAG_STATION_OPENED);
        n3.setRecordSum(0);
        n3.setOnline(true);
//        Log.i("dataTest", "石家庄井弪3G id=" + n3.getId());


        BaseDevice d31 = getD(SUConstant.DEVICE_TYPE_ENTRANCE);
        d31.setName("3G 门禁");
        d31.setIsAlarming(true);
        d31.setIsAvailable(true);
        d31.setStationId(n3.getId());

        BaseDevice d32 = getD(SUConstant.DEVICE_TPYE_INFRARED);
        d32.setName("3G 红外");
        d32.setIsAlarming(true);
        d32.setIsAvailable(true);
        d32.setStationId(n3.getId());

        ArrayList<BaseDevice> device3 = new ArrayList<>();
        device3.add(d31);
        device3.add(d32);
        n3.setDeviceList(device3);

        AlarmNode i31 = new AlarmNode();
        i31.setIsSolved((short) 0);
        i31.setDeviceName("3G 门禁");
        i31.setAlarmDate("2015-07-01 10:10:12");
        i31.setContent("fff");
        i31.setCapture(true);
        i31.setPositionType(0);
        i31.setPositionSegmentMsg(2);
        i31.setStationId(n3.getId());

        AlarmNode i32 = new AlarmNode();
        i32.setIsSolved((short) 0);
        i32.setDeviceName("3G 门禁");
        i32.setAlarmDate("2015-07-02 20:10:12");
        i32.setContent("fff");
        i32.setCapture(false);
        i32.setPositionType(0);
        i32.setPositionSegmentMsg(2);
        i32.setStationId(n3.getId());

        AlarmNode i33 = new AlarmNode();
        i33.setIsSolved((short) 0);
        i33.setDeviceName("3G 红外");
        i33.setAlarmDate("2015-07-07 13:10:12");
        i33.setContent("ccc");
        i33.setCapture(false);
        i33.setPositionType(0);
        i33.setPositionSegmentMsg(3);
        i33.setStationId(n3.getId());

        AlarmNode i34 = new AlarmNode();
        i34.setIsSolved((short) 0);
        i34.setDeviceName("3G 红外");
        i34.setAlarmDate("2015-07-10 23:10:12");
        i34.setContent("ccc");
        i34.setCapture(true);
        i34.setPositionType(0);
        i34.setPositionSegmentMsg(9);
        i34.setStationId(n3.getId());

        ArrayList<AlarmNode> a3 = new ArrayList<>();
        a3.add(i31);
        a3.add(i32);
        a3.add(i33);
        a3.add(i34);

        n3.setAlarmList(a3);
        n3.setNewAlarm(a3.size());

        ArrayList<StationNode> sn = new ArrayList<>();
        sn.add(n1);
        sn.add(n2);
        sn.add(n3);
        sn.add(n4); //???null station
        sn.add(n5); //???null station
        sn.add(n6); //???null station
        sn.add(n7); //???null station
        WPApplication.getInstance().setStationList(sn);

        int newSum = 0;
        for(StationNode n : sn){
            newSum += n.getNewAlarm();
        }
        WPApplication.getInstance().setNewAlarms(newSum);
        WPApplication.getInstance().rebuildAlarmList();
        WPApplication.getInstance().refreshReflectDevice();
        Log.i("AAA", "new alarm numbers  IN test DATA" + WPApplication.getInstance().getNewAlarms());

        new Thread(){
            @Override
            public void run(){
                Log.i(TAG, "run..");
                initialized = true;
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StationEvent event = new StationEvent(StationEvent.STATION_EVENT_INIT);
                event.msg = 1;
                EventBus.getDefault().post(event);
                Log.w("Service", "post refresh stations");
                AlarmMsgManager.getInstance().transmitCenter(AlarmMsgManager.ALARM_MANAGE_INIT, null);
                new TestNotification().start();
            }
        }.start();
        Log.i(TAG, "-----start test thread----");
    }

    private class TestNotification extends Thread{
        @Override
        public void run(){
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AlarmNode alarm = new AlarmNode();
            alarm.setIsSolved((short) 0);
            alarm.setDeviceName("3G-红外");
            alarm.setAlarmDate("2015-09-09 14:50:50");
            alarm.setContent("ccc");
            alarm.setCapture(true);
            alarm.setPositionType(0);
            alarm.setPositionSegmentMsg(1);
            alarm.setStationId("3".hashCode());

            List<AlarmNode> alarms = new ArrayList<>();
            alarms.add(alarm);
            AlarmMsgManager.getInstance().transmitCenter(AlarmMsgManager.ALARM_MANAGE_ADD, alarms);
            Log.i("AAA", "new alarm numbers--after4sec" + WPApplication.getInstance().getNewAlarms());
        }
    }

}
