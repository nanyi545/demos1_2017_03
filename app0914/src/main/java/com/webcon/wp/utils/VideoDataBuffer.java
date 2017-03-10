package com.webcon.wp.utils;

/**
 * 视频缓冲队列
 * @author Vieboo
 *
 */
public class VideoDataBuffer {
	
	private static VideoDataBuffer videoDataBuffer;
	
	private byte[] dataArray;
	private int front;	//前
	private int rear;	//后
	private int inLast; //存储剩余
	private int outLast; //读取剩余
	private int arraySize;
	private int readSize;
	
	private VideoDataBuffer(){
		
		arraySize = 1024 * 1024 * 2;	//2M
		dataArray = new byte[arraySize];
		front = 0;
		rear = 0;
		readSize = 1024;
		inLast = arraySize - front;
		outLast = arraySize - rear;
	}
	
	public static VideoDataBuffer getVideoDataBuffer(){
		if(videoDataBuffer == null){
			synchronized (VideoDataBuffer.class) {
				if(videoDataBuffer == null){
					videoDataBuffer = new VideoDataBuffer();
				}
			}
		}
		return videoDataBuffer;
	}
	
	
	public boolean enQueue(byte[] data){
		if(front - rear < 0 && (front + data.length) >= rear){
//			Log.i("VideoDataBuffer", "-----------存储回调媒体包丢失-------------");
			return false;
		}else if((front - rear) > 0 && data.length > inLast && 
				(data.length - inLast) >= rear){
//			Log.i("VideoDataBuffer", "-----------存储回调媒体包丢失2-------------");
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
	
	
	public boolean enQueue(byte[] data, int off){
		if(front - rear < 0 && (front + data.length - off) >= rear){
//			Log.i("VideoDataBuffer", "-----------存储回调媒体包丢失-------------");
			return false;
		}else if((front - rear) > 0 && (data.length - off) > inLast && 
				(data.length - off - inLast) >= rear){
//			Log.i("VideoDataBuffer", "-----------存储回调媒体包丢失2-------------");
			return false;
		}else{
			if((data.length - off) > inLast){
				System.arraycopy(data, off, dataArray, front, inLast);
				System.arraycopy(data, inLast + off, dataArray, 0, data.length - off - inLast);
				front = data.length - off - inLast;
			}else if((data.length - off) == inLast){
				System.arraycopy(data, off, dataArray, front, data.length - off);
				front = 0;
			}else{
				System.arraycopy(data, off, dataArray, front, data.length - off);
				front += (data.length - off);
			}
			inLast = arraySize - front;
			return true;
		}
	}
	
	
	
	
	public byte[] deQueue(){
		byte[] data = new byte[readSize];
		if(rear == front || ((front - rear) > 0 && (front - rear) < data.length) || 
				((front - rear) < 0 && (outLast + front) < data.length)){
//			Log.i("VideoDataBuffer", "-----------读取回调媒体包为空----------");
			return null;
		}else{
			if(outLast == data.length){
				System.arraycopy(dataArray, rear, data, 0, data.length);
				rear = 0;
			}else if(outLast < data.length){
				System.arraycopy(dataArray, rear, data, 0, outLast);
				System.arraycopy(dataArray, 0, data, outLast, data.length - outLast);
				rear = data.length - outLast;
			}else{
				System.arraycopy(dataArray, rear, data, 0, data.length);
				rear += data.length;
			}
			outLast = arraySize - rear;
			return data;
		}
	}
	
	public byte[] deQueueOfLength(){
		byte[] lengthData = new byte[4];
		if(rear == front || ((front - rear) > 0 && (front - rear) < lengthData.length) || 
				((front - rear) < 0 && (outLast + front) < lengthData.length)){
//			Log.i("VideoDataBuffer", "-----------读取回调媒体包为空----------");
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
