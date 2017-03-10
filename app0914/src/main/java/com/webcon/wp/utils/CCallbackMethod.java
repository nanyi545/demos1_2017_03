package com.webcon.wp.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.webcon.sus.activities.MonitorActivityCompat;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.eventObjects.MonitorEvent;
import com.webcon.sus.eventObjects.ServiceEvent;
import com.webcon.sus.eventObjects.StationEvent;
import com.webcon.sus.utils.AlarmMsgManager;
import com.webcon.sus.utils.CallbackBuffer;
import com.webcon.sus.utils.CommunicationUtils;
import com.webcon.sus.utils.DataParseUtils;
import com.webcon.sus.utils.SUConstant;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * 通信库-回调处理
 * @author m
 */
public class CCallbackMethod extends ContextWrapper {
    private static final String TAG = "CallbackMethod";
    private final boolean DEBUG = WPApplication.DEBUG;
	private static CCallbackMethod me;
	public boolean isResolve;      //解析标识？ true=可以解析
    private CallbackBuffer mBuffer;
	private static ParseCallbackDataThread mParser;   // change to static  ？？？

    private boolean data_yes=true;
    private byte[] dataVideoBuffer;

    /*  ------------------------------ */
    private CCallbackMethod(Context base) {
        super(base);
        isResolve = true;
        mBuffer = CallbackBuffer.getInstance();
        mParser = new ParseCallbackDataThread();
        mParser.start();
    }

	public static CCallbackMethod getInstance(Context context) {
		if (me == null) {
			synchronized (CCallbackMethod.class) {
				if (me == null) {
					me = new CCallbackMethod(context);
				}
			}
		}
		return me;
	}

    /**
     * 回调数据
     */
    private byte[] callbackBuffer;

    /**
     * 回   调:
     * 1.新报警消息
     * 2.视频数据
     * 3.其他通知（状态改变信息等）：包括站场上下线、用户异地登陆
     */
    public void onCallback(){
        if(DEBUG){
            Log.w(TAG, "##############onCallback#############");
        }
//        Log.i("Thread", "OnCallBack"+Thread.currentThread().getName());  //运行在service的线程A中
        mBuffer.enQueue(callbackBuffer);
        if (mParser != null) {
            mParser.inDataOpen();
        }
    }

    /**
     * 清理工作
     */
    public void release(){
        isResolve = false;
        if(mParser != null && mParser.isAlive()){
            mParser.interrupt();
        }
        // 释放后，下次重新初始化
        me = null;
    }

    /* ------------------- */
    /**
     * 读取缓冲区中的数据并解析
     */
    private class ParseCallbackDataThread extends Thread {

        @Override
        public void run() {
            try {
                    while (isResolve) {
                        if (mBuffer == null || mBuffer.getSize() <= 0) {
                            synchronized (this) {
                                wait();
                            }
                        } else {
                            // 解析是按顺序执行的
                            byte[] data = mBuffer.deQueue();
//                            Log.i("BBB","ParseCallbackDataThread--DATA length"+data.length); // 长度30464？？
//                            dumpDataBBB(data);
                            if (data != null && data.length != 0) {
                                parseHeader(data);
                            }
                        }
                    }

            } catch (InterruptedException e) {   //InterruptedException
                e.printStackTrace();
            } finally {
                if(DEBUG){
                    Log.i(TAG, "callback thread finnally");
                }
                mBuffer.clearData();
            }
        }

        public void inDataOpen() {
            synchronized (this) {
                notify();
            }
        }
    }

