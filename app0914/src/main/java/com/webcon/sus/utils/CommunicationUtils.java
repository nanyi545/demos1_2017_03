package com.webcon.sus.utils;

import android.util.Log;

import com.webcon.sus.activities.AlarmDetailsActivityCompat;
import com.webcon.sus.activities.MonitorActivityCompat;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.entity.BaseDevice;
import com.webcon.sus.entity.ImageNode;
import com.webcon.sus.entity.StationNode;
import com.webcon.sus.eventObjects.ErrorEvent;
import com.webcon.sus.eventObjects.ServiceEvent;
import com.webcon.sus.eventObjects.StationEvent;
import com.webcon.wp.utils.JTools;
import com.webcon.wp.utils.NativeInterface;
import com.webcon.wp.utils.WPApplication;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.greenrobot.event.EventBus;

/**
 * 通信库工具类
 * <p>UI层调用这里，在这里调用JNI接口</p>
 * @author m
 */
public class CommunicationUtils extends DataParseUtils{
    private static final String TAG = "Communication";

    private static CommunicationUtils me;
    /** 视频回调接口 */
    private IVideoCallback iVideoCallback;
    private boolean initialized = false;
    /** 保存回复的数据缓存数组 */
    private byte[] rspBuffer = new byte[1024 * 8];

    private CommunicationUtils(){
        // 初始化--设置为null
        iVideoCallback=new IVideoCallback(){ //unregister 不能写成null, iVideoCallback 必须是
            public void onData(int iDataType, byte[] pDataBuffer, int iDataSize){};
        };
    }

    public static CommunicationUtils getInstance(){
        if(me == null){
            synchronized (CommunicationUtils.class){
                if(me == null){
                    me = new CommunicationUtils();
                }
            }
        }
        return me;
    }

    /**
     * 注册视频回调监听
     */
    public void registerVideoCallback(IVideoCallback iVideoCallback){
        this.iVideoCallback = iVideoCallback;
    }

    /**
     * 视频预览回调接口
     * @param iDataType 视频数据类型：头部、数据
     * @param pDataBuffer 视频数据
     * @param iDataSize 视频数据大小
     */
    public void onVideoData(int iDataType, byte[] pDataBuffer, int iDataSize){
        if (null!=pDataBuffer)
        iVideoCallback.onData(iDataType, pDataBuffer, iDataSize);
    }

    /**
     * 反注册视频回调监听
     */
    public void unregisterVideoCallback(){
//------------------------------
//        iVideoCallback = null;
//-----------------------------
        iVideoCallback=new IVideoCallback(){ //unregister 不能写成null, iVideoCallback 必须是
            public void onData(int iDataType, byte[] pDataBuffer, int iDataSize){};
        };
    }

    /**
     * 初始化数据
     */
    public void loadInitialData(){
        if(WPApplication.DEBUG_NO_LOGIN){
            new Thread(){
                @Override
                public void run(){
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    StationEvent event = new StationEvent(StationEvent.STATION_EVENT_INIT);
                    event.msg = 10;
                    EventBus.getDefault().post(event);
                }
            }.start();
        }else{
            new Thread(new InitRunnble()).start();
        }
    }


    /* ******************** 通信接口调用 ******************** */

    /**
     * 初始化通信库
     */
    public int init(){
        if(DEBUG){
            Log.w(TAG, "init");
        }
        Log.i("BBB","before init");
        int ret = NativeInterface.getInstance().init();
        Log.i("BBB","after init"+ret);
        initialized = true;
        return ret;
    }

    public boolean isInitialized(){
        return initialized;
    }

    /**
     * 反初始化通信库
     */
    public int uninit(){
        if(DEBUG){
            Log.w(TAG, "uninit");
        }
        int ret = NativeInterface.getInstance().unInit();
        initialized = false;
        return ret;
    }

    /**
     * 登录实现
     * ##FIXME: netType是啥？(눈_눈)
     */
    public int login(String serverIp, int serverPort, byte[] userName, String userPwd, int netType){
        if(DEBUG){
            Log.w(TAG, "login");
        }
        return NativeInterface.getInstance().login(
                serverIp, serverPort, userName, userPwd, SUConstant.USER_TYPE_PHONE, netType);
    }


    public int logout1(){
        return NativeInterface.getInstance().logout();
    }

