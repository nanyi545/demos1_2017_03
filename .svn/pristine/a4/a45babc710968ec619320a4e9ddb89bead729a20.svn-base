package com.webcon.wp.utils;


import android.util.Log;

/**
 * 通信库回调的控制包的存放缓冲队列
 * @author Vieboo
 *
 */
public class CallbackPackageDataArray {
	
	private byte[] dataArray;
	private int front;	//前
	private int rear;	//后
	private int inLast; //存储剩余
	private int outLast; //读取剩余
	private int arraySize;
	
	public CallbackPackageDataArray(){
		arraySize = 1024 * 512;	//0.5M
		dataArray = new byte[arraySize];
		front = 0;
		rear = 0;
		inLast = arraySize - front;
		outLast = arraySize - rear;
	}
	
	public boolean enQueue(byte[] data){
		if(front - rear < 0 && (front + data.length) >= rear){
//			Log.i("VideoDataBuffer", "-----------存储回调控制包丢失-------------");
			return false;
		}else if((front - rear) > 0 && data.length > inLast && 
				(data.length - inLast) >= rear){
//			Log.i("VideoDataBuffer", "-----------存储回调控制包丢失2-------------");
			return false;
		}else{
			if(data.length > inLast){
				System.arraycopy(data, 0, dataArray, front, inLast);
				System.arraycopy(data, inLast, dataArray, 0, data.length - inLast);
				front = data.length - inLast;
			}else if(data.length == inLast){
				System.arraycopy(data, 0, dataArray, front, data.length);
				front = 0;
			}else{
				System.arraycopy(data, 0, dataArray, front, data.length);
				front += data.length;
			}
			inLast = arraySize - front;
			return true;
		}
	}
	
	public byte[] deQueue(){
		byte[] lengthData = new byte[4];
		if(rear == front || ((front - rear) > 0 && (front - rear) < lengthData.length) || 
				((front - rear) < 0 && (outLast + front) < lengthData.length)){
			Log.i("VideoDataBuffer", "-----------读取回调控制包为空----------");
			return null;
		}else{
			deQueueJudge(lengthData);
			int resolveDataLen = JTools.Bytes4ToInt(lengthData, 0);
			byte[] data = new byte[resolveDataLen];
			deQueueJudge(data);
			return data;
		}
	}
	
	private void deQueueJudge(byte[] deData){
		if(outLast == deData.length){
			System.arraycopy(dataArray, rear, deData, 0, deData.length);
			rear = 0;
		}else if(outLast < deData.length){
			System.arraycopy(dataArray, rear, deData, 0, outLast);
			System.arraycopy(dataArray, 0, deData, outLast, deData.length - outLast);
			rear = deData.length - outLast;
		}else{
			System.arraycopy(dataArray, rear, deData, 0, deData.length);
			rear += deData.length;
		}
		outLast = arraySize - rear;
	}
	
	public boolean isNotNull(){
        return !(front == rear);
	}
	
}
