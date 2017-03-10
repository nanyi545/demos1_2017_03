package com.webcon.wp.utils;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.webcon.sus.activities.MonitorActivityCompat;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.entity.BaseDevice;
import com.webcon.sus.entity.StationNode;
import com.webcon.sus.entity.UserNode;
import com.webcon.sus.handler.CrashHandler;
import com.webcon.sus.utils.SUConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 应用
 * <p>应用的全局变量的处理</p>
 *
 * @author m
 */
public class WPApplication extends Application {
    /**
     * 测试标记
     */
    public static final boolean DEBUG = false;
    public static final boolean DEBUG_NO_LOGIN = true && DEBUG;
    public static final boolean DEBUG_NO_RSP = true && DEBUG;
    public static final boolean DEBUG_NO_PTZ = true && DEBUG;
    public static final boolean DEBUG_NO_ALARM = true && DEBUG;
    public static final boolean DEBUG_NO_MONITOR_LOADING = true && DEBUG;
    public static final boolean DEFENSE_NOT_READY = false;   // 布防设置功能--->
    //
    private static WPApplication wpApplication;
    // 当前网络是否为移动网络
    public static boolean phoneNet;
    // 返回按键重写标识
    private int keyBackFlag;// 0为主界面退出行为，1为用户列表后退行为，2为报警列表退出删除行为
    // 当前退出动作是否为注销
    public static boolean flag_logout = false;
    // 是否被动退出，是则不再注销，防止意外
    public static boolean flag_exit = false;
    // 当前程序的Uid
    private int appUid;

    public Bundle monitorBundle_oncreate;

    /*  -------------- New ------------------ */
    /**
     * 报警序号种子 --- 已弃用
     */
    public int alarmSeqSeed = 0;
    /**
     * 注：这不是一个好的办法，暂时用来取代{@link PublicMethodUtil#getRunningProcess}进行标记
     */
    public int appFlag = SUConstant.APP_NOT_EXIST;
    /**
     * 后台服务启动标识
     */
    public boolean serviceExist = false;
    /**
     * 退出时，不再提供全局对象的调用
     */
    private boolean dataAccessable = false;
    /**
     * 用户结点
     */
    private UserNode user = new UserNode();
    /**
     * 全部站场列表
     */
    private List<StationNode> mStationList;
    /**
     * 全部报警消息列表
     */
    private List<AlarmNode> mAlarmList;
    /**
     * 全部报警消息数量
     */
    private int mNewAlarms = 0;
    /**
     * 映射表(stationId, pos)         用于将StationId转换为Station列表的下标
     */
    private ArrayMap<Integer, Integer> mStationReflect;
    /**
     * 映射表(deviceName, staitonId)  用于将设备名称转换为StationId
     */
    private ArrayMap<String, Integer> mDeviceReflect;


    /**
     * alarm notification status
     */
    public int notificationStatus = WPConstant.ALARM_NOTIFICATION_OFF;