    /**
     * 退出登录
     */
    public int logout(){
        if(DEBUG){
            Log.w(TAG, "logout");
        }
        registerProcess(false);
        Log.i("CCC","-----------------after unregister--------------");
        int ret=NativeInterface.getInstance().logout();
        Log.i("CCC","after unregister"+ret);
        return ret;
    }

    /**
     * 对站场的注册处理：注册/反注册
     */
    private void registerProcess(boolean registerState){
        if(DEBUG){
            Log.w(TAG, "registerProcess");
        }
        int registerType = registerState ? SUConstant.REGISTER_CLIENT_ONLINE : SUConstant.REGISTER_CLIENT_OFFLINE;
        if(WPApplication.getInstance().getStationList() == null
                || WPApplication.getInstance().getStationList().size() == 0){
            if(DEBUG){
                Log.e(TAG, "获取列表为空!");
            }
            return;
        }
        for(StationNode node : WPApplication.getInstance().getStationList()){
            boolean judge = (!node.isRegister() && registerState) || (node.isRegister() && !registerState);
            if(node.isOnline() && judge){
                byte[] req = createRequestSys(SUConstant.SUB_REQUEST_REGISTER, node.getId(), null, registerType);
                if(req != null){
                    int ret=NativeInterface.getInstance().sendDataToWinSys(
                            SUConstant.CALLBACK_PDU_DATA_TYPE_REQ,
                            SUConstant.PDU_C_CLI_REQ_WIN, req, req.length);
                    node.setIsRegister(registerState);
                    Log.i("CCC","station:"+node.getServerId()+"   register On/Off:"+(registerState?"on":"off")+"  register result:"+ret);
                    if(DEBUG){
                        Log.w(TAG, "do Register");
                    }
                }else{
                    Log.e(TAG, "生成注册请求包失败！");
                }
            }else if(DEBUG){
                Log.e(TAG, "Register failed");
            }
        }
    }

    public void registerProcessSingle(List<Integer> stationIds){
        if(DEBUG){
            Log.w(TAG, "-----------register when station online---------" );
        }
        for(int i : stationIds){
            if(WPApplication.getInstance().getStationNode(i).isOnline()){
                byte[] req = createRequestSys(SUConstant.SUB_REQUEST_REGISTER, i, null, SUConstant.REGISTER_CLIENT_ONLINE);
                if(req != null){
                    NativeInterface.getInstance().sendDataToWinSys(
                            SUConstant.CALLBACK_PDU_DATA_TYPE_REQ,
                            SUConstant.PDU_C_CLI_REQ_WIN, req, req.length);
                    WPApplication.getInstance().getStationNode(i).setIsRegister(true);
                }
            }else{
                if(DEBUG){
                    Log.e(TAG, "register error!");
                }
                WPApplication.getInstance().getStationNode(i).setIsRegister(false);
            }
        }
    }

    /* ********************与站场服务器的连接******************** */

    /**
     * 请求报警预览抓拍图片
     */
    public ImageNode requestAlarmCapture(AlarmNode mNode){
        if(DEBUG){
            Log.w(TAG, "requestAlarmCapture");
        }
        if(WPApplication.DEBUG_NO_ALARM){
            return null;
        }

        byte[] bigRspBuffer = new byte[1024 * 1024 * 2];
        byte[] req = createRequestSys(
                SUConstant.SUB_REQUEST_ALARM_IMAGE, mNode.getStationId(), mNode.getDeviceName(), mNode.getId());
        if(req != null){
            int ret = NativeInterface.getInstance().sendDataToWinSys2(
                    SUConstant.PDU_C_CLI_REQ_WIN, req, req.length, bigRspBuffer, bigRspBuffer.length);
            Log.i(AlarmDetailsActivityCompat.TAG,"request-return:"+ret);

            if(ret >= 0){
                return parseAlarmCaptureRsp(getNewData(bigRspBuffer, ret));
            } else if (ret==-3){
                EventBus.getDefault().post(new ErrorEvent(ErrorEvent.ERROR_EVENT_TYPE_CAPTURE_OVERTIME));
                return null;
            }
        }
        return null;
    }

    /**
     * 开启布防
     */
    public void openDefence(int stationId) {
        if(DEBUG){
            Log.w(TAG, "openDefence");
        }
        new Thread(new ButtonRunnable(SUConstant.FLAG_STATION_OPENED, stationId)).start();
    }

    /**
     * 关闭布防
     */
    public void closeDefence(int stationId){
        if(DEBUG){
            Log.w(TAG, "closeDefence");
        }
        new Thread(new ButtonRunnable(SUConstant.FLAG_STATION_CLOSED, stationId)).start();
    }