    /**
     * 解析头部：
     * <br>全部数据包括：①JNI层附加的头部、②PDU头部、③子PDU三个部分
     * <br>本方法先处理①附加的头部
     * <br>| length(4) | pdu(2) | msgType(2) | dataType(2) | datalength(4) | [PDU] |
     * <p>说明：有些回调需要一个回应；但如果是视频数据，则不需要回复</p>
     */
    private void parseHeader(byte[] data){
        if(!isResolve){  //isResolve false-->return, isResolve true-->work
            return;
        }
        // ----
        int offset = 0;
        // 数据长度
        int length = JTools.Bytes4ToInt(data, offset);
        offset += 4;
        // pdu类型：主要是来自服务软件的数据。。
        short pdu = JTools.Bytes2ToShort(data, offset);
        offset += 2;
        // msgType 消息类型：只需要：回调数据包 CALLBACK_PDU_MSG_TYPE_DATA
        short msgType = JTools.Bytes2ToShort(data, offset);
        offset += 2;
        // dataType 数据类型：1: 必达控制包 2:聊天文本信息 3:音视频数据包 可以不用理会
        short dataType = JTools.Bytes2ToShort(data, offset);
        offset += 2;
//        Log.i(TAG, "----length: " + length + ", pdu: " + pdu
//                + ", msgType: " + msgType + ", dataType: " + dataType + " ----");
        Log.i(MonitorActivityCompat.TAG, "parseHeader----length: " + length + ", pdu: " + pdu
                + ", msgType: " + msgType + ", dataType: " + dataType + " ----");
//        // 视频：----length: XX, pdu: 14, msgType: 1, dataType: 3 ----  //一秒多个视频包
//        // 报警消息：----length: 21, pdu: 14, msgType: 1, dataType: 1 ----


        // 收到的普通数据
        if(msgType == SUConstant.CALLBACK_PDU_MSG_TYPE_OnReceivedData){
            // 子pdu长度
            int pduLen = JTools.Bytes4ToInt(data, offset);
            if(DEBUG){
                Log.i(TAG, "pduLen:" + pduLen);
//                Log.i("BBB", "pduLen:" + pduLen);
            }
            offset += 4;

            if(pduLen <= 0){
                Log.e(TAG, "PDU数据长度错误：" + pduLen);
                return;
            }

            byte[] subData = new byte[pduLen];
            System.arraycopy(data, offset, subData, 0, pduLen);
//            Log.i("BBB", "parseHeader--" + "--data.length:" + data.length + "--PduLen" + pduLen);

            switch (pdu){
                // 来自中心服务器转发->站场服务器
                case SUConstant.PDU_B_WIN_SEND_CLI:
                    parseWinInformHead(subData);
                    break;
                // 来自中心服务器
                case SUConstant.PDU_D_SERVER_RSP_CLI:
                    parseStationState(subData);
                    break;
                // 来自服务器数据库的 （*不需要）
                case SUConstant.PDU_A_DB_RSP_CLI:
                    Log.e(TAG, "DISCARD DB DATA");
                    break;
                // 来自站场服务器的回应  （*不需要）
                case SUConstant.PDU_C_WIN_RSP_CLI:
                    Log.e(TAG, "DISCARD WIN RSP DATA");
                    break;
                default:
                    Log.e(TAG, "DISCARD --unkown data--");
//                    Log.i("BBB", "DISCARD --unkown data--");
                    break;
            }
        } else if(msgType == SUConstant.CALLBACK_PDU_MSG_TYPE_OnSecondLogined){
            // 用户在异地登录，被挤出  发送广播==> LogoutExitReceiver
            sendExitBroadcast(msgType);
            Log.i("LogOut", "--------------LogOut-------------");
        } else{
            Log.e(TAG, "unkown message type");
        }
    }

