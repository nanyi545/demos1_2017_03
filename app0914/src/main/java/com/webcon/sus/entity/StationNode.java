package com.webcon.sus.entity;


import android.support.annotation.NonNull;
import android.util.Log;

import com.webcon.sus.utils.CommunicationUtils;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.WPApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 站场结点
 * <p>包括包含设备列表和该站场报警消息列表等数据</p>
 *
 * @author m
 */
public class StationNode implements Comparable<StationNode> {
    private static final String TAG = "StationNode";
    /**
     * 设备列表
     */
    private List<BaseDevice> mDevices = new ArrayList<>();
    /**
     * 报警消息列表
     */
    private List<AlarmNode> mAlarms = new ArrayList<>();

    /**
     * 站场服务器Id
     */
    private String serverId = "";
    /**
     * 本地标识id
     */
    private int mId = 0;
    /**
     * 序列号 与属性组织结构有关
     */
    private int seq = 0;
    /**
     * 站场名称
     */
    private String mName = "";
    /**
     * 备注消息(保留)
     */
    private String mRemark = "";
    /**
     * 是否在线
     */
    private boolean isOnline = false;
    /**
     * 新报警消息数量
     */
    private int mNewAlarm = 0;
    /**
     * 站场状态：开启、关闭、有新报警
     */
    private int mState = SUConstant.FLAG_STATION_CLOSED;
    /**
     * 报警录音数量
     */
    private int mRecordSum = 0;
    /**
     * 上一个站场在线状态，主要判别站场状态是否变更，以及是否需要重新请求相应数据
     */
    private boolean preOnlineState = false;

    /**
     * 是否已注册
     */
    private boolean isRegister = false;

    /* -------------------------------------------- */

    public List<BaseDevice> getDeviceList() {
        if (mDevices == null) {
            Log.e("StationNode", "devices list is null");
//            throw new NullPointerException();
        }
        return mDevices;
    }

    public void setDeviceList(List<BaseDevice> list) {
        this.mDevices = list;
        if (mDevices != null) {
            Collections.sort((ArrayList) mDevices);
        }
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        mId = serverId.hashCode();
        this.serverId = serverId;
    }

    public int getId() {
        return this.mId;
    }

    public int getState() {
        return mState;
    }

    public void setState(int mState) {
        if (mState == SUConstant.FLAG_STATION_OPENED && mNewAlarm != 0) {
            mState = SUConstant.FLAG_STATION_ALARM;
        }
        this.mState = mState;
    }


    public boolean isRegister() {
        return isRegister;
    }

    public void setIsRegister(boolean isRegister) {
        this.isRegister = isRegister;
    }


    //--------------------
    /*
     * 报警消息列表的操作:
     * 1.添加、删除新消息
     * 2.处理新消息
     */

    public List<AlarmNode> getAlarmList() {
        refreshAlarmList();
        return mAlarms;
    }

    public void setAlarmList(List<AlarmNode> list) {
        this.mAlarms = list;
    }

    public int getNewAlarm() {
        return mNewAlarm;
    }

    public void setNewAlarm(int newAlarm) {
        mNewAlarm = newAlarm;
        if (mState != SUConstant.FLAG_STATION_CLOSED) {
            if (newAlarm > 0) {
                mState = SUConstant.FLAG_STATION_ALARM;
            } else {
                mState = SUConstant.FLAG_STATION_OPENED;
            }
        }
    }

    /**
     * 处理一条新消息：包括当前消息列表和全局列表的处理
     * NOTE: 总的来说，alarm结点在程序中都是使用直接用引用作为参数传递，这样与列表中保存的对象都是指向同一个地址，
     * 那么在程序的其他地方就可以直接修改alarm结点数据，所以该方法看起来是多余的处理步骤，
     * 但是实际上是不规范的操作导致逻辑失效的，本来是设计成只有一个修改入口的，
     * 综上，如果要遵循之前的逻辑，那么用于传递的结点对象应该是需要clone出来的，需要修改
     */
    public void solvedNewAlarm(AlarmNode alarm) {
        //FIXME:需要重新处理。。。
        if (alarm != null) {//修改和删除操作
//            WPApplication.getInstance().printAlarmStatus(330);// show the status of the alarms

//            // 处理站场结点下的消息列表
//            Log.i("alarmRV","station alarms"+mAlarms.size());
//            if(!findAlarmArrayAndSolved(mAlarms, alarm)){
//                Log.e(TAG, "not found item --> stationList");
//                throw new NullPointerException();
//            }

//            WPApplication.getInstance().printAlarmStatus(331);// show the status of the alarms
            // 处理全部消息列表
            if (!findAlarmArrayAndSolved(alarm)) {
                Log.e(TAG, "not found item --> allList");
                throw new NullPointerException();
            }
//            WPApplication.getInstance().printAlarmStatus(332);// show the status of the alarms
        }
        // 刷新队列
        WPApplication.getInstance().rebuildAlarmList();
    }