    /***
     * 发送首次语音请求（播放语音喊话之前），获得可以喊话返回之后才能喊话
     * 可以喊话 return （true）1 /不能喊话 return （false）0 / 请求失败 return--2
     */
    public int sendFirstVoiceReq(int stationId, String monitorName) {
        byte[] head = createRequestSys(SUConstant.SUB_1ST_REQ_SPEAKER, stationId, monitorName, 0);
        int ret_length = NativeInterface.getInstance().sendDataToWinSys2(
                SUConstant.PDU_C_CLI_REQ_WIN, head, head.length, rspBuffer, rspBuffer.length);  //通道PDU=12？
        Log.i("BBB","sendFirstVoiceReq--ret"+ret_length);
        if (ret_length >= 0) {
            boolean yn=parsePtzRespond(getNewData(rspBuffer, ret_length));
            int ret_value=yn?1:0;
            return ret_value;
        }
        return 2;
    }


    /**
     * 结束语音请求  喊完话之后
     * @param stationId
     * @param monitorName
     */
    public void stopVoiceReq(int stationId, String monitorName){
        byte[] head = createRequestSys(SUConstant.SUB_LAST_REQ_SPEAKER, stationId, monitorName, 0);
        int ret_length = NativeInterface.getInstance().sendDataToWinSys2(
                SUConstant.PDU_C_CLI_REQ_WIN, head, head.length, rspBuffer, rspBuffer.length);  //通道PDU=12？
        Log.i("BBB", "stop voice ret_length"+ret_length);
//        NativeInterface.getInstance().sendDataToWinSys(                 //  sendDataToWinSys2 or sendDataToWinSys
//                SUConstant.CALLBACK_PDU_DATA_TYPE_REQ,SUConstant.PDU_C_CLI_REQ_WIN,head,head.length);
    }


    /**
     * 播放语音喊话
     */
    public void sendVoice(int stationId, String monitorName, byte[] data, int length){  //包长60?  ---应该96？
        if(DEBUG){
            Log.w(TAG, "sendVoice");
        }
        byte[] head = createRequestSys(SUConstant.SUB_REQUEST_SPEAKER, stationId, monitorName, 0);
        if(head != null){
            byte[] sendData = new byte[head.length + 4 + length];
            //----------------写入head-------？？？？？----------------------
            System.arraycopy(head, 0, sendData, 0, head.length);//----------
            //---------------------------------------------------------------

            int offset = head.length;
            // 数据长度
            JTools.IntToBytes4(length, sendData, offset);
            offset += 4;
            // 语音数据
            System.arraycopy(data, 0, sendData, offset, data.length);
//            int voice_ret=NativeInterface.getInstance().sendDataToWinSys(
//                    SUConstant.CALLBACK_PDU_DATA_TYPE_M, SUConstant.PDU_C_CLI_REQ_WIN, sendData, sendData.length);

            //--------   9-18 according to Zhou, change datatype  -------
            int voice_ret=NativeInterface.getInstance().sendDataToWinSys(
                    SUConstant.CALLBACK_PDU_DATA_TYPE_REQ, SUConstant.PDU_C_CLI_REQ_WIN, sendData, sendData.length);
//            Log.i("BBB","voice return"+voice_ret);
        }
    }

    /**
     * 接收到回调数据后，给站场服务器的回复（基本上是对新报警消息的回复）
     */
    public void respondWin(short socket1, short socket2, short rspRet){
        if(DEBUG){
            Log.w(TAG, "respondWin");
        }
        byte[] rsp = CommunicationUtils.getInstance().createRspToWin(socket1, socket2, rspRet);
        int aa=NativeInterface.getInstance().sendDataToWinSys(SUConstant.CALLBACK_PDU_DATA_TYPE_RSP,
                SUConstant.PDU_B_CLI_RSP_WIN, rsp, rsp.length);
        Log.i(MonitorActivityCompat.TAG,"回复:"+aa);
    }

    /**
     * 接收到回调数据后，回复给中心服务器
     */
    public void respondServer(short ret){
        if(DEBUG){
            Log.w(TAG, "respondServer");
        }
        byte[] rsp = CommunicationUtils.getInstance().createRspToServer(ret);
        NativeInterface.getInstance().sendDataToCenterServ(SUConstant.CALLBACK_PDU_DATA_TYPE_RSP,
                SUConstant.PDU_D_CLI_REQ_SERVER, rsp, rsp.length);
    }

