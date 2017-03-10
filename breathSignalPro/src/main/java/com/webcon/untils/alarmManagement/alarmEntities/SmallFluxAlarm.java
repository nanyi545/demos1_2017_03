package com.webcon.untils.alarmManagement.alarmEntities;

import com.webcon.untils.alarmManagement.AlarmManager;

import java.util.Calendar;

/**
 * Created by Administrator on 16-1-27.
 */
public class SmallFluxAlarm extends BreathAlarm {

    public SmallFluxAlarm(int[] alarmTime,int[] alarmwave,int[] fluxes){
        super(alarmTime,alarmwave, AlarmManager.smallFluxAlarm);
        this.flux=fluxes[0];
        this.fluxThreshold=fluxes[1];
    }
    public SmallFluxAlarm(Calendar alarmTime, int[] alarmwave, int[] fluxes){
        super(alarmTime,alarmwave, AlarmManager.smallFluxAlarm);
        this.flux=fluxes[0];
        this.fluxThreshold=fluxes[1];
    }

    /**
     * the fluxes (actual flux and target flux) at which this alarm was triggered,
     * A.U.
     *  **/
    public int flux;
    public int fluxThreshold;

    @Override
    public String toString() {
        String ret="低通量警报:"+this.getTimeStr()+"最小通量："+fluxThreshold+ "---当前通量"+flux;
        return ret;
    }


}
