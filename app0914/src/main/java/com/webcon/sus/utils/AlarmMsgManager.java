package com.webcon.sus.utils;

import android.util.Log;

import com.webcon.sus.activities.MonitorActivityCompat;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.entity.StationNode;
import com.webcon.sus.eventObjects.MessageEvent;
import com.webcon.sus.eventObjects.ServiceEvent;
import com.webcon.wp.utils.WPApplication;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

/**
 * 消息处理中心
 * <p>
 * 首先只有一个入口，以及一个单线程池，<br>
 * 然后，所有的消息要经过这里，再转发处理结果
 * </p>
 * @author m
 */
public class AlarmMsgManager {
    public static final String TAG = "AlarmMsgManager";
    public static final int ALARM_MANAGE_SOLVE_REQUEST  = 1;
    public static final int ALARM_MANAGE_SOLVE_RECEIVE  = 2;
    public static final int ALARM_MANAGE_ADD            = 3;
    public static final int ALARM_MANAGE_INIT           = 4;
    public static final int ALARM_MANAGE_DELETE         = 5;
    public static final int ALARM_MANAGE_CLEAR          = 6;
    public static final int ALARM_MANAGE_CLEAR_STATION  = 7;



    public static final int ALARM_RET_SOLVE_ERROR_CONNECT   = -1;
    public static final int ALARM_RET_SOLVE_ERROR_SOLVED    = -2;
    private boolean DEBUG = WPApplication.DEBUG;

    private static AlarmMsgManager me;
    //线程池
    private ExecutorService pool;

    private AlarmMsgManager(){
    }

    public static AlarmMsgManager getInstance(){
        if(me == null){
            synchronized (AlarmMsgManager.class){
                if(me == null){
                    me = new AlarmMsgManager();
                }
            }
        }
        return me;
    }

    public void release(){
        if(pool != null){
            pool.shutdown();
            pool = null;
        }
    }

    //------------------------------------
    /**
     * ★转发消息通知中心★
     * <p>所有的消息都要经过这里，并且消息是未处理过的</p>
     * <p>用于转发处理结果、以及发送通知</p>
     * 1.初始化消息<br>
     * 2.新消息<br>
     * 3.发送通知<br>
     * 4.修改表状态<br>
     * 5.#暂不经过本地数据库<br>
     */
    public void transmitCenter(int type, List<AlarmNode> alarms){  // 单线程 线程池， 删除/添加 alarm--> 线程安全
        if(WPApplication.getInstance().getAccessable()){
            if(pool == null){
                if(WPApplication.DEBUG){
                    Log.w(TAG, "create single thread Executor");
                }
                pool = Executors.newSingleThreadExecutor();
            }
            pool.execute(new MyRunn(type, alarms));
        }
    }

    /**
     * 消息处理线程
     */
    private class MyRunn implements Runnable{
        private int type;
        private List<AlarmNode> alarms;

        public MyRunn(int type, List<AlarmNode> alarms){
            this.type = type;
            this.alarms = alarms;
        }

