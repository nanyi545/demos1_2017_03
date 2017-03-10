package com.webcon.sus.media;

import com.webcon.wp.utils.JTools;

/**
 * 采集视频数据缓冲区
 * @author Vieboo
 *
 */
public class SelfVideoDataBuffer {

	private static SelfVideoDataBuffer videoDataBuffer;
	
	private byte[] dataArray;
	private int front;	//前
	private int rear;	//后
	private int inLast; //存储剩余
	private int outLast; //读取剩余
	private int arraySize;
	
	private SelfVideoDataBuffer(){
		
		arraySize = 1024 * 1024 * 2;	//2M
		dataArray = new byte[arraySize];
		front = 0;
		rear = 0;
		inLast = arraySize - front;
		outLast = arraySize - rear;
	}
	
	public static SelfVideoDataBuffer getVideoDataBuffer(){
		if(videoDataBuffer == null){
			synchronized (SelfVideoDataBuffer.class) {
				if(videoDataBuffer == null){
					videoDataBuffer = new SelfVideoDataBuffer();
				}
			}
		}
		return videoDataBuffer;
	}
	
	
	public boolean enQueue(byte[] data){
		if(front - rear < 0 && (front + data.length) >= rear){
			return false;
		}else if((front - rear) > 0 && data.length > inLast && 
				(data.length - inLast) >= rear){
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
	
	
	public byte[] deQueueOfLength(){
		byte[] lengthData = new byte[4];
		if(rear == front || ((front - rear) > 0 && (front - rear) < lengthData.length) || 
				((front - rear) < 0 && (outLast + front) < lengthData.length)){
			return null;
		}else{
			deQueueJudge(lengthData);
			int resolveDataLen = JTools.Bytes4ToInt(lengthData, 0);
			byte[] data = new byte[resolveDataLen];				
			deQueueJudge(data);
			byte[] finalData = new byte[resolveDataLen + 4];
			JTools.IntToBytes4(resolveDataLen, finalData, 0);
			System.arraycopy(data, 0, finalData, 4, data.length);
			return finalData;
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
	
	
	
	public void clearData(){
		front = 0;
		rear = 0;
	}
	
	
	
	/**
	 * 清除掉接收到但是没有播放的数据（播放时surfaceHolder进入onDestroy状态）
	 * 设置rear = from;
	 */
	public void clearOldData(){
		rear = front;
	}
}