    /**
     * 添加一条报警消息
     */
    public void addNewAlarm(AlarmNode alarm) {
        if (mAlarms == null) {
            mAlarms = new ArrayList<>();
        }

        // 如果alarm 不在list中, 添加alarm
        // 如果alarm 在list中, findAlarmArrayAndSolved() method updates the alarm's solve state:
        if (!findAlarmArrayAndSolved2(mAlarms, alarm)) {
            mAlarms.add(alarm);
            setNewAlarm(mNewAlarm + 1);
        }
        //NOTE:除初始化之外，其他的报警消息操作都在指定的Station中的列表进行，然后自动构建全局列表
        refreshAlarmList();

//        WPApplication.getInstance().addNewAlarm();
        //NOTE:与通知分离，之后要注意发送事件通知
    }

    /**
     * 删除一条报警消息
     * <p>注：未实现相关功能</p>
     */
    public void removeAlarm(AlarmNode alarm) {
        //TODO: 关于能否匹配的问题？
        mAlarms.remove(alarm);
        if (alarm.getIsSolved() == 0) {
            // 减少未处理消息
            solvedNewAlarm(null);
        }
        refreshAlarmList();

        WPApplication.getInstance().rebuildAlarmList();
//        WPApplication.getInstance().substractNewAlarm();
    }

    public void refreshAlarmList() {
        if ((mAlarms != null) & (mAlarms.size() > 0)) Collections.sort(mAlarms);// TODO  enable this
        correctNewAlarms();
    }

    /**
     * 核查未处理报警消息
     */
    public int correctNewAlarms() {
        int mNewAlarm = 0;
        for (AlarmNode an : mAlarms) {
            if (an.getIsSolved() == 0) {
                mNewAlarm++;
            }
        }
        this.mNewAlarm=mNewAlarm;
        return mNewAlarm;
    }

    //清空场站下alarmList
    public void clearAlarmList() {
        mAlarms.clear();
    }

    /**
     * 获取结点
     */
    public AlarmNode getAlarmNode(int id) {
        if (mAlarms.size() <= 0) {
            return null;
        } else {
            for (AlarmNode node : mAlarms) {
                if (node.getId() == id) {
                    return node;
                }
            }
        }
        return null;
    }
    //------------

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getRecordSum() {
        return mRecordSum;
    }

    public void setRecordSum(int mRecordSum) {
        this.mRecordSum = mRecordSum;
    }

    public String getRemark() {
        return mRemark;
    }

    public void setRemark(String mRemark) {
        this.mRemark = mRemark;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isAlive) {
        preOnlineState = isOnline;
        this.isOnline = isAlive;
    }

    // 请放到线程中执行
    public void refreshInformation() {
        if (isNeedRefresh()) {
            CommunicationUtils.getInstance().requestRefreshStationInformation(mId);
            updateOnlineState();
        }
    }



    private boolean isNeedRefresh() {
        return isOnline && !preOnlineState;
    }

    private void updateOnlineState() {
        preOnlineState = isOnline;
    }

    public boolean isDefend() {
        return mState == SUConstant.FLAG_STATION_OPENED || mState == SUConstant.FLAG_STATION_ALARM;
    }


    private boolean findAlarmArrayAndSolved(AlarmNode alarm) {
        boolean ret = false;
//        for (AlarmNode n : list) {
//            if (n.isSameAlarm(alarm)) {
//                n.setIsSolved((short) 1);
//                n.setSolver(alarm.getSolver());
//                n.setSolvedDate(alarm.getSolvedDate());
//                ret = true;
//                break;
//            }
//        }
        Iterator<AlarmNode> alarmNodeIterator=mAlarms.iterator();
        while (alarmNodeIterator.hasNext()){
            AlarmNode tempNode=alarmNodeIterator.next();
            if (alarm.isSameAlarm(tempNode)){
                tempNode.setIsSolved((short) 1);
                tempNode.setSolver(alarm.getSolver());
                tempNode.setSolver(alarm.getSolver());
                ret = true;
                break;
            }
        }
        return ret;
    }


    /**
     * set a particular alarm(specified bu alarmId) to be solved
     */
    public void findAlarmAndSolved(int alarmId,String solver,String solveTime) {
        for (AlarmNode n : mAlarms) {
            if (n.getId()==alarmId) {
                n.setIsSolved((short)1);
                n.setSolver(solver);
                n.setSolvedDateStr(solveTime);
                break;
            }
        }
    }


    private boolean findAlarmArrayAndSolved2(List<AlarmNode> list, AlarmNode alarm) {
        boolean ret = false;
        for (AlarmNode n : list) {
            if (n.isSameAlarm(alarm)) {
                n.setIsSolved(alarm.getIsSolved());
                n.setSolver(alarm.getSolver());
                n.setSolvedDate(alarm.getSolvedDate());
                ret = true;
                break;
            }
        }
        return ret;
    }




    @Override
    public int compareTo(@NonNull StationNode another) {
        if (isOnline && !another.isOnline) {
            return -1;
        } else if (!isOnline && another.isOnline) {
            return 1;
        } else {
//            if(another.getState() < mState){
//                return -1;
//            }else if(another.getState() > mState){
//                return 1;
//            }
//            if(another.getId() > mId){
//                return -1;
//            }else if(another.getId() < mId){
//                return 1;
//            }else{
//                return 0;
//            }
            return serverId.compareTo(another.getServerId());
        }
    }

}
