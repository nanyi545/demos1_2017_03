package com.webcon.sus.utils;


import android.util.Log;

import java.util.LinkedList;

public class CallbackBuffer {
    private static final String TAG = "CallbackBuffer";
    private static final int MAX = 150;	//缓冲队列长度
    private static CallbackBuffer mQueue;
    private LinkedList<byte[]> list;	//实现Queue
//    private List<byte[]> list;	//实现Queue

    private CallbackBuffer(){
//        list =  Collections.synchronizedList(new LinkedList<byte[]>());
        list = new LinkedList<>();
    }

    /**
     * 返回单例对象
     */
    public static CallbackBuffer getInstance(){
        if(mQueue == null){
            synchronized (CallbackBuffer.class) {
                if(mQueue == null){
                    mQueue = new CallbackBuffer();
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
        synchronized (CallbackBuffer.class){
//            Log.i("AAA", "thread A" + Thread.currentThread().getName());//get current thread
            Log.i("AAA", "en Queue--length" + list.size());  // print current length of the que
            if(list.size() >= MAX){
                Log.e(TAG, "loss pack");
                return false;
            }
            byte[] data = new byte[in.length];
            System.arraycopy(in, 0, data, 0, in.length);
            //将数据添加到队列末尾
            list.addLast(data);
//        list.add(data);
            return true;
        }
    }

    /**
     * 元素出队
     * @return 返回队列第一个元素
     */
    public byte[] deQueue(){
        synchronized (CallbackBuffer.class){   //.this
//            Log.i("AAA", "thread B" + Thread.currentThread().getName());
            if(getSize() <= 0){
                return null;
            }
            return list.pollFirst();
        }
//        byte[] data = list.get(0);
//        list.remove(0);
//        return data;
    }

    /**
     * 获取当前队列长度
     * @return 当前队列长度
     */
    public int getSize(){
        return list.size();
    }

    /**
     * 丢弃旧的数据
     */
    public void clearData(){
        list.clear();
    }


    /**
     * 清空队列
     */
    public void release(){
        list.clear();
        mQueue = null;
    }

}