        @Override
        public void run(){
            if(DEBUG){
                Log.w(TAG, "run --process alarm event---");
                Log.i("FAB", "run --process alarm event---" + type);
                Log.i("FAB", "current Thread:" + Thread.currentThread().getName());
            }
            switch(type){
                // 初始化报警消息
                case ALARM_MANAGE_INIT:
                    if(DEBUG){
                        Log.w(TAG, "init alarms");
                    }
                    Log.i(MonitorActivityCompat.TAG,"alarmMessage manager--init");
                    MessageEvent initMsg = new MessageEvent(MessageEvent.ALARM_FLAG_INIT);
                    initMsg.reload = true;
                    EventBus.getDefault().post(initMsg); // 有毛用？？没有接收此事件的
                    break;
                // 报警消息 -处理请求
                case ALARM_MANAGE_SOLVE_REQUEST:

//                    Log.i("alarmRV", "ALARM_MANAGE_SOLVE_REQUEST:" + alarms.get(0).getAlarmDate() + "---" + alarms.get(0).getIsSolved());
//                    WPApplication.getInstance().printAlarmStatus(41);// show the status of the alarms
                    Log.i(MonitorActivityCompat.TAG, "ALARM_MANAGE_SOLVE_REQUEST:" + alarms.get(0).getAlarmDate() + "---" + alarms.get(0).getIsSolved());

                    if(DEBUG){
                        Log.w(TAG, "process alarm");
                    }
                    if(alarms == null || alarms.size() <= 0){
                        Log.e(TAG, "报警消息请求错误-1");
                        return;
                    }
                    // 发送处理请求，返回解析结果
                    boolean reqRet = CommunicationUtils.getInstance().requestSolveAlarm(alarms.get(0));
                    Log.i(MonitorActivityCompat.TAG, "ALARM_MANAGE_SOLVE_REQUEST:-->ret" + reqRet);

                    // Log.i(TAG, "here--reqRet:" + reqRet+"  alarms0==null:"+(alarms.get(0)==null));
//                    WPApplication.getInstance().printAlarmStatus(42);// show the status of the alarms

                    AlarmNode node = alarms.get(0);
                    if(node == null){
                        Log.e(TAG, "空报警消息");
                        return;
                    }
                    int stationId = node.getStationId();

                    if(reqRet){
                        WPApplication.getInstance().getStationNode(stationId).solvedNewAlarm(node);//本地设置为已处理
//                        node.setIsSolved((short)1);//本地设置为已处理??//---sometimes work..sometime doesn't...??? why
                    }


//                    WPApplication.getInstance().printAlarmStatus(43);// show the status of the alarms

                    // 发送本地通知
                    MessageEvent eventRefresh = new MessageEvent(MessageEvent.ALARM_FLAG_REFRESH);
                    eventRefresh.stationId = stationId;
                    eventRefresh.alarmId = node.getId();
                    eventRefresh.success = reqRet;
                    EventBus.getDefault().post(eventRefresh);
//                    WPApplication.getInstance().printAlarmStatus(44);// show the status of the alarms

                    break;
                // 报警消息 -接收的处理通知
                case ALARM_MANAGE_SOLVE_RECEIVE:
                    if(DEBUG){
                        Log.w(TAG, "receive solved alarm notify");
                    }
                    if(alarms == null || alarms.size() <= 0){
                        Log.e(TAG, "空消息错误-1");
                        return;
                    }
                    AlarmNode node2 = alarms.get(0);
                    if(node2 == null){
                        Log.e(TAG, "空消息错误-2");
                        return;
                    }
                    int stationId2 = node2.getStationId();
                    if(stationId2 == -1){
                        Log.e(TAG, "找不到对应站场信息");
                        return;
                    }
                    WPApplication.getInstance().getStationNode(stationId2).solvedNewAlarm(node2);

                    // 发送本地通知
                    MessageEvent eventRefresh2 = new MessageEvent(MessageEvent.ALARM_FLAG_REFRESH);
                    eventRefresh2.stationId = stationId2;
                    eventRefresh2.alarmId = node2.getId();
                    EventBus.getDefault().post(eventRefresh2);
                    break;
                // 收到新的报警消息
                case ALARM_MANAGE_ADD:
                    if(DEBUG){
                        Log.w(TAG, "receive a new alarm");
                    }
                    if(alarms == null || alarms.size() <= 0){
                        return;
                    }
                    AlarmNode alarm = alarms.get(0);
                    int stationId3 = alarm.getStationId();
                    if(WPApplication.getInstance().getAccessable()){
                        StationNode n1=WPApplication.getInstance().getStationNode(stationId3);
                        Log.i(MonitorActivityCompat.TAG,"n1 null?"+(n1!=null));
                        if (n1!=null){
                            synchronized (WPApplication.getInstance()) {
                                n1.addNewAlarm(alarm);
                                WPApplication.getInstance().rebuildAlarmList();
                            }
                        }
                    }else{
                        return;
                    }

                    // 通知处理---发送notification的
                    ServiceEvent eventNotify = new ServiceEvent(ServiceEvent.SERVICE_EVENT_CREATE_NOTI);
                    eventNotify.setAlarm(alarm);
                    EventBus.getDefault().post(eventNotify);
                    // 发送刷新
                    MessageEvent eventRefresh3 = new MessageEvent(MessageEvent.ALARM_FLAG_REFRESH);
                    eventRefresh3.stationId = stationId3;
                    eventRefresh3.alarmId = alarm.getId();
                    EventBus.getDefault().post(eventRefresh3);
                    break;
                // 删除报警消息
                case ALARM_MANAGE_DELETE:
//                    Log.i("alarm--","longclick--delete");
//                    Log.i("swipe","swipe--delete");
                    if(alarms == null || alarms.size() < 0){
                        return;
                    }
                    AlarmNode alarm1 = alarms.get(0);
                    int stationId4 = alarm1.getStationId();
//                    Log.i("dataTest","delete station id="+stationId4+"   alarm time"+alarm1.getAlarmDate());

                    //删除场站下alarm 其中removeAlarm会调用rebuildAlarmList()
                    if(WPApplication.getInstance().getAccessable()){
                        WPApplication.getInstance().getStationNode(stationId4).removeAlarm(alarm1);
                    }else{
                        return;
                    }
                    // 通知处理---更新notification的
                    ServiceEvent eventNotify2 = new ServiceEvent(ServiceEvent.SERVICE_EVENT_CREATE_NOTI);
                    eventNotify2.setAlarm(null);
                    EventBus.getDefault().post(eventNotify2);

//                    // 发送刷新
//                    MessageEvent eventRefresh4 = new MessageEvent(MessageEvent.ALARM_FLAG_REFRESH);
//                    eventRefresh4.stationId = stationId4;
//                    eventRefresh4.alarmId = alarm1.getId();
//                    EventBus.getDefault().post(eventRefresh4);
                    // 发送刷新
                    MessageEvent eventRefresh4 = new MessageEvent(MessageEvent.ALARM_SWIPE_DELETED);
                    eventRefresh4.stationId = stationId4;
                    eventRefresh4.alarmId = alarm1.getId();
                    EventBus.getDefault().post(eventRefresh4);
                    break;
                case ALARM_MANAGE_CLEAR:
                    Log.i("FAB", "transmit center:");
                    //删除场站下alarm 其中removeAlarm会调用rebuildAlarmList()
                    if(WPApplication.getInstance().getAccessable()){
                        WPApplication.getInstance().clearAlarmList();
                    }else{
                        return;
                    }
                    // 通知处理---更新notification的
                    ServiceEvent eventNotify3 = new ServiceEvent(ServiceEvent.SERVICE_EVENT_CREATE_NOTI);
                    eventNotify3.setAlarm(null);
                    EventBus.getDefault().post(eventNotify3);

                    // 发送刷新UI
                    MessageEvent eventRefresh5 = new MessageEvent(MessageEvent.ALARM_FLAG_REFRESH);  //发送任何messageEvent在主界面都会刷新
                    EventBus.getDefault().post(eventRefresh5);
                    break;

                case ALARM_MANAGE_CLEAR_STATION:
//                    Log.i("alarm--","longclick--delete");
//                    Log.i("swipe","swipe--delete");
                    if(alarms == null || alarms.size() < 0){
                        return;
                    }
                    AlarmNode alarm6 = alarms.get(0);
                    int stationId6 = alarm6.getStationId();
//                    Log.i("dataTest","delete station id="+stationId4+"   alarm time"+alarm1.getAlarmDate());

                    //删除场站下所有 alarm
                    if(WPApplication.getInstance().getAccessable()){
                        WPApplication.getInstance().getStationNode(stationId6).clearAlarmList();
                        WPApplication.getInstance().correctNewAlarms();
                        WPApplication.getInstance().rebuildAlarmList();
                    }else{
                        return;
                    }
                    // 通知处理---更新notification的
                    ServiceEvent eventNotify6 = new ServiceEvent(ServiceEvent.SERVICE_EVENT_CREATE_NOTI);
                    eventNotify6.setAlarm(null);
                    EventBus.getDefault().post(eventNotify6);

                    // 发送刷新UI
                    MessageEvent eventRefresh6 = new MessageEvent(MessageEvent.ALARM_FLAG_REFRESH);  //发送任何messageEvent在主界面都会刷新
                    eventRefresh6.stationId=stationId6;
                    EventBus.getDefault().post(eventRefresh6);
                    Log.i("CLEARSTATION","send MessageEvent");
                    break;


                default:
                    break;
            }
        }
    }

}