    /**
     * 云台控制  stationId, monitorName, ptzType, moveOrStop
     */
    public void requestPtz(int stationId, String monitorName, int ptzType, boolean ptzState){
        if(DEBUG){
            Log.w(TAG, "requestPtz");
        }
        if(ptzType > 0){
            int ptzCode = CommonUtils.mergeIntData(ptzType,
                    ptzState ? SUConstant.PTZ_STATE_MOVING : SUConstant.PTZ_STATE_STAY);

            byte[] req = createRequestSys(SUConstant.SUB_REQUEST_PTZ, stationId, monitorName, ptzCode);

//            int aaaa=ptzState ? SUConstant.PTZ_STATE_MOVING : SUConstant.PTZ_STATE_STAY;
//            Log.i("BBB","stationId:"+stationId+"---monitorName"+monitorName+"----ptzType"+ptzType+"m/s"+aaaa);

            if(req != null){
                int ret = NativeInterface.getInstance().sendDataToWinSys2(
                        SUConstant.PDU_C_CLI_REQ_WIN, req, req.length, rspBuffer, rspBuffer.length);
//                Log.i("BBB","JNI ret:"+ret);//打印返回
                //TODO ---获取云台控制的解析还有问题，目前忽略解析返回值
// ---------------//返回云台控制是否成功-----
                if(ret >= 0){
                    parsePtzRespond(getNewData(rspBuffer, ret));
                }
            }
        }
    }

    /**
     * 对设备的请求
     */
    public boolean requestPtzDevice(short pdu, byte[] req){
        if(DEBUG){
            Log.w(TAG, "requestPtzDevice: " + pdu);
        }
        int ret = NativeInterface.getInstance().sendDataToWinSys2(pdu, req, req.length, rspBuffer, rspBuffer.length);
        //0成功，<0 失败, 3 正在建立通道 稍后重发
        if (ret>=0){
        return parsePtzRespond(getNewData(rspBuffer, ret));
        }
        return false;
    }

    /**
     * 对设备的请求-->初始化后，停止视频专用
     */
    public void stopVideoDevice_WithoutRetCheck(short pdu, byte[] req){
        if(DEBUG){
            Log.w(TAG, "requestPtzDevice: " + pdu);
        }
        int ret = NativeInterface.getInstance().sendDataToWinSys2(pdu, req, req.length, rspBuffer, rspBuffer.length);
        Log.i("AAA","stop preview:return value:"+ret);
    }


        /**
         * 请求处理一条消息
         * <br>·若请求成功，则返回0
         * <br>·若请求失败，-1.连接错误，-2.已被处理
         * @param mNode 待处理报警消息
         */
    public boolean requestSolveAlarm(AlarmNode mNode){
        if(DEBUG){
            Log.w(TAG, "requestSolveAlarm");
        }
        if(WPApplication.DEBUG_NO_ALARM){
            return true;
        }

        byte[] req = createRequestSys(
                SUConstant.SUB_REQUEST_PROCESS_ALARM, mNode.getStationId(), mNode.getDeviceName(), mNode.getId());

        if(req != null){
            int ret = NativeInterface.getInstance().sendDataToWinSys2(
                    SUConstant.PDU_C_CLI_REQ_WIN, req, req.length, rspBuffer, rspBuffer.length);
            Log.i(MonitorActivityCompat.TAG,"return from  server:"+ret);

            if(ret >= 0){
                return parsePtzRespond(getNewData(rspBuffer, ret));
            }
        }
        return false;  //return true to allow alarm solved TODO  RETURN FALSE----after server OK
    }

    /**
     * 请求DB修改用户昵称
     */
    public boolean requestModifyUserName(String userId, String newNickname){
        if(DEBUG){
            Log.w(TAG, "requestModifyUserName");
        }
        byte[] req = createRequestBDModifyUserName(userId, newNickname);
        if(req != null && req.length > 0){
            int ret = NativeInterface.getInstance().getDBData(
                    SUConstant.PDU_A_CLI_REQ_DB, req, req.length, rspBuffer, rspBuffer.length);
            if(DEBUG){
                Log.w(TAG, "result:" + ret);
            }
            if(ret >= 0){
                return parseDBRsp(getNewData(rspBuffer, ret));
            }
        }
        return false;
    }

