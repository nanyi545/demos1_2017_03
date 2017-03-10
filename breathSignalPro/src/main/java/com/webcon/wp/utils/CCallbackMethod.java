package com.webcon.wp.utils;

import android.content.Context;
import android.content.ContextWrapper;

public class CCallbackMethod extends ContextWrapper {
	
	public CCallbackMethod(Context base){
		super(base);
	}

	public int call(){
		System.out.println("------call");
		return 50;
	}
	
//	public String callHello(byte[] databuf)
	//public String callHello(String databuf)
//	public int callHello(String databuf)
	public void callHello()
	{
	//	msgType dataType username  flag  pdu datalen data
		short msgType,dataType,pdu ;
		String username;
		long flag,  datalen;
		byte[] data;
				
		System.out.println("------callHello ++++ 9999  len= " + car_infoBuf.length );
		/*
		System.out.println("------callHello ++++ " + "msgtype[" + msgType + "] data[" 
		+ dataType + "] username[" + username + "] datalen[" + datalen + "] pdu[" 
				+ pdu + "] flag[" + flag +"]" );
				*/
	
		//return 0;
	
	}
	/*
	public static String callHello(String outstr)
	{
		System.out.println("------callHello ++++ " + outstr);
		return outstr;
	}
	
	public static String callHello(int msgType,int dataType,String username,
			byte[] data,int datalen,int pdu,int flag)
	{
		System.out.println("------callHello ++++ " + "msgtype[" + msgType + "] data[" 
	+ dataType + "] username[" + username + "] d.len[" + data.length + "] datalen[" 
	+ datalen + "] pdu[" + pdu + "] flag[" + flag +"]" );	
		return "";
	}
		
	public static String callHello(byte[] outstr)
	{
		System.out.println("------callHello ++++ outstr[" + outstr.length + "]");
		return "";
	}
	*/
	/*
	public static String callHello(byte[] data)
	{
		System.out.println("------callHello ++++ len[" + data.length + "]");	
		return "";
	}
	*/
	
	public byte[] car_infoBuf;


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
	 * @return 0成功 <0 失败
	 */
	public native int doWork(String javaPath, String javaMethod, String bufName);








}