    /**
     * 解析站场服务器发送的数据，主要处理②PDU头部
     * <p>| socket1(2) | socket2(2) | subPduType(2) | dataLen(4) | data... |</p>
     */
    private void parseWinInformHead(byte[] data){

//        Log.i(MonitorActivityCompat.TAG,"parseWin data Length:"+data.length);

        if(!isResolve){
            return;
        }
//        dumpData2(data);//显示数据
        int offset = 0;

        // pdu头：socket（sub-centre)
        short socket1 = JTools.Bytes2ToShort(data, offset);
        offset += 2;

        // pdu头：socket（win-sub)
        short socket2 = JTools.Bytes2ToShort(data, offset);
        offset += 2;

        // pdu头：子PDU类型
        short subPduType = JTools.Bytes2ToShort(data, offset);
        if(DEBUG){
            Log.i(TAG, "subPduType:" + subPduType);
        }
        offset += 2;

        // pdu头：数据长度
        int subPduLen = JTools.Bytes4ToInt(data, offset);
        if(DEBUG){
            Log.e(TAG, "supPduLen:" + subPduLen);
        }
        offset += 4;

        if ((subPduLen>2999999)|(subPduLen<0)) return; // does this
        // 子PDU
        byte[] subData = new byte[subPduLen];  //
        Log.i(MonitorActivityCompat.TAG, "parseWinInformHead--" + "--socket1:"+socket1+"--socket2:"+socket2+"--subPduType"+subPduType+"--subPduLen"+subPduLen);

        //  parseWinInformHead----socket1:6--socket2:9--subPduType10003--subPduLen0
        //   socket1:6--socket2:9--subPduType10003--subPduLen0

//        Log.i("BBB", "parseWinInformHead--" + "--socket1:"+socket1+"--socket2"+socket2+"--subPduType"+subPduType+"--subPduLen"+subPduLen);
//        Log.i("BBB","datalength:"+data.length+"---offset:"+offset+"---subPduLen:"+subPduLen);
        // parseWinInformHead----socket1:8--socket2:9--subPduType10023--subPduLen34
        // datalength:44---offset:10---subPduLen:34


        if (data.length>=(offset+subPduLen)) {  // make sure arraycopy is safe
            System.arraycopy(data, offset, subData, 0, subPduLen);
        }
        else return;


        // 分派子pdu处理
        switch(subPduType){
            // 接收新报警消息 --需要回应
            case SUConstant.SUB_NEW_ALARM:
//                dumpData(data);
                short rspRet = -1;  //已处理
//                Log.i("BBB","subData:LENGTH"+subData.length);
                if(parseSubNewAlarm(subData)){   //  parseSubNewAlarm 返回 已处理-->false  未处理-->true
                    rspRet = 0;   // 未处理  ？
                }
                if(!WPApplication.DEBUG_NO_RSP){   //回应服务器
                    CommunicationUtils.getInstance().respondWin(socket1, socket2, rspRet);
                }

                break;
            // 接收预览视频数据 --不需要回应
            case SUConstant.SUB_PREVIEW_DATA:
//                Log.i(MonitorActivityCompat.TAG,""+subPduLen);
                parseSubVideoData(subData);
                break;
            // 报警处理状态修改，一般是别人处理了，在这个手机更新  --不需要回应
            case SUConstant.SUB_RESPOND_ALARM_UPDATE:
                parseAlarmUpdate(subData);
                break;
            // 接收报警消息已处理通知 --不需要回应
            case SUConstant.SUB_RSP_PROCESS_ALARM:
                dumpData(data);
                AlarmMsgManager.getInstance().transmitCenter(
                        AlarmMsgManager.ALARM_MANAGE_SOLVE_RECEIVE,
                        parseSubToSolvedAlarm(subData));
                break;
            case SUConstant.SUB_REQUEST_GET_DEVICES:
                Log.i(MonitorActivityCompat.TAG,"parseWinInformHead--10017");
                Log.i("BBB","parseWinInformHead--10017");
                break;
            case SUConstant.SUB_REQUEST_MODIFY:
                Log.i(MonitorActivityCompat.TAG,"parseWinInformHead--10019");
                Log.i("BBB","parseWinInformHead--10019");
                break;
            default:
                Log.e(TAG, "未知PDU类型：" + subPduType);
                break;
        }
    }


    private void parseAlarmUpdate(byte[] data){
        int offset = 4; // 跳过自定义字段
        int alarmId=-1;
        StringBuilder sb1 = new StringBuilder();

        alarmId = JTools.Bytes4ToInt(data, offset);
        offset+=4;

        // get solver user Id
        String userSolveId= DataParseUtils.getOneString(offset, data);
        offset += (userSolveId.length() + 1);

        // get alarm station ID
        String statonId= DataParseUtils.getOneString(offset, data);
        offset += (statonId.length() + 1);


        // time_1 报警时间-时分秒 %H:%M:%S
        String time1 = DataParseUtils.getOneString(offset, data);
        offset += (time1.length() + 1);

        // time_2 报警时间-年月日 %y-%m-%d
        String time2 = DataParseUtils.getOneString(offset, data);
        offset += (time2.length() + 1);

        //  + " soveTime:"+ time2+time1
        WPApplication.getInstance().setAlarmSolved(statonId,alarmId, userSolveId,time2+" "+time1);  // set to alarm to solve--update data
        AlarmMsgManager.getInstance().transmitCenter(AlarmMsgManager.ALARM_MANAGE_INIT, null);  // update corresponding UI

        Log.i(MonitorActivityCompat.TAG,"solver:"+userSolveId+"  station:"+statonId+"  alarm id:"+alarmId + " soveTime:"+ time2+time1 );

    }



