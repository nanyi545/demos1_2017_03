package com.webcon.sus.media;

import java.util.LinkedList;

/**
 * 视频数据发送缓冲队列
 *
 */
public class SelfVideoDataBuffer2 {
	private final static int MAX = 50;
	private static SelfVideoDataBuffer2 mQueue;
	private LinkedList<byte[]> list;	//实现Queue
	
	/**
	 * 私有构造器
	 */
	private SelfVideoDataBuffer2(){
		list = new LinkedList<byte[]>();
	}
	
	/**
	 * 返回单例对象
	 */
	public static SelfVideoDataBuffer2 getVideoDataBuffer(){
		if(mQueue == null){
			synchronized (SelfVideoDataBuffer2.class) {
				if(mQueue == null){
					mQueue = new SelfVideoDataBuffer2();
				}
			}
		}
		return mQueue;
	}
	
	/**
	 * 元素入队
	 * @param in 编码后的数据
	 * @return 队列已满返回false
	 */
	public boolean enQueue(byte[] in){
		synchronized(SelfVideoDataBuffer2.class){
			if(list.size() >= MAX){
				return false;
			}
            byte[] data = new byte[in.length];
			System.arraycopy(in, 0, data, 0, in.length);
			//将数据添加到队列末尾
			list.addLast(data);
			return true;
		}
	}
	
	/**
	 * 元素出队
	 * @return 返回队列第一个元素
	 */
	public byte[] deQueue(){
		synchronized(SelfVideoDataBuffer2.class){
			return list.pollFirst();
		}
	}
	
	/**
	 * 清空队列
	 */
	public void release(){
		synchronized(SelfVideoDataBuffer2.class){
			list.clear();
			mQueue = null;
		}
	}
	
	/**
	 * 丢弃旧的数据
	 */
	public void clearOldData(){
		synchronized(SelfVideoDataBuffer2.class){
			list.clear();
		}
	}
	
	/**
	 * 获取当前队列长度
	 * @return 当前队列长度
	 */
	public int listSize(){
		synchronized(SelfVideoDataBuffer2.class){
			return list.size();
		}
	}
}