    /**
     * 请求DB修改用户密码
     */
    public boolean requestModifyUserPasswd(String userId, String oldPass, String newPass){
        if(DEBUG){
            Log.w(TAG, "requestModifyUserPasswd");
        }
        byte[] req = createRequestBDModifyPasswd(userId, oldPass, newPass);
        if(req != null && req.length > 0){
            int ret = NativeInterface.getInstance().getDBData(
                    SUConstant.PDU_A_CLI_REQ_DB, req, req.length, rspBuffer, rspBuffer.length);
            if(DEBUG){
                Log.w(TAG, "result:" + ret);
            }
            if(ret >= 0){
                return parseDBRsp(getNewData(rspBuffer, ret));
            }
        }
        return false;
    }

    /**
     * 请求修改站场名称 （未实现）
     */
    public boolean requestModifyStationName(byte[] req){
        if(DEBUG){
            Log.w(TAG, "requestModifyStationName");
        }
        if(req != null && req.length > 0){
            int ret = NativeInterface.getInstance().sendDataToWinSys2(
                    SUConstant.PDU_C_CLI_REQ_WIN, req, req.length, rspBuffer, rspBuffer.length);
            if(ret >= 0){
                return parsePtzRespond(getNewData(rspBuffer, ret));
            }
        }
        return false;
    }

    /**
     * 请求刷新站场数据，包括设备信息、报警消息
     * @param stationId 站场Id
     */
    public synchronized void requestRefreshStationInformation(int stationId){
        if(DEBUG){
            Log.w(TAG, "---requestRefreshStationInformation----");
        }
        int ret;
        /* 获取设备列表 */
        if(DEBUG){
            Log.w(TAG, "获取设备列表>>>>>>>>>>>>>>>>>>>> ");
        }
        byte[] reqDevices = createRequestSys(SUConstant.SUB_REQUEST_GET_DEVICES, stationId, null, 0);
        if(reqDevices == null){
            if(DEBUG){
                Log.e(TAG, "req is null");
            }
            return;
        }
        ret = NativeInterface.getInstance().sendDataToWinSys2(
                SUConstant.PDU_C_CLI_REQ_WIN,
                reqDevices, reqDevices.length,
                rspBuffer, rspBuffer.length);
        if(ret < 0){
            logError(ret, 1300);
            return;
        }
        if(DEBUG){
            Log.w(TAG, "ret:" + ret);
        }
        //#解析结果
        if(!parseInit(getNewData(rspBuffer, ret), WPApplication.getInstance().getStationNode(stationId))){
            if(DEBUG){
                Log.e(TAG, "解析站场设备错误！");
            }
        }

        /* 获取报警列表 */
        if(DEBUG){
            Log.w(TAG, "获取报警列表>>>>>>>>>>>>>>>>>>>> ");
        }
        byte[] reqAlarm = createRequestSys(SUConstant.SUB_REQUEST_GET_ALARMS, stationId, null, 0);
        if(reqAlarm == null){
            if(DEBUG){
                Log.e(TAG, "req is null");
            }
            return;
        }
        ret = NativeInterface.getInstance().sendDataToWinSys2(
                SUConstant.PDU_C_CLI_REQ_WIN,
                reqAlarm, reqAlarm.length,
                rspBuffer, rspBuffer.length);
        Log.i(MonitorActivityCompat.TAG,"SUB_REQUEST_GET_ALARMS  10029--return:"+ret);

        if(ret < 0){
            logError(ret, 1400);
            return;
        }
        //#解析结果
        if(!parseInit(getNewData(rspBuffer, ret), WPApplication.getInstance().getStationNode(stationId))){
            if(DEBUG){
                Log.e(TAG, "解析报警消息错误！");
            }
        }
    }

    /**
     * 刷新全部站场列表的数据：①初始化时；②站场离线状态改变时
     * 然后重新生成全局报警消息列表
     * 刷新数据后，还需发布事件，刷新显示
     */
    public void refreshAllStationInformation(List<Integer> stationIds){
//        if(DEBUG){
//            Log.w(TAG, "--------刷新站场列表的数据----------");
//        }

        Log.i(MonitorActivityCompat.TAG, "SUB_REQUEST_GET_ALARMS--------刷新站场列表的数据----------");
        if(!WPApplication.getInstance().getAccessable()){
            return;
        }
        if(stationIds == null){
            for(StationNode node : WPApplication.getInstance().getStationList()){
                Log.i(MonitorActivityCompat.TAG, "SUB_REQUEST_GET_ALARMS--------1 station node----------");
                node.refreshInformation();
            }
        }else{
            for(int i : stationIds){
                WPApplication.getInstance().getStationNode(i).refreshInformation();
            }
        }

        if(DEBUG){
            Log.w(TAG, "--------刷新站场列表的数据 over----------");
        }
        // 生成全局报警消息列表
        WPApplication.getInstance().rebuildAlarmList();
        // 注意设备名和站场id的映射表
    }

