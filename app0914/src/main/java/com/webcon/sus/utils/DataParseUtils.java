package com.webcon.sus.utils;


import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.webcon.sus.activities.AlarmDetailsActivityCompat;
import com.webcon.sus.activities.MonitorActivityCompat;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.entity.BaseDevice;
import com.webcon.sus.entity.DeviceFactory;
import com.webcon.sus.entity.ImageNode;
import com.webcon.sus.entity.StationNode;
import com.webcon.wp.utils.JTools;
import com.webcon.wp.utils.WPApplication;
import com.webcon.wp.utils.WPConstant;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据的打包和解析
 * @author m
 */
public class DataParseUtils {
    private static final String TAG = "DataParseUtils";
    protected boolean DEBUG = WPApplication.DEBUG;
    private static final int HALF_SHORT = 0xFF;
    private static final int HALF_INT = 0xFFFF;

    /**
     * 初始化数据的解析，主要用于拆分头部
     */
    public boolean parseInit(byte[] data, StationNode station) {
        byte[] trimmedData = checkHead(data);
        if (trimmedData == null) {
            if(DEBUG){
                Log.e(TAG, "返回空数据");
            }
            return false;
        }
        int offset = 0;
        // 获取子PDU
        short subPdu = JTools.Bytes2ToShort(trimmedData, offset);
        if (DEBUG) {
            Log.i(TAG, "subPdu:" + subPdu);
        }
        offset += 2;
        // 子PDU数据
        byte[] subData = new byte[trimmedData.length - offset];
        System.arraycopy(trimmedData, offset, subData, 0, subData.length);

        // 判断子PDU
        boolean ret;
        switch (subPdu) {
            // 站场列表
            case SUConstant.SUB_DB_RSP_STATIONS:   // 20107
                ret = parseInitStations(subData);
                break;
            // 设备列表
            case SUConstant.SUB_RSP_GET_DEVICES:
                if(station == null){
                    if(DEBUG){
                        Log.e(TAG, "StationNode is null");
                    }
                    return false;
                }
                ret = parseInitDevices(station, subData);
                break;
            // 报警消息列表
            case SUConstant.SUB_RSP_GET_ALARMS:

                if(station == null){
                    if(DEBUG){
                        Log.e(TAG, "StationNode is null");
                    }
                    return false;
                }
                ret = parseInitAlarms(station, subData);
                Log.i(MonitorActivityCompat.TAG,"SUB_REQUEST_GET_ALARMS  10022"+ret);

                break;
            default:
                if (DEBUG) {
                    Log.e(TAG, "no matched sub Pdu");
                }
                return false;
        }
        return ret;
    }

