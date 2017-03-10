package com.webcon.sus.entity;

import java.io.Serializable;

/**
 * 报警信息实体类
 * @author Vieboo
 *
 */
public class AlarmInfo implements Serializable {
	
	private String alarmEqId;	    //报警设备ID
	private String alarmEqName;	    //报警设备昵称
	private String alarmDateTime;	//报警日期、时间
	private short alarmType;	    //报警类型		2:摄像头报警	3:人体心跳报警	4:人体呼吸报警
	private String alarmInfo;	    //报警信息		1:轻度报警		2、3:重度报警
	private short isSolve;	        //是否已经处理	0：未处理	 	1：已经处理
	private String dealUserNick;    //处理着
	private long dealTime;          //处理时间
	
	
	
	public String getAlarmEqId() {
		return alarmEqId;
	}
	public void setAlarmEqId(String alarmEqId) {
		this.alarmEqId = alarmEqId;
	}
	public String getAlarmEqName() {
		return alarmEqName;
	}
	public void setAlarmEqName(String alarmEqName) {
		this.alarmEqName = alarmEqName;
	}
	public String getAlarmDateTime() {
		return alarmDateTime;
	}
	public void setAlarmDateTime(String alarmDateTime) {
		this.alarmDateTime = alarmDateTime;
	}
	public short getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(short alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmInfo() {
		return alarmInfo;
	}
	public void setAlarmInfo(String alarmInfo) {
		this.alarmInfo = alarmInfo;
	}
	public short getIsSolve() {
		return isSolve;
	}
	public void setIsSolve(short isSolve) {
		this.isSolve = isSolve;
	}
	public String getDealUserNick() {
		return dealUserNick;
	}
	public void setDealUserNick(String dealUserNick) {
		this.dealUserNick = dealUserNick;
	}
	public long getDealTime() {
		return dealTime;
	}
	public void setDealTime(long dealTime) {
		this.dealTime = dealTime;
	}
	@Override
	public String toString() {
		return "AlarmInfo [alarmEqId=" + alarmEqId + ", alarmDateTime="
				+ alarmDateTime + ", alarmType=" + alarmType + "]";
	}
	
	
}