    public void refreshAllStationInformationTest(List<Integer> stationIds){
        Log.i(MonitorActivityCompat.TAG, "SUB_REQUEST_GET_ALARMS--------刷新站场列表的数据----------");
        if(!WPApplication.getInstance().getAccessable()){
            return;
        }
        if(stationIds == null){
            for(StationNode node : WPApplication.getInstance().getStationList()){
                Log.i(MonitorActivityCompat.TAG, "SUB_REQUEST_GET_ALARMS--------1 station node----------");
//                node.refreshInformation();
//                node.refreshInformation(true);
            }
        }else{
            for(int i : stationIds){
                WPApplication.getInstance().getStationNode(i).refreshInformation();
            }
        }

        if(DEBUG){
            Log.w(TAG, "--------刷新站场列表的数据 over----------");
        }
        // 生成全局报警消息列表
        WPApplication.getInstance().rebuildAlarmList();
        // 注意设备名和站场id的映射表
    }


    /* ********************** 其他 ****************** */
    /**
     * 释放工作
     * <p>反初始化、清理内存等。。。</p>
     */
    public void release(){

    }


    public byte[] getNewData(byte[] data, int len){
        if(len == 0){
            len = data.length;
        }
//        Log.i("BBB","PTZcontrol return length"+len);
        byte[] newData = new byte[len];
        System.arraycopy(data, 0, newData, 0, len);
        return newData;
    }

    private String[] getServerIds(){
        if(!WPApplication.getInstance().getAccessable()){
            return null;
        }
        String[] servers = new String[WPApplication.getInstance().getStationList().size()];
        int i = 0;
        // NOTE：此处不能很好地处理进程退出时数据读取的问题
        for(StationNode n : WPApplication.getInstance().getStationList()){
            servers[i++] = n.getServerId();
        }
        return servers;
    }

    /* ***************************************** Thread ****************************** */
    /**
     * 布防按钮
     */
    private class ButtonRunnable implements Runnable {

        private int ss = 0;
        private int id = 0;

        public ButtonRunnable(int ss, int id){
            this.ss = ss;  // SS ：布防开启还是关闭
//            // 站场状态 --登录后
//            public static final int FLAG_STATION_CLOSED     = 0x100;  // 256 to close defense
//            public static final int FLAG_STATION_OPENED     = 0x101;  // 257 to open defense
//            public static final int FLAG_STATION_ALARM      = 0x102;
            this.id = id;  // ID station id
        }

        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 测试>>跳过通信
            if(WPApplication.DEBUG_NO_PTZ){
                //test----------------
                short pdu = (ss == SUConstant.FLAG_STATION_OPENED) ?
                        SUConstant.SUB_REQUEST_OPEN_DEFENCE : SUConstant.SUB_REQUEST_STOP_DEFENCE;
//                Log.i("DEFENSE","pdu="+pdu);
                //test------------------

                WPApplication.getInstance().getStationNode(id).setState(ss);
                // 通知站场状态改变事件
                StationEvent event= new StationEvent(StationEvent.STATION_EVENT_REFRESH);
                event.stationId = id;
                event.msg = ss;
                EventBus.getDefault().post(event);
                return;
            }

            // 布防功能未完成 >> 跳过通信
            if(WPApplication.DEFENSE_NOT_READY){
                //test----------------
                short pdu = (ss == SUConstant.FLAG_STATION_OPENED) ?
                        SUConstant.SUB_REQUEST_OPEN_DEFENCE : SUConstant.SUB_REQUEST_STOP_DEFENCE;
//                Log.i("DEFENSE","pdu="+pdu);
                //test------------------

                WPApplication.getInstance().getStationNode(id).setState(ss);
                // 通知站场状态改变事件
                StationEvent event= new StationEvent(StationEvent.STATION_EVENT_REFRESH);
                event.stationId = id;
                event.msg = ss;
                EventBus.getDefault().post(event);
                return;
            }