    /**
     * 解析初始站场信息
     * <p> | result(2) | [| clusterId(2) | clusterUserId(4) |] | num(4) | [for] | </p>
     * <p> | parentId(4) | grantId(4)(=0则name作用户昵称)  | nodeType(4) | name(?) | serverId(?) |</p>
     *
     * @param data 子Pdu数据
     */
    public boolean parseInitStations(byte[] data) {
        ArrayList<StationNode> list = new ArrayList<>();
        int offset = 0;
        // result 请求结果
        short result = JTools.Bytes2ToShort(data, offset);
        if (result != 0) {
            if (DEBUG) {
                Log.e(TAG, "查询数据库错误：" + result);
            }
            return false;
        }
        offset += 2;

        // clusterId 集群号 2
        short clusterId = JTools.Bytes2ToShort(data, offset);
        WPApplication.getInstance().getCurrentUser().setClusterid(clusterId);
        offset += 2;

        // clusterUserId 集群userId 4
        int clusterUserId = JTools.Bytes4ToInt(data, offset);
        WPApplication.getInstance().getCurrentUser().setClusterUserId(clusterUserId);
        offset += 4;

        // num 结点数量
        int len = JTools.Bytes4ToInt(data, offset);
        offset += 4;
        if (DEBUG) {
            Log.i(TAG, "station len:" + len);
        }
        if (len <= 0) {
            if (DEBUG) {
                if (len == -1) {
                    Log.e(TAG, "用户不在组织结构!");
                } else {
                    Log.e(TAG, "站场数量错误!");
                }
            }
            return false;
        } else {
            int parentId, grantId, stationType;
            String serverName, serverId;
            //循环解析站场结点
            for (int i = 0; i < len; i++) {
                // parentId 父节点id
                parentId = JTools.Bytes4ToInt(data, offset);
                offset += 4;
                // grantId 结点id
                grantId = JTools.Bytes4ToInt(data, offset);
                offset += 4;
                // nodeType 类型     (节点：1，站场：2)
                stationType = JTools.Bytes4ToInt(data, offset);
                offset += 4;
                // name 节点名
                serverName = getOneString(offset, data);

                try {
                    offset += (serverName.getBytes(WPConstant.STRING_GB2312).length + 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                if (DEBUG) {
                    Log.i(TAG, "Name:" + serverName);
                }
                // serverId 服务器id
                serverId = getOneString(offset, data);
                Log.i("Communication","--??????---"+i+"  ;;; "+serverId+"   station type:"+stationType+ "   server name:"+serverName);

                try {
                    offset += (serverId.getBytes(WPConstant.STRING_GB2312).length + 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // 处理结果
                if (parentId == 0 && !serverName.equals("")) {
                    // 设置用户名
                    WPApplication.getInstance().getCurrentUser().setUserName(serverName);
                } else if (stationType == SUConstant.DB_ORG_TYPE_STATION
                        && !serverId.equals("") && !serverName.equals("")) {
                    // 初始化结点
                    StationNode node = new StationNode();
                    node.setName(serverName);
                    node.setServerId(serverId);
                    list.add(node);
                } else {
                    if (DEBUG) {
                        Log.w(TAG, "处理站场结点失败！");
                    }
                }
            }
        }
        // 取得全部站场，保存到全局
        WPApplication.getInstance().setStationList(list);
        return true;
    }

    /**
     * 解析初始设备信息
     * <p> 子PDU: | userData(4) | result(4) | defenceState(2) | sum(4) | [for] | </p>
     * <p> [| type(short) | deviceName(?) |] </p>
     * @param data 子Pdu数据（不包括类型）
     */
    public boolean parseInitDevices(@NonNull StationNode station, byte[] data) {
        ArrayList<BaseDevice> list = new ArrayList<>();
        int offset = 0;

        // userData 跳过  4
        offset += 4;

        // result (= 0 请求成功)   4
        int result = JTools.Bytes4ToInt(data, offset);
        if (result != 0) {
            if (DEBUG) {
                Log.e(TAG, "请求站场设备信息失败：" + result);
            }
            return false;
        }
        offset += 4;

        // defenceState 布防状态（=1 开启， =0 关闭）  2
        short defenceState = JTools.Bytes2ToShort(data, offset);
        int stationState;
        if(defenceState == 1){
            stationState = SUConstant.FLAG_STATION_OPENED;
        }else if(defenceState == 0){
            stationState = SUConstant.FLAG_STATION_CLOSED;
        }else{
            if(DEBUG){
                Log.e(TAG, "错误的布防信息：" + defenceState);
            }
            return false;
        }
        station.setState(stationState);
        offset += 2;

        // sum 设备数量 4
        int sum = JTools.Bytes4ToInt(data, offset);
        if (sum < 0) {
            if (DEBUG) {
                Log.e(TAG, "错误的设备数量!");
            }
            return false;
        }
        offset += 4;

        // 循环解析
        short deviceType;
        int deviceTypeLocal;
        String deviceName;
        for (int i = 0; i < sum; i++) {
            // type 设备状态 2
            deviceType = JTools.Bytes2ToShort(data, offset);
            switch (deviceType){
                case SUConstant.DEVICE_PDU_TYPE_MONITOR:
                    deviceTypeLocal = SUConstant.DEVICE_TYPE_MONITOR;
                    break;
                case SUConstant.DEVICE_PDU_TYPE_RADAR:
                    deviceTypeLocal = SUConstant.DEVICE_TYPE_RADAR;
                    break;
                default:
                    deviceTypeLocal = SUConstant.DEVICE_TYPE_OTHER;
                    break;
            }
            offset += 2;

            // deviceName 昵称
            deviceName = getOneString(offset, data);

            try {
                offset += (deviceName.getBytes(WPConstant.STRING_GB2312).length + 1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }




            // 创建设备
            BaseDevice device = DeviceFactory.getInstance().createDevice(deviceTypeLocal);
            device.setName(deviceName);
            device.setIsAvailable(true);
            list.add(device);

            // 添加到全局映射表 NOTE：全局映射表只在这里有添加项，没有考虑其他意外情况
            WPApplication.getInstance().addDeviceReflect(deviceName, station.getId());
        }
        station.setDeviceList(list);

        return true;
    }

    /**
     * 解析初始报警信息
     * <p> | userData(4) | nRet(4) | len(4) |[alarms]</p>
     * <p>废弃：| num(2) | [!!] |</p>
     * <p>[| 警报编号 | 报警时间-时分秒 | 报警时间-年月日 | 设备昵称 | 位置信息类型 | 段位置信息（编号）
     *      | 图片标识 | 处理标识 | 处理时间-时分秒 | 处理时间-年月日 | 处理者 |]</p>
     * <p>[| alarmId(4) | time_1(?) | time_2(?) | device(?) | posType(2) | posMsg(4) | captureFlag(2)
     *      | solveFlag(2)| solveTime_1(?) | sovleTime_2(?) | solver(?) |]</p>
     * @param data 子Pdu数据（不包括类型）
     */
    public boolean parseInitAlarms(@NonNull StationNode station, byte[] data) {
        ArrayList<AlarmNode> list = new ArrayList<>();
        int newAlarms = 0;
        int offset = 0;

        // userData 跳过 4
        offset += 4;

        // result (= 0 请求成功)   4
        int result = JTools.Bytes4ToInt(data, offset);
        if (result != 0) {
            if (DEBUG) {
                Log.e(TAG, "请求站场设备信息失败：" + result);
            }
            return false;
        }
        offset += 4;

        StringBuilder sb = new StringBuilder();
        // sum 警报数量 4
        int sum = JTools.Bytes4ToInt(data, offset);
        if (sum < 0) {
            if (DEBUG) {
                Log.e(TAG, "报警数量错误!");
            }
            return false;
        }
        offset += 4;

        Log.i(MonitorActivityCompat.TAG,"SUB_REQUEST_GET_ALARMS  10022:"+"   result=0?"+result+" sum="+sum);


        // 解析
        for (int i = 0; i < sum; i++) {
            // 创建报警消息
            AlarmNode alarm = new AlarmNode();

            // alarmId 警报编号 4
            int alarmId = JTools.Bytes4ToInt(data, offset);
            alarm.setId(alarmId);
            alarm.setStationId(station.getId());
            offset += 4;

            // time_1 报警时间-时分秒 %H:%M:%S
            String time1 = getOneString(offset, data);
            offset += (time1.length() + 1);

            // time_2 报警时间-年月日 %y-%m-%d
            String time2 = getOneString(offset, data);
            offset += (time2.length() + 1);

            sb.append(time2);
            sb.append(" ");
            sb.append(time1);
            alarm.setAlarmDate(sb.toString());
            sb.delete(0, sb.length());

            // device 设备昵称 string
            String deviceName = getOneString(offset, data);
            alarm.setDeviceName(deviceName);
            try {
                offset += (deviceName.getBytes(WPConstant.STRING_GB2312).length + 1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


//            // posType 位置信息类型  2
//            short posType = JTools.Bytes2ToShort(data, offset);
//            alarm.setPositionType(posType);
//            offset += 2;

            // posMsg 段位置信息（编号） 4       // posType == 0 表示段位信息
            int segment = JTools.Bytes4ToInt(data, offset);
            alarm.setPositionSegmentMsg(segment);
            offset += 4;


            // captureFlag 图片标识 2    // ==1 表示有抓拍图片
            short captureFlag = JTools.Bytes2ToShort(data, offset);
            alarm.setCapture(captureFlag == 1);
            offset += 2;

            // solveFlag 处理标识 2      // 0表示未处理 1表示已处理
            short isSolved = JTools.Bytes2ToShort(data, offset);
            alarm.setIsSolved(isSolved);
            offset += 2;

            Log.i("alarmRV","init alarms---date:"+alarm.getAlarmDate()+"   is solved:"+alarm.getIsSolved()+"   station id"+alarm.getStationId()+"  hasPicture:"+alarm.isCapture());



            // 未处理=0, 已处理=1
            if (isSolved == 0) {
                //注意未处理报警消息的累加
                newAlarms++;
            } else if (isSolved == 1) {
                // solveTime_1  处理时间-时分秒 %H:%M:%S
                String solveTime_1 = getOneString(offset, data);
                offset += (solveTime_1.length() + 1);

                // solveTime_2  处理时间-年月日 %y-%m-%d
                String solveTime_2 = getOneString(offset, data);
                offset += (solveTime_2.length() + 1);
                sb.append(solveTime_2);
                sb.append(" ");
                sb.append(solveTime_1);
                alarm.setSolvedDateStr(sb.toString());
                sb.delete(0, sb.length());

                // solver 处理者 string
                String solver = getOneString(offset, data);
                alarm.setSolver(solver);
                offset += (solver.length() + 1);

                Log.i(MonitorActivityCompat.TAG,"initAlarms"+ i +"  solveTime"+solveTime_1+solveTime_2+"  solver"+solver);
            } else {
                if (DEBUG) {
                    Log.e(TAG, "无法识别的报警状态！");
                }
                return false;
            }
            Log.i(MonitorActivityCompat.TAG,"initAlarms"+ i+"th alarm:"+" alarmId:"+alarmId+"-time-"+time1+"-日-"+time2+"-deviceName-"+deviceName+"-captureFlag-"+captureFlag +"-段位-"+segment+"-已处理-"+(isSolved==1));

            // 完成一个报警消息
            list.add(alarm);
        }

        station.setNewAlarm(newAlarms);
        station.setAlarmList(list);
        return true;
    }
    /* ----------------------- 初始化 结束 ----------------------------- */


    /**
     * 校验一般通道PDU的头部：
     * <p>PDU：| result(2) | pmid(4) |//截取之后的→// subType(2) | ... |</p>
     * 包括DB的回复、客户端系统的回复、中心服务器的回复，都有一样的头部
     */
    public synchronized byte[] checkHead(byte[] data) {
        if (!WPApplication.getInstance().getAccessable()) {
            return null;
        }
        if (data == null || data.length == 0) {
            if (DEBUG) {
                Log.e(TAG, "空数据错误");
            }
            CommonUtils.sendErrorEvent(SUConstant.PARSE_RSP_ERROR_WRONG_DATA, 0);
            Log.i(AlarmDetailsActivityCompat.TAG, "空数据错误");
            return null;
        } else if (DEBUG) {
            dumpData(data);
        }
        int offset = 0;
        // 通信层结果
        short tcpResult = JTools.Bytes2ToShort(data, offset);
        offset += 2;
        if (tcpResult != 0) {
            String error;
            if(tcpResult == SUConstant.ERROR_RETURN_OVERTIME){
                error = "返回超时";
            }else if(tcpResult == SUConstant.ERROR_RETURN_OFFLINE){
                error = "请求的站场不在线";
            }else {
                error = "请求失败";
            }
            Log.i(AlarmDetailsActivityCompat.TAG, error);
            if(DEBUG){
                Log.e(TAG, error);
            }
            CommonUtils.sendErrorEvent(SUConstant.PARSE_RSP_ERROR_COMMUNICATION, tcpResult);
            return null;
        }
        // 跳过保留字段
        offset += 4;
        // 取出 | subType + 子PDU |
        byte[] subData = new byte[data.length - offset];
        System.arraycopy(data, offset, subData, 0, subData.length);

        return subData;
    }


    /**
     * 解析控制请求回调---站场服务器
     * <p>子PDU一般情况：| userData(4) | result(4)(0表示成功，-1表示失败) |</p>
     * 发生错误便直接处理掉，调用{@link CommonUtils#sendErrorEvent(short, int)}弹出信息
     *
     * @param data 由sendDataToWinSys2返回的回应数据
     */
    public synchronized boolean parsePtzRespond(byte[] data) {
//        dumpDataBBB(data);
        int ret;
        byte[] trimmedData = checkHead(data);
//        dumpDataBBB(trimmedData);
        if (trimmedData == null) {
            return false;
        }
        int offset = 0;
        // 子PDU类型
        short pdu = JTools.Bytes2ToShort(trimmedData, offset);
        if(DEBUG){
            Log.i(TAG, "ptz rsp Pdu:" + pdu);
        }
//        Log.i("BBB", "ptz rsp Pdu:" + pdu);

        Log.i(MonitorActivityCompat.TAG, "ptz rsp Pdu:" + pdu);

        offset += 2;
        // 处理子PDU
        switch (pdu){

            // 开启布防请求
            case SUConstant.SUB_RESPOND_OPEN_DEFENCE:
                // 跳过保留字段
                offset += 4;
                // 获取处理结果
                ret = JTools.Bytes4ToInt(trimmedData, offset);
                Log.i(MonitorActivityCompat.TAG,"bufang kaiqi"+ret);
                // =-1 处理失败  =0 处理成功
                if (ret != 0) {
                    if(DEBUG){
                        Log.e(TAG, "请求处理报警消息失败: " + ret);
                    }
                    CommonUtils.sendErrorEvent(pdu, ret);
                    return false;
                }
                break;
            // 关闭布防请求
            case SUConstant.SUB_RESPOND_STOP_DEFENCE:
                // 跳过保留字段
                offset += 4;
                // 获取处理结果
                ret = JTools.Bytes4ToInt(trimmedData, offset);
                Log.i(MonitorActivityCompat.TAG,"bufang guanbi"+ret);
                // =-1 处理失败  =0 处理成功
                if (ret != 0) {
                    if(DEBUG){
                        Log.e(TAG, "请求处理报警消息失败: " + ret);
                    }
                    CommonUtils.sendErrorEvent(pdu, ret);
                    return false;
                }
                break;

            // 报警消息处理请求结果
            case SUConstant.SUB_RSP_PROCESS_ALARM:
                // 跳过保留字段
                offset += 4;
                // 获取处理结果
                ret = JTools.Bytes4ToInt(trimmedData, offset);
                // =0 处理失败  =1 处理成功  =2 已处理
                offset += 4;  // 跳过处理标志

                String str=getOneString(offset,trimmedData);
                Log.i(MonitorActivityCompat.TAG,"处理ID"+str+"   ret(0fail,1ok,2already):"+ret);


                if (ret != 1) {
                    if(DEBUG){
                        Log.e(TAG, "请求处理报警消息失败: " + ret);
                    }
                    CommonUtils.sendErrorEvent(pdu, ret);
                    return false;
                }
                break;
            // 喊话请求结果处理---
            case SUConstant.SUB_RESP_SPEAKER:
                // 跳过保留字段
                offset += 4;

                ret = JTools.Bytes4ToInt(trimmedData, offset);
                Log.i("BBB", "10033 ret:" + ret);
                // =0 不能喊话  =1 可以喊话
                if (ret==0){
                    return false;
                }
                break;
            default:
                // 其他云台控制请求   云台控制返回--10008
                // 跳过保留字段
                offset += 4;
                // 获取结果
//                Log.i("BBB", "PARSE--- offset:" + offset+"---trimmedData"+trimmedData.length);

                ret = JTools.Bytes4ToInt(trimmedData, offset);

//                Log.i("BBB", "PARSE ret(1success,0fail):" + ret);

                if (ret != 0) {
                    CommonUtils.sendErrorEvent(pdu, ret);
                    return false;
                }
                break;
        }
        return true;
    }

    /**
     * 解析数据库的回应结果
     * <p>可考虑和{@link DataParseUtils#parseInit(byte[], StationNode)}合并</p>
     * <p> | result(2) | pmid(4) | subPdu(2) | result(2) | </p>
     */
    public synchronized boolean parseDBRsp(byte[] data) {
        byte[] trimmedData = checkHead(data);
        if (trimmedData == null) {
            return false;
        }
        int offset = 0;
        // 子PDU类型
        short subPdu = JTools.Bytes2ToShort(trimmedData, offset);
        if (DEBUG) {
            Log.w(TAG, "subPdu: " + subPdu);
        }
        offset += 2;
        switch (subPdu) {
            case SUConstant.SUB_DB_RSP_MODIFY_PW:
            case SUConstant.SUB_DB_RSP_MODIFY_UN:
                break;
            default:
                if (DEBUG) {
                    Log.e(TAG, "未知的PDU类型：" + subPdu);
                }
                CommonUtils.sendErrorEvent(SUConstant.PARSE_RSP_ERROR_WRONG_DATA, -1);
                return false;
        }

        short shRet = JTools.Bytes2ToShort(trimmedData, offset);
        if (shRet != 0) {
            if (DEBUG) {
                Log.e(TAG, "处理失败！");
            }
            CommonUtils.sendErrorEvent(subPdu, shRet);
        }
        return shRet == 0;
    }

    /**
     * 解析站场在线状态数据：包括服务器的回应、服务器的主动通知
     * <p>子PDU（获取在线信息）: | result(2) | num(2) | [data] |  </p>
     * <p>子PDU（上线离线通知）：| num(2) | [data] |   </p>
     * [data]：| serverId(?) | sFlag(2) |
     * (1:在线，2:离线)
     */
    public synchronized boolean parseServerStationsData(byte[] data, List<Integer> stationIds) {
        byte[] trimmedData = checkHead(data);
        if (trimmedData == null) {
            return false;
        }
        int offset = 0;
        // 子PDU类型
        short pdu = JTools.Bytes2ToShort(trimmedData, offset);
        offset += 2;
        Log.i("stationChange","pdu:"+pdu);
        if (pdu == SUConstant.SUB_SERVER_RSP_STATIONS) {
            // result
            short tcpRes = JTools.Bytes2ToShort(trimmedData, offset);
            if (tcpRes != 0) {
                if (DEBUG) {
                    Log.e(TAG, "communication result error：" + tcpRes);
                }
                CommonUtils.sendErrorEvent(SUConstant.PARSE_RSP_ERROR_COMMUNICATION, tcpRes);
                return false;
            }
            offset += 2;
        } else if (pdu != SUConstant.SUB_SERVER_NOTIFY_ONLINE) {
            if (DEBUG) {
                Log.e(TAG, "未知的PDU：" + pdu);
            }
            return false;
        }
        // num 战场个数
        short num = JTools.Bytes2ToShort(trimmedData, offset);
        if (num <= 0) {
            if (DEBUG) {
                Log.e(TAG, "站场个数错误：" + num);
            }
            CommonUtils.sendErrorEvent(SUConstant.PARSE_RSP_ERROR_WRONG_DATA, 0);
            return false;
        }
        offset += 2;

        Log.i("stationChange","战场个数:"+num);

        /* -- 循环解析 | serverId(?) | sFlag(2) |-- */
        // 服务器Id
        String serverId;
        int stationId;
        // 在线状态：1：在线；2：离线
        short flag;
        // 在线状态：boolean
        boolean isOnline;
        for (int i = 0; i < num && offset < trimmedData.length; i++) {
            // 获取战场ID // serverId (String)
            serverId = getOneString(offset, trimmedData);
            stationId = serverId.hashCode();
            try {
                offset += (serverId.getBytes(WPConstant.STRING_GB2312).length + 1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            // sFlag 战场状态
            flag = JTools.Bytes2ToShort(trimmedData, offset);
            offset += 2;

            Log.i("stationChange","战场id:"+serverId+ "  战场状态:(1在线2离线)"+flag);

            // -------处理站场结点---------
            isOnline = flag == SUConstant.STATION_FLAG_ONLINE;
            if (WPApplication.getInstance().getStationNode(stationId) == null) {
                if (DEBUG) {
                    Log.e(TAG, "未找到相应站场结点: " + stationId);
                }
                return false;
            } else {
                // 修改站场的在线状态
                WPApplication.getInstance().getStationNode(stationId).setOnline(isOnline);
                if(stationIds != null){
                    stationIds.add(stationId);
                }
            }
        }
        return true;
    }

    /**
     * 解析获取抓拍图片请求
     * <p>[ 子pdu(2) ][ userData(4) | nRet(4) | picType(2) | picLen(4) | picData(?) ]</p>
     */
    public synchronized ImageNode parseAlarmCaptureRsp(byte[] data){
        byte[] trimmedData = checkHead(data);
        if (trimmedData == null) {
            Log.i(AlarmDetailsActivityCompat.TAG,"checkHead failed");
            return null;
        }
        int ret;
        int offset = 0;
        // 子PDU类型
        short pdu = JTools.Bytes2ToShort(trimmedData, offset);
        offset += 2;
        Log.i(AlarmDetailsActivityCompat.TAG,"return PDU"+pdu);

        if(pdu != SUConstant.SUB_RSP_ALARM_IMAGE){
            Log.i(AlarmDetailsActivityCompat.TAG,"--------------wrong pdu-------------");
            if(DEBUG){
                Log.e(TAG, "错误的PDU: " + pdu);
            }
            return null;
        }
        // userData 跳过保留字段
        offset += 4;
        // nRet 请求结果
        ret = JTools.Bytes4ToInt(trimmedData, offset);
        if (ret != 0) {
            CommonUtils.sendErrorEvent(pdu, ret);
            return null;
        }
        offset += 4;
        ImageNode image = new ImageNode();
        // picType 图片格式
        short imageType = JTools.Bytes2ToShort(trimmedData, offset);
        image.setImageType(imageType);
        offset += 2;
        // picLen 图片长度
        int len = JTools.Bytes4ToInt(trimmedData, offset);
        Log.i(AlarmDetailsActivityCompat.TAG,"picture length"+len);
        if(len <= 0){
            if(DEBUG){
                Log.e(TAG, "图片长度错误!!");
            }
            return null;
        }
        offset += 4;


        // 生成图片
        Bitmap b = BitmapUtil.bytesToBitmap(trimmedData, offset, len);
        if(b == null){
            if(DEBUG){
                Log.e(TAG, "生成Bitmap失败!!");
            }
            return null;
        }
        image.setBitmap(b);
        return image;
    }

    /* ---------------------- create request   ------------------------------------ */
    /* ---------------------- create request   ------------------------------------ */

    /**
     * 创建请求的头: 适用于数据库通道、中心服务器通道的子PDU头部
     * <p>| pmid(4) | subType(2) | </p>
     */
    private byte[] createRequestHeadNormal(short type) {
        byte[] head = new byte[4 + 2];
        // pmid 保留
        JTools.IntToBytes4(0, head, 0);
        // 子pdu类型
        JTools.ShortToBytes2(type, head, 4);
        return head;
    }

    /**
     * 向数据库请求初始化站场列表
     * <p> | userId(char ..) |</p>
     */
    public synchronized byte[] createRequestBDGetStations() {
        byte[] head = createRequestHeadNormal(SUConstant.SUB_DB_REQUEST_STATIONS);
        byte[] userId = getByteArrayFromStr(WPApplication.getInstance().getCurrentUser().getUserId(), true);
        byte[] req = new byte[head.length + userId.length];
        System.arraycopy(head, 0, req, 0, head.length);
        System.arraycopy(userId, 0, req, head.length, userId.length);
        return req;
    }

    /**
     * 向数据库请求修改用户昵称
     * <p> | userId(?) | newNickname(?) |</p>
     */
    public synchronized byte[] createRequestBDModifyUserName(String userId, String newNickname) {
        byte[] head = createRequestHeadNormal(SUConstant.SUB_DB_REQUEST_MODIFY_UN);
        byte[] bUserId = getByteArrayFromStr(userId, true);
        byte[] bNickname = getByteArrayFromStr(newNickname, true);
        byte[] req = new byte[head.length + bUserId.length + bNickname.length];
        int offset = 0;
        System.arraycopy(head, 0, req, offset, head.length);
        offset += head.length;
        System.arraycopy(bUserId, 0, req, offset, bUserId.length);
        offset += bUserId.length;
        System.arraycopy(bNickname, 0, req, offset, bNickname.length);
        return req;
    }

    /**
     * 向数据库请求修改用户密码
     * <p>| userId(?) | oldPasswd(?) | newPasswd(?) |</p>
     */
    public synchronized byte[] createRequestBDModifyPasswd(String userId, String oldPW, String newPW) {
        byte[] head = createRequestHeadNormal(SUConstant.SUB_DB_REQUEST_MODIFY_PW);
        byte[] bUserId = getByteArrayFromStr(userId, true);
        byte[] bOldPwd = getByteArrayFromStr(oldPW, true);
        byte[] bNewPwd = getByteArrayFromStr(newPW, true);
        byte[] req = new byte[head.length + bUserId.length + bOldPwd.length + bNewPwd.length];
        int offset = 0;
        System.arraycopy(head, 0, req, offset, head.length);
        offset += head.length;
        System.arraycopy(bUserId, 0, req, offset, bUserId.length);
        offset += bUserId.length;
        System.arraycopy(bOldPwd, 0, req, offset, bOldPwd.length);
        offset += bOldPwd.length;
        System.arraycopy(bNewPwd, 0, req, offset, bNewPwd.length);
        return req;
    }


    /**
     * 向服务器请求站场在线信息
     * <p>PDU：| userId(?) | num(2) | [data] |      [data]：| serverId(?) |</p>
     *
     * @param servers 待查询站场serverID集合
     */
    public synchronized byte[] createRequestServerStations(String[] servers) {
        byte[] head = createRequestHeadNormal(SUConstant.SUB_SERVER_REQUEST_STATIONS);
        byte[] bUserId = getByteArrayFromStr(WPApplication.getInstance().getCurrentUser().getUserId(), true);
        byte[] bServers = getStationServerIds(servers);
        if(bServers == null){
            return null;
        }
        byte[] req = new byte[head.length + bUserId.length + 2 + bServers.length];
        int offset = 0;
        // 请求头部
        System.arraycopy(head, 0, req, 0, head.length);
        offset += head.length;
        // 用户Id
        System.arraycopy(bUserId, 0, req, offset, bUserId.length);
        offset += bUserId.length;
        // 站场个数
        JTools.ShortToBytes2((short) servers.length, req, offset);
        offset += 2;
        // 添加serverIds
        System.arraycopy(bServers, 0, req, offset, bServers.length);
        Log.i(TAG, "---------------dump req---------------------------");
        dumpData(req);
        return req;
    }

    /**
     * 创建请求：主要是与站场服务器的连接
     * PDU_OpROUTER0_REQ_FromClient: 12
     * <p> | pmid(4) | serverId(?) | subPdu(2) | userData(4) | ... | </p>
     */
    public synchronized byte[] createRequestSys(int subPdu, int stationId, String deviceId, int extra) {
        if (!WPApplication.getInstance().getAccessable()) {
            return null;
        }
        byte[] req = null;
        int offset = 0;
        byte[] bServer = getByteArrayFromStr(WPApplication.getInstance().getServerId(stationId), true);
        byte[] head = new byte[4 + bServer.length + 2 + 4];
        // 保留字段
        intToByte(0, head, 0);
        offset += 4;
        // 站场serverID
        System.arraycopy(bServer, 0, head, offset, bServer.length);
        offset += bServer.length;
        // 子PDU类型
        shortToByte((short) subPdu, head, offset);
        offset += 2;
        // 保留字段
        intToByte(0, head, offset);
        offset += 4;

        switch (subPdu) {
            // 请求打开喊话
            case SUConstant.SUB_REQUEST_SPEAKER:
                //首次喊话请求---请求打开喊话之前 9-18加入
            case SUConstant.SUB_1ST_REQ_SPEAKER:
                //结束喊话请求---喊话完毕之前 9-21加入
            case SUConstant.SUB_LAST_REQ_SPEAKER:
                // 请求设备列表
            case SUConstant.SUB_REQUEST_GET_DEVICES:
            // 请求报警列表
            case SUConstant.SUB_REQUEST_GET_ALARMS:
            // 开启布防请求
            case SUConstant.SUB_REQUEST_OPEN_DEFENCE:
            // 关闭布防请求
            case SUConstant.SUB_REQUEST_STOP_DEFENCE:
                req = head;
                break;
            // 报警消息处理请求
            case SUConstant.SUB_REQUEST_PROCESS_ALARM:
//                Log.i(MonitorActivityCompat.TAG, "create REQ success:"+subPdu);
                String userID=WPApplication.getInstance().getCurrentUser().getUserId();  // 加入id string
                byte[] bID = getByteArrayFromStr(userID, true);
                req = new byte[head.length + 4 + bID.length];
                System.arraycopy(head, 0, req, 0, head.length);
                JTools.IntToBytes4(extra, req, offset);
                System.arraycopy(bID, 0, req, offset+4, bID.length);
//                Log.i("AlarmMsgManager", "create REQ success");
                break;
            // 请求抓拍图片
            case SUConstant.SUB_REQUEST_ALARM_IMAGE:
                req = new byte[head.length + 4];
                System.arraycopy(head, 0, req, 0, head.length);
                JTools.IntToBytes4(extra, req, offset);
                break;
            // 手机端上下线请求 | type(4) | clusterUserId(4) |
            case SUConstant.SUB_REQUEST_REGISTER:
                intToByte(extra, head, offset - 4);
                short clusterId = WPApplication.getInstance().getCurrentUser().getClusterid();
                int clusterUserId = WPApplication.getInstance().getCurrentUser().getClusterUserId();
                if(DEBUG){
                    Log.i(TAG, "register>>> clusterId:" + clusterId + " clusterUserId:" + clusterUserId);
                }
                if(clusterUserId == -1){
                    if(DEBUG){
                        Log.e(TAG, "无效的集群userId!");
                    }
                }else{
                    req = new byte[head.length + 6];
                    System.arraycopy(head, 0, req, 0, offset);
                    JTools.ShortToBytes2(clusterId, req, offset);
                    offset += 2;
                    JTools.IntToBytes4(clusterUserId, req, offset);
                }
                break;
            // 请求预览视频
            case SUConstant.SUB_REQUEST_START_PREVIEW:
                if(deviceId != null){
                    byte[] p1 = getByteArrayFromStr(deviceId, true);
                    req = new byte[head.length + p1.length];
                    System.arraycopy(head, 0, req, 0, head.length);
                    System.arraycopy(p1, 0, req, offset, p1.length);
                }
                break;
            // 请求停止预览视频 FIXME: 数据??
            case SUConstant.SUB_REQUEST_STOP_PREVIEW:
//                req = new byte[head.length - 4];
//                System.arraycopy(head, 0, req, 0, req.length);
//                req = head;
                if(deviceId != null){
                    offset -= 4;
                    byte[] bDiveiceBytes = getByteArrayFromStr(deviceId, true);
                    if(bDiveiceBytes != null){
                        req = new byte[offset + bDiveiceBytes.length];
                        System.arraycopy(head, 0, req, 0, offset);
                        System.arraycopy(bDiveiceBytes, 0, req, offset, bDiveiceBytes.length);
                    }else{
                        Log.e(TAG, "生成用户名数组失败！");
                    }
                } else if(DEBUG){
                    Log.e(TAG, "无设备信息！");
                }
                break;
            // 云台控制请求
            case SUConstant.SUB_REQUEST_PTZ:
                if (deviceId != null) {
                    byte[] p2 = getByteArrayFromStr(deviceId, true);
                    int[] a = CommonUtils.divideIntData(extra);
                    req = new byte[head.length + p2.length + 4 + 4];
                    System.arraycopy(head, 0, req, 0, head.length);
                    System.arraycopy(p2, 0, req, offset, p2.length);
                    offset += p2.length;
                    intToByte(a[0], req, offset);
                    offset += 4;
                    intToByte(a[1], req, offset);
                }
                break;
            // 播放报警语音
            case SUConstant.SUB_REQUEST_ALARM_PLAYER:
                req = new byte[head.length + 4];
                System.arraycopy(head, 0, req, 0, head.length);
                intToByte(extra, req, offset);
                break;
            // 修改操作..
            case SUConstant.SUB_REQUEST_MODIFY:
                //TODO：未定。。。
                break;
            default:
                break;
        }
        if (req != null) {
            dumpData(req);
        }
        return req;
    }

    /**
     * 创建对回调数据的回应 ---> 系统站场服务器
     * <p>| socket1(4) | socket2(4) | pduType(2)(101) | result(2) |</p>
     */
    public synchronized byte[] createRspToWin(short socket1, short socket2, short ret) {
        byte[] rsp = new byte[8];
        int offset = 0;
        // socket1
        shortToByte(socket1, rsp, offset);
        offset += 2;
        // socket2
        shortToByte(socket2, rsp, offset);
        offset += 2;
        // pduType
        shortToByte(SUConstant.CALLBACK_SUB_PDU_RSP_WIN, rsp, offset);
        offset += 2;
        // result
        shortToByte(ret, rsp, offset);
        return rsp;
    }

    /**
     * 创建对回调数据的回应 ---> 中心服务器
     * <p>| pmid(4) | subType(2) | result(2) |</p>
     */
    public synchronized byte[] createRspToServer(short ret) {
        byte[] rsp = new byte[8];
        int offset = 0;
        // pmid
        intToByte(0, rsp, offset);
        offset += 4;
        // subType
        shortToByte(SUConstant.SUB_SERVER_RSP_ONLINE, rsp, offset);
        offset += 2;
        // result
        shortToByte(ret, rsp, offset);
        return rsp;
    }

    //-------------------------------------------------

    /**
     * 将站场服务器ID数组转换为byte数组
     */
    public byte[] getStationServerIds(String[] servers){
        if (servers == null || servers.length == 0) {
            if (DEBUG) {
                Log.e(TAG, "请求站场数据-参数错误");
            }
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String s : servers) {
            sb.append(s);
            sb.append('\0');
        }
        return getByteArrayFromStr(sb.toString(), false);
    }

    /**
     * 取出byte[]中的一个字符串
     */
    public static String getOneString(int start, byte[] data) {
        int rear = start;
        String str = "";
        while (rear < data.length && data[rear] != '\0') {
            rear++;
        }
        try {
//            str = new String(data, start, rear - start, WPConstant.STRING_UTF8);
            str = new String(data, start, rear - start, WPConstant.STRING_GB2312);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * short数据赋值到byte[]中
     */
    private void shortToByte(short i, byte[] data, int offset) {
        data[offset + 1] = (byte) (i & HALF_SHORT);
        i >>= 8;
        data[offset] = (byte) (i & HALF_SHORT);
    }

    /**
     * int数据赋值到byte[]中
     */
    private void intToByte(int i, byte[] data, int offset) {
        shortToByte((short) (i & HALF_INT), data, offset + 2);
        shortToByte((short) (HALF_INT & (i >> 16)), data, offset);
    }

    /**
     * 将字符串转化为byte数组
     * @param str     字符串
     * @param hasTail 是否需要'\0'结尾
     */
    private synchronized byte[] getByteArrayFromStr(String str, boolean hasTail) {
        if (str == null) {
            return null;
        }
        byte[] ret = null;
        try {
            if (hasTail) {
//                ret = (str + "\0").getBytes(WPConstant.STRING_UTF8);
                ret = (str + "\0").getBytes(WPConstant.STRING_GB2312);
            } else {
//                ret = str.getBytes(WPConstant.STRING_UTF8);
                ret = str.getBytes(WPConstant.STRING_GB2312);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 将字符串转化为byte数组
     * @param str     字符串
     * @param hasTail 是否需要'\0'结尾
     */
    private synchronized byte[] getByteArrayFromStr2(String str, boolean hasTail) {
        if (str == null) {
            return null;
        }
        byte[] ret = null;
        try {
            if (hasTail) {
                ret = (str + "\0").getBytes(WPConstant.STRING_UTF8);
            } else {
                ret = str.getBytes(WPConstant.STRING_UTF8);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ret;
    }



    /**
     * 打印字节数据
     */
    private void dumpData(byte[] data) {
        if (DEBUG) {
            Log.w(TAG, "[----------------data  start----------------]");
            if (data.length > 150) {
                for (int i = 0; i < 150; i++) {
                    Log.i(TAG, "byte data:" + data[i]);
                }
            } else {
                for (byte b : data) {
                    Log.i(TAG, "byte data:" + b);
                }
            }
            Log.w(TAG, "[----------------data  end-----------------]");
        }
    }


    private void dumpDataBBB(byte[] data) {
            Log.i("BBB", "[----------------data  start----------------]");
            if (data.length > 150) {
                for (int i = 0; i < 150; i++) {
                    Log.i("BBB", "byte data:" + data[i]);
                }
            } else {
                for (int i = 0; i < data.length; i++) {
                    Log.i("BBB", "index"+i+"  byte data:" + data[i]);
                }
            }
            Log.i("BBB", "[----------------data  end-----------------]");
    }



}
