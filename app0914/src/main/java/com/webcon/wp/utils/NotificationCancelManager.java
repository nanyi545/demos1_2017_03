package com.webcon.wp.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 清除通知的管理类
 * @author Vieboo
 *
 */
public class NotificationCancelManager {

	private static NotificationCancelManager ncm;
	
	public static final String TAG_MUTUAL_INVITE = "TAG_MUTUAL_INVITE";	//会话邀请
	public static final String TAG_VIDEO_INVITE = "TAG_VIDEO_INVITE";	//视频邀请
	public static final String TAG_ALARM = "TAG_ALARM";	//报警通知
	
	private List<Integer> mutualInviteList;	//会话邀请通知ID的集合
	private List<Integer> videoInviteList;	//视频邀请通知ID的集合
	private List<Integer> alarmList;	//报警通知ID的集合
	
	private NotificationManager notificationManager;
	
	private NotificationCancelManager(){
		mutualInviteList = new ArrayList<Integer>();
		videoInviteList = new ArrayList<Integer>();
		alarmList = new ArrayList<Integer>();
	}
	
	public static NotificationCancelManager getInstance(){
		if(ncm == null){
			synchronized (NotificationCancelManager.class) {
				if(ncm == null){
					ncm = new NotificationCancelManager();
				}
			}
		}
		return ncm;
	}
	
	
	/**
	 * 添加新的通知ID
	 * @param id
	 * @param tag
	 */
	public void addNewNotification(int id, String tag){
		//会话邀请
		if(tag.equals(TAG_MUTUAL_INVITE)){
			mutualInviteList.add(id);
		}
		//视频邀请
		else if(tag.equals(TAG_VIDEO_INVITE)){
			videoInviteList.add(id);
		}
		//报警通知
		else if(tag.equals(TAG_ALARM)){
			alarmList.add(id);
		}
	}
	
	
	/**
	 * 根据tag清除通知
	 */
	public void clearNotifications(Context context, String tag){
		if(notificationManager == null){
			notificationManager = (NotificationManager) context.getSystemService(
					Context.NOTIFICATION_SERVICE);
		}
		//会话邀请
		if(tag.equals(TAG_MUTUAL_INVITE)){
			clear(mutualInviteList);
		}
		//视频邀请
		else if(tag.equals(TAG_VIDEO_INVITE)){
			clear(videoInviteList);
		}
		//报警通知
		else if(tag.equals(TAG_ALARM)){
			clear(alarmList);
		}
	}
	
	/**
	 * 清除通知
	 */
	private void clear(List<Integer> list){
		for(int i=0; i<list.size(); i++){
			notificationManager.cancel(list.get(i));
		}
		list.clear();
	}
	
	
	/**
	 * 清除所有通知
	 */
	public void clearAllNotification(Context context){
		if(notificationManager == null){
			notificationManager = (NotificationManager) context.getSystemService(
					Context.NOTIFICATION_SERVICE);
		}
		notificationManager.cancelAll();
		mutualInviteList.clear();
		videoInviteList.clear();
		alarmList.clear();
	}
	
	/**
	 * 清除单条通知
	 */
	public void clearOneNotification(Context context, int id, String tag){
		if(notificationManager == null){
			notificationManager = (NotificationManager) context.getSystemService(
					Context.NOTIFICATION_SERVICE);
		}
		//报警通知
		if(tag.equals(TAG_ALARM)){
			clearOne(id, alarmList);
		}else{
            Log.e("NotificationCancel", "错误的通知类型");
        }
	}
	
	/**
	 * 清除单条
	 */
	private void clearOne(Integer id, List<Integer> list){
		if(list.contains(id)){
			notificationManager.cancel(id);
			list.remove(id);
		}
	}
	
}
