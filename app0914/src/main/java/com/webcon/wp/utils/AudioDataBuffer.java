package com.webcon.wp.utils;

/**
 * 音频缓冲队列
 * @author Vieboo
 *
 */
public class AudioDataBuffer {
	
//	private byte[] dataArray;
//	private int front;	//前
//	private int rear;	//后
//	private int last; //剩余
//	private int arraySize;
//	private int readSize;
	
	private static AudioDataBuffer videoDataBuffer;
	
	private byte[] dataArray;
	private int front;	//前
	private int rear;	//后
	private int inLast; //存储剩余
	private int outLast; //读取剩余
	private int arraySize;
	private int readSize;
	
	private AudioDataBuffer(){
//		arraySize = 1024 * 1024 * 2;	//2M
//		dataArray = new byte[arraySize];
//		front = 0;
//		rear = 0;
//		readSize = 60;
////		readSize = 320;
//		last = arraySize - front;
		
		arraySize = 1024 * 1024 * 2;	//2M
		dataArray = new byte[arraySize];
		front = 0;
		rear = 0;
		readSize = 320;
		inLast = arraySize - front;
		outLast = arraySize - rear;
	}
	
	public static AudioDataBuffer getAudioDataBuffer(){
		if(videoDataBuffer == null){
			synchronized (AudioDataBuffer.class) {
				if(videoDataBuffer == null){
					videoDataBuffer = new AudioDataBuffer();
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
	
//	public boolean enQueue(byte[] data){
//		if(front - rear < 0 && (front + data.length) >= rear){
////			Log.i("VideoDataBuffer", "-----------存储音频数据丢失------------->" + front);
//			return false;
//		}else if(front - rear > 0 && data.length > last && 
//				(data.length - last) >= rear){
////			Log.i("VideoDataBuffer", "-----------存储音频数据丢失2------------->" + front);
//			return false;
//		}else{
//			if(data.length > last){
//				System.arraycopy(data, 0, dataArray, front, last);
//				System.arraycopy(data, last, dataArray, 0, data.length - last);
//				front = data.length - last;
//			}else if(data.length == last){
//				System.arraycopy(data, 0, dataArray, front, data.length);
//				Log.i("VideoDataBuffer", "----入队相等----");
//				front = 0;
//			}else{
//				System.arraycopy(data, 0, dataArray, front, data.length);
//				front += data.length;
//			}
//			last = arraySize - front;
//			return true;
//		}
//	}
	
	
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
	
	
//	public byte[] deQueue(){
//		if(rear == front || ((front - rear) > 0 && (front - rear) < readSize)){
////			Log.i("VideoDataBuffer", "-----------读取音频数据为空---------->" + rear);
//			return null;
//		}
//		byte[] data = new byte[readSize];
//		System.arraycopy(dataArray, rear, data, 0, readSize);
//		if(rear == (arraySize - readSize)){
//			rear = 0;
//		}else{
//			rear += readSize;
//		}
////		Log.i("VideoDataBuffer", "-----------读取视频数据正常---------->" + rear);
//		return data;
//	}
	
	public void clearData(){
		front = 0;
		rear = 0;
	}
	
//	public void clearData(){
//		front = 0;
//		rear = 0;
//		last = arraySize - front;
//	}
		
}
