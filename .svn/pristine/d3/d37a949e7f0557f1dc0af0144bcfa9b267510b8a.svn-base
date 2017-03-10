package com.webcon.wp.utils;

/**
 * 登陆锁：防止登陆时dowork服务还没有启动
 *
 */
public class LoginLock {

	private static LoginLock loginLock;
	public static boolean hasLocked = false;
	
	private LoginLock(){
		
	}
	
	public static LoginLock getInstance(){
		if(loginLock == null){
			synchronized (LoginLock.class) {
				if(loginLock == null){
					loginLock = new LoginLock();
				}
			}
		}
		return loginLock;
	}
	
	public synchronized void getLock(){
		try {
			loginLock.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void getUnlock(){
		loginLock.notifyAll();
	}
}