    /**
     * 解析新报警消息
     * <p>| alarmId(4) | time_1(?) | time_2(?) | device(?) | posType(2) | posMsg(4) | captureFlag(2)
     *      | solveFlag(2)| solveTime_1(?) | sovleTime_2(?) | solver(?) | </p>
     * @return 是否处理成功，用于回应   已处理-->false  未处理-->true
     */
    private boolean parseSubNewAlarm(byte[] data){

//        Log.i("BBB","parseSubNewAlarm::"+isResolve);

        if(!isResolve){
            return false;
        }
        if(data.length == 0){
            return false;
        }

        int offset = 0;
        StringBuilder sb = new StringBuilder();
        AlarmNode alarm = new AlarmNode();

        // alarmId 警报编号 4
        int alarmId = JTools.Bytes4ToInt(data, offset);
        alarm.setId(alarmId);
        offset += 4;
//        Log.i("BBB","alarmId"+alarmId);

        // time_1 报警时间-时分秒 %H:%M:%S
        String time1 = DataParseUtils.getOneString(offset, data);
        offset += (time1.length() + 1);

//        Log.i("BBB","parseSubNewAlarm   alarmId:"+alarmId+"----time十分秒"+time1);

        // time_2 报警时间-年月日 %y-%m-%d
        String time2 = DataParseUtils.getOneString(offset, data);
        offset += (time2.length() + 1);
        sb.append(time2);
        sb.append(" ");
        sb.append(time1);

//        Log.i("BBB", "parseSubNewAlarm  sb" +sb.toString());
//       sb.toString()   2015-09-17 13_49_23
        alarm.setAlarmDate(sb.toString());
        sb.delete(0, sb.length());

        // device 设备昵称 string
        String deviceName = DataParseUtils.getOneString(offset, data);
//        Log.i("BBB","deviceName----"+deviceName);
        alarm.setDeviceName(deviceName);
        alarm.setStationId(WPApplication.getInstance().getStationId(deviceName));
        offset += (deviceName.length() + 1);

//        // posType 位置信息类型  2
//        short posType = JTools.Bytes2ToShort(data, offset);
//        alarm.setPositionType(posType);
//        offset += 2;

        // posMsg 段位置信息（编号） 4       // posType == 0 表示段位信息
        int segment = JTools.Bytes4ToInt(data, offset);
        alarm.setPositionSegmentMsg(segment);
        offset += 4;

        // captureFlag 图片标识 2    // ==1表示有抓拍图片
        short captureFlag = JTools.Bytes2ToShort(data, offset);
//        Log.i("BBB","captureFlag"+captureFlag);

        alarm.setCapture(captureFlag == 1);// ==1表示有抓拍图片
        offset += 2;

        Log.i(MonitorActivityCompat.TAG,"subNewAlarm alarmId:"+alarmId+"-time"+time1+"-日"+time2+"-deviceName"+deviceName+"-captureFlag"+captureFlag+"-段位"+segment);
//      alarmId:2----time十分秒14_20_49---年月日2015-09-17---deviceName19---captureFlag1

        // solveFlag 处理标识 2      // 0表示未处理 1表示已处理
        short isSolved = JTools.Bytes2ToShort(data, offset);
        alarm.setIsSolved(isSolved);

        if (isSolved == 1) {
            if(DEBUG){
                Log.e(TAG, "解析新消息异常：消息已处理!");
            }
            return false;
        }

        //创建报警消息结点
        ArrayList<AlarmNode> list = new ArrayList<>();
        list.add(alarm);
        //转发处理  --->发给AlarmMsgManager 更新UI
        AlarmMsgManager.getInstance().transmitCenter(AlarmMsgManager.ALARM_MANAGE_ADD, list);
        return true;
    }

    /**
     * 将收到的已处理报警消息通知处理成消息结点
     * <p> | userdata(4) | solveFlag(4) | userId(?) | alarmId(4) |</p>
     */
    private List<AlarmNode> parseSubToSolvedAlarm(@NonNull byte[] data){
        if(data.length <= 0){
            Log.e(TAG, "空数据");
            return null;
        }
        // 解析数据
        int offset = 4;
        // 处理标识
        int solveFlag = JTools.Bytes4ToInt(data, offset);
        offset += 4;
        if(solveFlag == SUConstant.ALARM_FLAG_SOLVED_NOTIFY){
            AlarmNode alarm = null;
            // 获取用户Id
            String userId = DataParseUtils.getOneString(offset, data);
            offset += (userId.length() + 1);
            // 获取报警消息编号
            int alarmId = JTools.Bytes4ToInt(data, offset);
            // 找出相应的报警消息结点
            for(AlarmNode n : WPApplication.getInstance().getAllAlarmList()){
                if(n.getId() == alarmId){
                    // 传递了引用的话，就变成了直接操作了
                    alarm = n;
                    alarm.setSolver(userId);
                    alarm.setIsSolved((short) 1);
                    alarm.setSolvedDate(new Date().getTime());
                }
            }
            if(alarm == null){
                return null;
            }
            List<AlarmNode> list = new ArrayList<>();
            list.add(alarm);
            return list;
        }else{
            return null;
        }
    }

