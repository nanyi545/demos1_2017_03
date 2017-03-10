package com.webcon.sus.entity;

import android.util.Log;

import com.webcon.wp.utils.WPApplication;

/**
 * 登录用户信息结点
 * @author m
 */
public class UserNode {

    private short clusterid = -1;
    private int clusterUserId = -1;
    private String userId = "";
    private String userName = "";
    private boolean initialized = false;

    public String getUserId() {
        if(WPApplication.DEBUG){
            Log.i("USER_NODE", "getUserId:" + userId);
        }
        return userId;
    }

    public void setUserId(String userId) {
        initialized = false;
        this.userId = userId;
    }

    public String getUserName() {
        if(WPApplication.DEBUG){
            Log.i("USER_NODE", "getUserName:" + userName);
        }
        return userName;
    }

    public void setUserName(String userName) {
        initialized = true;
        this.userName = userName;
    }

    public short getClusterid() {
        return clusterid;
    }

    public void setClusterid(short clusterid) {
        this.clusterid = clusterid;
    }

    public int getClusterUserId() {
        return clusterUserId;
    }

    public void setClusterUserId(int clusterUserId) {
        this.clusterUserId = clusterUserId;
    }

    public boolean getInitialized(){
        return this.initialized;
    }
}
