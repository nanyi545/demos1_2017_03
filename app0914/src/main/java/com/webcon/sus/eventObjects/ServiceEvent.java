package com.webcon.sus.eventObjects;

import com.webcon.sus.entity.AlarmNode;

/**
 * @author m
 */
public class ServiceEvent extends BaseEvent{
    public static final int SERVICE_EVENT_INIT_OVER         = 1;
    public static final int SERVICE_EVENT_INIT_STATION_REQ  = 2;
    public static final int SERVICE_EVENT_INIT_ALARM_REQ    = 3;
    public static final int SERVICE_EVENT_CREATE_NOTI       = 4;
    public static final int SERVICE_EVENT_CLEAR_NOTI        = 5;
    public static final int SERVICE_EVENT_INIT_ERROR        = 10;
    public static final int SERVICE_EVENT_INIT_BUSY         = 16;

    private AlarmNode alarm;
    public ServiceEvent(int type) {
        super(type);
    }
    public int msg;

    public void setAlarm(AlarmNode alarm){
        this.alarm = alarm;
    }

    public AlarmNode getAlarm(){
        return alarm;
    }
}
