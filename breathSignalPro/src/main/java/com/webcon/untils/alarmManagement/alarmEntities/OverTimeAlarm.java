package com.webcon.untils.alarmManagement.alarmEntities;

import com.webcon.untils.alarmManagement.AlarmManager;

import java.util.Calendar;

/**
 * Created by Administrator on 16-1-27.
 */
public class OverTimeAlarm extends BreathAlarm {


    public OverTimeAlarm(int[] alarmTime,int[] alarmwave,int delay_){
        super(alarmTime,alarmwave, AlarmManager.overTimeAlarm);
        this.delay=delay_;
    }
    public OverTimeAlarm(Calendar alarmTime,int[] alarmwave,int delay_){
        super(alarmTime,alarmwave, AlarmManager.overTimeAlarm);
        this.delay=delay_;
    }

    /**
     * the delayTime (the threshold time) at which this alarm was triggered,
     * in unit of 0.2 sec
     *  **/
    public int delay;

    @Override
    public String toString() {
        String ret="超时警报:"+this.getTimeStr()+"--超过"+(delay/5.0)+"秒没有检测到呼吸信号";
        return ret;
    }
}