    /* ===========================================================  */
    public static WPApplication getInstance() {
        return wpApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = OsUtils.getProcessName(this, android.os.Process.myPid());
        Log.i("AAA", "application on create" + processName + "   process ID:" + android.os.Process.myPid());
//        String processName = OsUtils.getProcessName(this, android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = processName.equals(WPConstant.PACKAGE_NAME);
            if (defaultProcess) {
                single_init();
            }
        }
    }

    private void single_init() {
        //TODO to turn on the crash handler
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        //

        wpApplication = this;
        appFlag = SUConstant.APP_EXIT_MAIN;
        mStationReflect = new ArrayMap<>();
        mDeviceReflect = new ArrayMap<>();
        mAlarmList = new ArrayList<>();
    }

    /**
     * 设置全部站场列表
     */
    public void setStationList(List<StationNode> list) {
        if (this.mStationList != null) {
            this.mStationList.clear();
        }
        if (list != null) {
            this.mStationList = list;
            refreshReflectMap();
        }
    }

    /**
     * 获取全部站场列表
     * <p>如果要根据stationId获取指定节点，请调用{@link WPApplication#getStationNode(int)}获取</p>
     */
    public List<StationNode> getStationList() {
        refreshStationList();
        return mStationList;
    }


    /**
     * 根据站场Id，获取stationNode结点
     *
     * @param stationId 站场id
     */
    public StationNode getStationNode(int stationId) {
        if ((mStationReflect != null) && (mStationList != null) && dataAccessable && mStationReflect.containsKey(stationId) && (mStationReflect.get(stationId) < mStationList.size())) {
            return mStationList.get(mStationReflect.get(stationId));
        } else {
            return null;
        }
    }


    // for debug
    public synchronized void printAlarmStatus(int id) {
        String a = "" + id + ":";
        String b = "" + id + ":";
        for (AlarmNode aNode : mAlarmList) {
            a = a + aNode.getIsSolved()+"-";
            b = b + aNode.getId()+"-";
        }
        Log.i("alarmRV", a);
        Log.i("alarmRV", b);
    }


    /**
     * 根据本地站场标识，获取战场服务器id
     *
     * @param stationId 本地站场标识
     * @return serverId
     */
    public String getServerId(int stationId) {
        if (mStationReflect != null && mStationList != null) {
            String aa=mStationList.get(mStationReflect.get(stationId)).getServerId();
            Log.i(MonitorActivityCompat.TAG,"stationId:"+stationId+"  serverId:"+aa);
            return aa;
        } else {
            return null;
        }
    }

    /**
     * 刷新站场列表，重排序
     */
    public void refreshStationList() {
        if (mStationList != null) {
            Collections.sort((ArrayList) mStationList);
            refreshReflectMap();
        }
    }

    /*
     * 说明：
     * 全局消息列表的操作暂定如下：
     * 1.设置列表：对报警消息的任何操作，都在子Station中处理，然后重新组合成全部列表
     * 2.处理消息包括：添加新消息、设置消息数量、减少新消息
     */

    /**
     * 刷新报警消息列表，重排序
     */
    public void refreshAlarmList() {
        synchronized (WPApplication.getInstance()) {
            if (mAlarmList != null & mAlarmList.size() > 0) {
                Collections.sort((ArrayList) mAlarmList);
            }
        }
    }

    /**
     * 从场站消息队列  重建全局消息列表
     */
    public void rebuildAlarmList() {
//        Log.i(MonitorActivityCompat.TAG,"mAlarmList!=null:"+(mAlarmList != null)+"--mAlarmList.size():"+
//                mAlarmList.size()+"-mStationList != null:"+(mStationList != null)+"-mStationList.size():"+(mStationList.size()));
//        if((mAlarmList != null)&&(mAlarmList.size()>0) && (mStationList != null)&&(mStationList.size()>0)){
        synchronized (WPApplication.getInstance()) {
            if (mAlarmList != null && mStationList != null) {
                mAlarmList.clear();
                mNewAlarms = 0;
                for (StationNode node : mStationList) {
                    if (node.isOnline() && node.getAlarmList() != null) {    // 只添加在线的站场的消息列表
                        mAlarmList.addAll(node.getAlarmList());
                        mNewAlarms += node.getNewAlarm();
                    }
                }
                refreshAlarmList();
            }
        }
    }

    /**
     * 获取全部消息列表
     */
    public List<AlarmNode> getAllAlarmList() {
        refreshAlarmList();
        return mAlarmList;
    }

    // -----
    public AlarmNode getSameNode(AlarmNode target){
        Iterator<AlarmNode> alarmIter=mAlarmList.iterator();
        while(alarmIter.hasNext()){
            AlarmNode tempNode=alarmIter.next();
            if(tempNode.isSameAlarm(target))
            return tempNode;
        }
        return null;
    }


    /**
     * 获取全部消息列表-no refresh
     */
    public List<AlarmNode> getAllAlarmList_norefresh() {
        return mAlarmList;
    }


    /**
     * 获取全部新消息的数量
     */
    public int getNewAlarms() {
        return mNewAlarms;
    }

    /**
     * 设置全部新消息的数量 ☆重要
     */
    public void setNewAlarms(int newAlarms) {
        this.mNewAlarms = newAlarms;
    }

    /**
     * 减少一条新消息的数量
     */
    public void substractNewAlarm() {
        if (mNewAlarms <= 0) {
            refreshAlarmList();
            return;
        }
        mNewAlarms--;
        refreshAlarmList();
    }

    /**
     * 增加一条新消息的数量
     */
    public void addNewAlarm() {
        mNewAlarms++;
    }

    /**
     * 刷新站场id和站场pos的映射关系
     */
    private void refreshReflectMap() {
        if (mStationReflect != null) {
            mStationReflect.clear();
            int i = 0;
            for (StationNode n : mStationList) {
                mStationReflect.put(n.getId(), i++);
            }
        }
    }

    /**
     * 刷新设备名和站场id的映射
     */
    public void refreshReflectDevice() {
        if (mStationList != null) {
            mDeviceReflect.clear();
            for (StationNode n : mStationList) {
                for (BaseDevice d : n.getDeviceList()) {
                    mDeviceReflect.put(d.getName(), n.getId());
                }
            }
        }
    }

    /**
     * 增加一条设备名和站场id的映射
     *
     * @param device    设备名
     * @param stationId 站场id
     */
    public void addDeviceReflect(String device, int stationId) {
        if (mDeviceReflect != null) {
            mDeviceReflect.put(device, stationId);
        }
    }


    /**
     * 根据设备名，获取所属stationNode结点
     *
     * @param deviceName 设备名
     */
    public StationNode getStationNode(String deviceName) {
        if (mDeviceReflect != null && mDeviceReflect.get(deviceName) != null) {
            return getStationNode(mDeviceReflect.get(deviceName));
        } else {
            return null;
        }
    }

    private StationNode getStationNodeByServerID(String stationId) {
        StationNode mStationNode=null;
        if (mStationList!=null&mStationList.size()>0){
            Iterator<StationNode> stationIterator=mStationList.iterator();
            while (stationIterator.hasNext()){
                StationNode node=stationIterator.next();
                if (node.getServerId().equals(stationId))  {
                    mStationNode=node;
                    break;
                }
            }
        }
        return mStationNode;
    }

    // set a particular alarm(specified by int alarmid) under a station(specified by String statoinId) to be solved
    // and auto refresh...
    public void setAlarmSolved(String stationId,int AlarmId, String solver,String solveTime){
        StationNode node=getStationNodeByServerID(stationId);
        node.findAlarmAndSolved(AlarmId,solver,solveTime);
        correctNewAlarms();
        rebuildAlarmList();
    }


    /**
     * 根据设备名，获取所属的stationNode的Id
     *
     * @param deviceName 设备名
     */
    public int getStationId(String deviceName) {
        if (mDeviceReflect != null && mDeviceReflect.size() > 0 && mDeviceReflect.containsKey(deviceName)) {
            return mDeviceReflect.get(deviceName);
        } else {
            return -1;
        }
    }

    public void setAccessable(boolean accessable) {
        this.dataAccessable = accessable;
    }

    public boolean getAccessable() {
        return dataAccessable;
    }

    //----for test-----

    /**
     * 检查新的报警消息
     */
    public boolean checkNewAlarms() {
        int sum = 0;
        for (StationNode sn : mStationList) {
            sum += sn.getNewAlarm();
        }
        return mNewAlarms == sum;
    }

    /**
     * 刷新数据，刷新每个场站新消息数目，以及总的新消息数目
     */
    public int correctNewAlarms() {
        int mNewAlarms = 0;
        for (StationNode sn : mStationList) {
            mNewAlarms += sn.correctNewAlarms();
        }
        this.mNewAlarms=mNewAlarms;
        return mNewAlarms;
    }

    /* -------------------------------- */

    public int getKeyBackFlag() {
        return keyBackFlag;
    }

    public void setKeyBackFlag(int keyBackFlag) {
        if (keyBackFlag == 0) {
            this.keyBackFlag = keyBackFlag;
        } else {
            this.keyBackFlag |= keyBackFlag;
        }

    }

    public int getAppUid() {
        return appUid;
    }

    public void setAppUid(int appUid) {
        this.appUid = appUid;
    }


    private AlarmNode node;

    /**
     * 清空alarm列表
     * 清空每个station的alarm然后rebuild。。。
     */
    public void clearAlarmList() {
//        mAlarmList.clear();  //TODO why this doesn't work
        for (StationNode node : mStationList) {  //清空每个station的alarm
            if (node.isOnline() && node.getAlarmList() != null) {    // 只添加在线的站场的消息列表
                node.clearAlarmList();
            }
        }
        correctNewAlarms();  // 重新计算 各个厂站未处理消息数目，以及总的未处理消息数目
        rebuildAlarmList();  // 然后rebuild。。。
    }

    /**
     * 退出清理工作
     */
    public void release() {
        mNewAlarms = 0;
        serviceExist = false;
        if (mAlarmList != null) {
            mAlarmList.clear();
        }
        if (mStationReflect != null) {
            mStationReflect.clear();
        }
        if (mDeviceReflect != null) {
            mDeviceReflect.clear();
        }
        if (mStationList != null) {
            mStationList.clear();
        }
    }

    /**
     * 获取当前登录用户信息结点
     */
    public UserNode getCurrentUser() {
        return this.user;
    }


}