            // 请求与处理
            boolean ret = false;
            short pdu = (ss == SUConstant.FLAG_STATION_OPENED) ?
                    SUConstant.SUB_REQUEST_OPEN_DEFENCE : SUConstant.SUB_REQUEST_STOP_DEFENCE;
//            Log.i("DEFENSE","pdu="+pdu);
            byte[] req = createRequestSys(pdu, id, null, 0);

            try{
                if(req != null){
                    // 发送请求 开启/关闭布防！
                    int result = NativeInterface.getInstance().sendDataToWinSys2(
                            SUConstant.PDU_C_CLI_REQ_WIN, req, req.length, rspBuffer, rspBuffer.length);  // 通道pdu 12？
//                    Log.i("DEFENSE","ss:"+ss+"----short ss:"+(short) ss);
//                    Log.i("DEFENSE",""+result);
                    if (result<0) ret=false;  // <0 失败
                    else ret = parsePtzRespond(getNewData(rspBuffer, result));// >0 成功
                }
            }catch(UnsatisfiedLinkError e){
                e.printStackTrace();
            }

            if(ret){
                WPApplication.getInstance().getStationNode(id).setState(ss);
                // 通知站场状态改变事件
                StationEvent event= new StationEvent(StationEvent.STATION_EVENT_REFRESH);
                event.stationId = id;
                event.msg = ss;
                EventBus.getDefault().post(event);
            }else{
                ss = (ss == SUConstant.FLAG_STATION_OPENED) ?
                        SUConstant.FLAG_STATION_CLOSED : SUConstant.FLAG_STATION_OPENED;
                WPApplication.getInstance().getStationNode(id).setState(ss);
                // 通知站场状态改变事件
                StationEvent event= new StationEvent(StationEvent.STATION_EVENT_REFRESH);
                event.stationId = id;
                event.msg = ss;
                EventBus.getDefault().post(event);
                // 错误消息
                CommonUtils.sendErrorEvent(pdu, 0);
            }
        }
    }

    private void logError(int e, int t){
        Log.e(TAG, "error:" + t + "/" + e);
        if(DEBUG){
            Log.e(TAG, "error:" + t + "/" + e);
        }
    }




    private void stopPreview(){
        List<StationNode> mStationList=null;
        while(mStationList==null) {     // loop until the station information is obtained from internet!
            try {
                Log.i(MonitorActivityCompat.TAG,"null?"+(mStationList==null)+"waiting... in process");
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
//                            new Thread(new StopPrevRunnable(stationNode.getId(),currentDevice.getName())).start();
                            new StopPrevRunnable(stationNode.getId(),currentDevice.getName()).run();
                        }
                    }
                }
            }
        }
    }

    /**
     * 请求线程: 停止预览
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

            CommunicationUtils.getInstance().stopVideoDevice_WithoutRetCheck(SUConstant.PDU_C_CLI_REQ_WIN, req);

        }
    }


    public void startInit(ExecutorService threadPool){
        final Future future=threadPool.submit(new InitRunnble());
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {// future.get needs to be NOT in main thread.
                    future.get(8000, TimeUnit.MILLISECONDS);   // stop preview failed in 5 seconds --> call UI to hint"timeout"
                } catch (TimeoutException e) {
                    Log.i("communicationInit","updateTimeOut");
                    initServiceBusy();
                } catch (InterruptedException e) {
                    initErrorService();
                    Log.i("communicationInit","InterruptedException");
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    initErrorService();
                    Log.i("communicationInit","ExecutionException");
                    e.printStackTrace();
                }

            }
        }).start();
    }


    /****************************************
     * 初始化数据线程：
     */
    private class InitRunnble implements Runnable{

        public InitRunnble(){

        }

        @Override
        public void run(){
            Log.i("CCC","init runnable run in "+Thread.currentThread().getName());
            int ret;
            byte[] retData = new byte[1024 * 4];

//            if(DEBUG){
//                Log.w(TAG, "--------------------------------------------");
//                Log.i(TAG, "①>>>>init station list");
//            }
            // 向数据库请求站场列表
            byte[] req1 = createRequestBDGetStations();   // 20106
            if(req1 == null){
                if(DEBUG){
                    Log.e(TAG, "req is null");
                }
                return;
            }
            ret = NativeInterface.getInstance().getDBData(
                    SUConstant.PDU_A_CLI_REQ_DB,
                    req1, req1.length,
                    retData, retData.length);
            Log.i("Communication","getDBData"+ret); // -------106
            Log.i("CCC","getDBData"+ret); // -------106

            if(ret < 0){
                Log.i("Communication","111----222"+ret);
                logError(ret, 1100);
                initErrorService();
                return;
            }

            if(ret < 20){  // repeated refresh action will give ret==16
                initServiceBusy();
                return;
            }

            if(DEBUG){
                Log.i(TAG, "ret:" + ret);
            }
            // parseInit --> 解析场站列表, station -> null
            if(!parseInit(getNewData(retData, ret), null)){   // --20107--
                if(DEBUG){
                    Log.e(TAG, "解析站场列表错误！");
                }
                Log.i(TAG, "解析站场列表错误！");
                initErrorService();
                EventBus.getDefault().post(new ServiceEvent(ServiceEvent.SERVICE_EVENT_INIT_OVER));
                return;
            }

            if(DEBUG){
                Log.w(TAG, "--------------------------------------------");
                Log.i(TAG, "②>>>>init station state");
            }
            Log.i(TAG, "②>>>>init station state");
            // 向服务器请求站场状态
            String[] serverIds = getServerIds();
            if(serverIds == null){
                if(DEBUG){
                    Log.e(TAG, "create serverIds is fail");
                }
                return;
            }else {
                for(String s : serverIds){
                    Log.i(TAG,"stationId:" + s);
                }
            }
            byte[] req2 = createRequestServerStations(serverIds);
            if(req2 == null){
                if(DEBUG){
                    Log.e(TAG, "req is null");
                }
                return;
            }
            ret = NativeInterface.getInstance().sendDataToCenterServ2(
                    SUConstant.PDU_D_CLI_REQ_SERVER,
                    req2, req2.length,
                    retData, retData.length);
            if(ret < 0){
                logError(ret, 1200);
                return;
            }
            if(DEBUG){
                Log.i(TAG, "ret:" + ret);
            }
            if(!parseServerStationsData(getNewData(retData, ret), null)){
                if(DEBUG){
                    Log.e(TAG, "解析站场状态错误！");
                }
                initErrorService();
                Log.i(TAG, "解析站场状态错误！");
                EventBus.getDefault().post(new ServiceEvent(ServiceEvent.SERVICE_EVENT_INIT_OVER));
                return;
            }
            // 发送UI通知>>刷新站场列表
            refreshInitStationList(ret, true);

            if(DEBUG){
                Log.w(TAG, "--------------------------------------------");
                Log.i(TAG, "③>>>>register all stations");
            }
            // 向站场请求注册
            registerProcess(true);

            if(DEBUG){
                Log.w(TAG, "--------------------------------------------");
                Log.i(TAG, "④>>>>request all stations");
            }
            // 向站场请求数据：设备列表、报警列表
            refreshAllStationInformation(null);  //
            // 初始化完成
            ServiceEvent sevent = new ServiceEvent(ServiceEvent.SERVICE_EVENT_INIT_OVER);
            sevent.msg = 0;
            EventBus.getDefault().post(sevent);
            // 发送请求，刷新，已经加载完设备信息和报警信息
            AlarmMsgManager.getInstance().transmitCenter(AlarmMsgManager.ALARM_MANAGE_INIT, null);
            if(DEBUG){
                Log.w(TAG, "----------------------init thread over --------------");
            }

            //            * stop all previews-->  this is needed to prevent spurious video packages...
            stopPreview();
//            ExecutorService threadPool = Executors.newSingleThreadExecutor();
//            Future future=threadPool.submit(new Runnable(){
//                @Override
//                public void run() {
//                    stopPreview();
//                }
//            });
//
//            try {
//                future.get(5000, TimeUnit.MILLISECONDS);   // stop preview failed in 5 seconds --> call UI to hint"timeout"
//            } catch (TimeoutException e) {
//                initServiceBusy();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }


        }
    }

    private void initErrorService(){
        EventBus.getDefault().post(new ServiceEvent(ServiceEvent.SERVICE_EVENT_INIT_ERROR));
    }
    private void initServiceBusy(){
        EventBus.getDefault().post(new ServiceEvent(ServiceEvent.SERVICE_EVENT_INIT_BUSY));
    }


    /** 发送通知给UI  刷新站场列表 */
    public void refreshInitStationList(int ret, boolean reload){
        if(DEBUG){
            Log.i(TAG, "--------refreshInitStationList--------");
        }
        StationEvent event = new StationEvent(StationEvent.STATION_EVENT_INIT);
        event.msg = ret;
        event.reload = reload;
        EventBus.getDefault().post(event);
    }

}
