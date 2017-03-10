package com.webcon.wp.utils;

/**
 * 通信库的JNI接口类
 * @author Vieboo
 *
 */
public class NativeInterface {
	
	private static NativeInterface nativeInterface;

    static{
        System.loadLibrary("socks5");
        System.loadLibrary("jrtp");
        System.loadLibrary("CommBase");
        System.loadLibrary("NetCtrl");
        System.loadLibrary("FCNetAC");
        System.loadLibrary("myjni");
    }

    private NativeInterface(){

    }
	
	public static NativeInterface getInstance(){
		if(nativeInterface == null){
			synchronized (NativeInterface.class) {
				if(nativeInterface == null){
					nativeInterface = new NativeInterface();
				}
			}
		}
		return nativeInterface;
	}
	
	/**
	 * 初始化
	 * @return	0成功  	<0 失败
	 */
	public native int init();
	
	/**
	 * 反初始化
	 * @return	0成功		<0失败
	 */
	public native int unInit();

    /**
     * 登  录
     * note:在init成功之后才能调用
     * @param serverIp      服务器Ip
     * @param serverPort    服务器port
     * @param userName      用户账号
     * @param userPwd       用户密码
     * @param userType      用户类型  0-普通；1-手机; 2-pad
     * @param netType       网络类型  1
     * @return              0成功，<0 失败
     * -1 初始化未成功;
     *  1 端口占用;
     * -4 获取默认会议失败;
     * -5 加入默认会议失败;
     * -7 登陆超时；
     *  9 是登陆时错误返回值是1;
     *    其它是db返回错误值    -4004 用户号码不存在	    -4005 密码错误
     */
    public native int login(String serverIp, int serverPort, byte[] userName, String userPwd, int userType, int netType);

    /**
     * 登  出
     * @return ??
     */
    public native int logout();

	/**
	 * 发往数据库的数据、获取数据库数据
     * note:在FCNetACLogin成功之后才能调用
	 * @param reqPdu		发送的数据标识
	 * @param inData		发送的数据信息
	 * @param inDataLen		发送数据的长度
	 * @param outData		接收到的数据信息
	 * @param outDataLen	接收数组的长度
	 * @return	接收到的数据长度		<0 失败
	 */
	public native int getDBData(short reqPdu, byte[] inData, int inDataLen, byte[] outData, int outDataLen);

    /**
     * 发送数据（对windows端的系统服务软件的请求）
     * note:在FCNetACLogin成功之后才能调用
     * @param dataType  发送方式(1: req 控制包(主动请求) 2: rsp 控制包(对服务器的回复) 3:rsp 音视频包
     * @param reqPdu    发送请求的命令
     * @param inData    发送的数据信息
     * @param inLen     发送数据的长度
     * @return          0成功，<0 失败, 3 正在建立通道 稍后重发
     */
    public native int sendDataToWinSys(short dataType, short reqPdu, byte[] inData, int inLen);

    /**
     * 发送数据 同步
     * note:在FCNetACLogin成功之后才能调用
     * @param reqPdu    发送请求的命令
     * @param inData    发送的数据信息
     * @param inLen     发送数据的长度
     * @param outData   接收到的数据信息    ----的位置（缓冲区）
     * @param outLen    存放接收到的数据长度----缓冲区大小
     * @return          0成功，<0 失败, 3 正在建立通道 稍后重发   --otherwise返回字节数组长度
     */
    public native int sendDataToWinSys2(short reqPdu, byte[] inData, int inLen, byte[] outData, int outLen);

    /**
     * 发送数据（对中心服务器的请求）
     * note:在FCNetACLogin成功之后才能调用
     * @param dataType  发送方式(1: req 控制包 2: rsp 控制包， 3: 音视频包
     * @param reqPdu    发送请求的命令
     * @param inData    发送的数据信息
     * @param inLen     发送数据的长度
     * @return          0成功，<0 失败, 3 正在建立通道 稍后重发
     */
    public native int sendDataToCenterServ(short dataType, short reqPdu, byte[] inData, int inLen);

    /**
     * 发送数据 同步
     * note:在FCNetACLogin成功之后才能调用
     * @param reqPdu    发送请求的命令
     * @param inData    发送的数据信息
     * @param inLen     发送数据的长度
     * @param outData   接收到的数据信息
     * @param outLen    存放接收到的最大数据长度
     * @return          0成功，<0 失败, 3 正在建立通道 稍后重发
     */
    public native int sendDataToCenterServ2(short reqPdu, byte[] inData, int inLen, byte[] outData, int outLen);

}
