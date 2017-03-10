package com.webcon.sus.entity;

import java.io.Serializable;

/**
 * 设备（摄像头）实体类
 * @author Vieboo
 *
 */
public class Equipment implements Serializable {

	private String  eqID;	        //设备ID（名称）
	private String  eqName;	        //设备昵称
	private int     eqConNum;	    //门限次数
	private int     eqConWeek;	    //报警周期
	private String  eqConStartTime;	//起始时间
	private String  eqConEndTime;	//结束时间
	private String  eqRemark;	    //备注信息
	private String  monitorId;		//对应的监控端用户ID
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eqID == null) ? 0 : eqID.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Equipment other = (Equipment) obj;
		if (eqID == null) {
			if (other.eqID != null)
				return false;
		} else if (!eqID.equals(other.eqID))
			return false;
		return true;
	}
	
	
	public String getEqID() {
		return eqID;
	}
	public void setEqID(String eqID) {
		this.eqID = eqID;
	}
	public String getEqName() {
		return eqName;
	}
	public void setEqName(String eqName) {
		this.eqName = eqName;
	}
	public int getEqConNum() {
		return eqConNum;
	}
	public void setEqConNum(int eqConNum) {
		this.eqConNum = eqConNum;
	}
	public int getEqConWeek() {
		return eqConWeek;
	}
	public void setEqConWeek(int eqConWeek) {
		this.eqConWeek = eqConWeek;
	}
	public String getEqConStartTime() {
		return eqConStartTime;
	}
	public void setEqConStartTime(String eqConStartTime) {
		this.eqConStartTime = eqConStartTime;
	}
	public String getEqConEndTime() {
		return eqConEndTime;
	}
	public void setEqConEndTime(String eqConEndTime) {
		this.eqConEndTime = eqConEndTime;
	}
	public String getEqRemark() {
		return eqRemark;
	}
	public void setEqRemark(String eqRemark) {
		this.eqRemark = eqRemark;
	}
	public String getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	
}