    /**
     * 解析视频数据，并调用回调接口进行播放
     * <p>| handlerData(4) | deviceName(?) | dataType(4) | dataLen(4) | videoData(?) |</p>
     */
    private void parseSubVideoData(byte[] data){
        if(DEBUG){
            Log.i(TAG, "---<<parse video data | len :" + data.length + ">>---");
        }

        int offset = 0;

        // handlerData 预览句柄 跳过
        offset += 4;

        // deviceName 设备昵称 跳过    //   TODO  --getOneString ERROR?
        String un = null;
        try {
            un = CommunicationUtils.getOneString(offset, data);
//            Log.i(MonitorActivityCompat.TAG, "deviceName:" + un + "; offset:" + un.length());
        } catch (Exception e) {
            Log.i(MonitorActivityCompat.TAG, "deviceName is null" +(un==null) +  " offset"+ offset+"  ---data:length" +data.length);
            Log.i(MonitorActivityCompat.TAG, Log.getStackTraceString(e));
        }


        offset += (un.length() + 1);

        if(DEBUG){
            Log.i(TAG, "deviceName:" + un + "; offset:" + un.length());
        }

        // dataType 数据类型 4 (=1 视频头数据    =2 视频流数据)
        int dataType = JTools.Bytes4ToInt(data, offset);
        offset += 4;

        // dataLen 数据长度 4
        int dataLen = JTools.Bytes4ToInt(data, offset);
        offset += 4;

//        if(DEBUG){
//            Log.i(TAG, "dataType:" + dataType + "; dataLen:" + dataLen);
//        }
        Log.i(MonitorActivityCompat.TAG, "dataType:" + dataType + "; dataLen:" + dataLen);
        //  dataType:256; dataLen:818????  //  dataType   -861405184  ???

        // 取出数据
        if(dataLen > 0){
            try {
                byte[] video = new byte[dataLen];
                System.arraycopy(data, offset, video, 0, dataLen);
                CommunicationUtils.getInstance().onVideoData(dataType, video, dataLen);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Log.e(TAG, "video data is null");
        }
    }

    /**
     * 解析站场状态改变信息：站场上下线通知
     * <p>| result(2) | pmid(4) | subType(2) |</p>
     */
    private void parseStationState(byte[] data){
        if(DEBUG){
            Log.i(TAG, "----callback-------parseStationState--------");
        }
        Log.i("stationChange","parseStationState");
        final List<Integer> stationIds = new ArrayList<>();
        // 解析数据
        if(CommunicationUtils.getInstance().parseServerStationsData(data, stationIds)){
            // 发送回复
            if(!WPApplication.DEBUG_NO_RSP){
                CommunicationUtils.getInstance().respondServer((short) 0);
                CommunicationUtils.getInstance().registerProcessSingle(stationIds);
            }
            new Thread(){
                @Override
                public void run(){
                    // 重建站场数据
                    CommunicationUtils.getInstance().refreshAllStationInformation(stationIds);
                    // 发布清除通知事件
                    EventBus.getDefault().post(new ServiceEvent(ServiceEvent.SERVICE_EVENT_CLEAR_NOTI));
                    // 发布刷新事件
                    EventBus.getDefault().post(new StationEvent(StationEvent.STATION_EVENT_ONLINE_STATE_CHANGED));
                }
            }.start();
        }else{
            // 发送回复
            if(!WPApplication.DEBUG_NO_RSP){
                CommunicationUtils.getInstance().respondServer((short) -1);
            }
        }
    }

    private void dumpData(byte[] data){
        if(DEBUG){
            Log.w(TAG, "[----------------data----------------]");
            if(data.length > 100){
                for(int i = 0; i < 100; i++){
                    Log.i(TAG, "byte data:" + data[i]);
                }
            }else{
                for(byte b : data){
                    Log.i(TAG, "byte data:" + b);
                }
            }
            Log.w(TAG, "[----------------data-----------------]");
        }
    }


    private void dumpDataBBB(byte[] data) {
        Log.w(TAG, "[----------------data----------------]");
        if (data.length > 100) {
            for (int i = 0; i < 100; i++) {
                Log.i("BBB", "index:" + i + "----byte data:" + data[i]);
            }
        } else {
            for (byte b : data) {
                Log.i("BBB", "byte data:" + b);
            }
        }
        Log.w(TAG, "[----------------data-----------------]");
    }



    private void dumpData2(byte[] data){
        if(DEBUG){
            Log.w(TAG, "[----------------data----------------]");
            if(data.length < 50){
                for(byte b : data){
                    Log.i(TAG, "byte data:" + b);
                }
            }else{
                for(int i = 0; i < 50; i++){
                    Log.i(TAG, "byte data:" + data[i]);
                }
            }
            Log.w(TAG, "[----------------data-----------------]");
        }
    }


    // 发送退出广播
    private void sendExitBroadcast(int msgType){
        // 视频预览退出
        EventBus.getDefault().post(new MonitorEvent(MonitorEvent.MONITOR_EXCEPTION_QUIT));
        // 应用退出
        WPApplication.flag_exit = true;
        Bundle bundle = new Bundle();
        bundle.putInt("msgType", msgType);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setAction(WPConstant.LOGOUT_EXIT_ACTION);
        sendBroadcast(intent);
    }

//    //--- MyTest=========
//    public void testNoti(){
//        //| length(4) | pdu(2) | msgType(2) | dataType(2) | datalength(4) | [PDU] |
//        int offset = 0;
//        byte[] head = new byte[14 + 10];
//        JTools.IntToBytes4(0, head, offset);
//        offset += 4;
//        JTools.ShortToBytes2(SUConstant.PDU_B_WIN_SEND_CLI, head, offset);
//        offset += 2;
//        JTools.ShortToBytes2(SUConstant.CALLBACK_PDU_MSG_TYPE_OnReceivedData, head, offset);
//        offset += 2;
//        JTools.ShortToBytes2((short) 1, head, offset);
//        offset += 2;
//        int pduLenOffset = offset;
//        offset += 4;
//        //| socket1(2) | socket2(2) | subPduType(2) | dataLen(4) | data... |
//        JTools.ShortToBytes2((short)0, head, offset);
//        offset += 2;
//        JTools.ShortToBytes2((short)2, head, offset);
//        offset += 2;
//        JTools.ShortToBytes2(SUConstant.SUB_NEW_ALARM, head, offset);
//        offset += 2;
//        int pduSubLenOffset = offset;
//        // | deviceName | date | content |
//        ///获取报警时间
//        //获取报警内容
//        try {
//            byte[] deviceName = "Orange\0".getBytes(WPConstant.STRING_UTF8);
//            byte[] alarmData = getDate();
//            byte[] alarmContent = "(;¬_¬)\0".getBytes(WPConstant.STRING_UTF8);
//            int dataLen = deviceName.length + alarmData.length + alarmContent.length;
//            byte[] req = new byte[head.length + dataLen];
//            int offset2 = 0;
//            System.arraycopy(head, 0, req, offset2, head.length);
//            offset2 += head.length;
//            System.arraycopy(deviceName, 0, req, offset2, deviceName.length);
//            offset2 += deviceName.length;
//            System.arraycopy(alarmData, 0, req, offset2, alarmData.length);
//            offset2 += alarmData.length;
//            System.arraycopy(alarmContent, 0, req, offset2, alarmContent.length);
//            JTools.IntToBytes4(10 + dataLen, req, pduLenOffset);
//            JTools.IntToBytes4(dataLen, req, pduSubLenOffset);
//            Log.w(TAG, "----------------------send-----------------");
//            parseHeader(req);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

    private byte[] getDate() throws UnsupportedEncodingException {
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = sdf.format(curDate) + "\0";
        return str.getBytes(WPConstant.STRING_UTF8);
    }

    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());


    /* --------------- native ---------------- */
    /**
     * 提供回调函数的类
     * @return 0成功 <0 失败
     */
    public native int setCallBackObj();


    /**
     * 后台开始工作
     * 调用java回调函数返回数据，
     * note:在FCNetACLogin成功之后才能调用
     *
     * @param javaPath      java回调函数的路径
     * @param javaMethod    java回调函数的方法名称
     * @param bufName       返回数据存放的数组名称
     * @return              0成功 <0 失败
     */
    public native int doWork(String javaPath, String javaMethod, String bufName);
}
