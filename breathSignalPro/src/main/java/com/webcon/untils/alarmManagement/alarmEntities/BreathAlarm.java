package com.webcon.untils.alarmManagement.alarmEntities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 16-1-27.
 */
public class BreathAlarm implements Serializable, Comparable<BreathAlarm> {

    public int alarmLevel;
    public int alarmType;  // types of alarm

    private int[] waveSnap=new int[300]; // snap shot of breath waveform at the time of alarm
    public int[] getWaveform(){
        return waveSnap;
    }

    private Calendar alarmT1;        // time of the alarm   Calendar c2 = new GregorianCalendar(2012, 11, 12, 12, 12, 12);
    public Calendar getTime(){
        return alarmT1;
    }


    /** $是否已经处看过饿了     0：没看  1：已经看了 */
    public short isChecked;

    //---------contructor1-------------
    public BreathAlarm(int[] alarmTime,int[] alarmwave,int alarmType_){
        int second=alarmTime[5];
        int minute=alarmTime[4];
        int hour=alarmTime[3];
        int monthDay=alarmTime[2];
        int month=alarmTime[1]-1;
        int year=alarmTime[0];
        alarmT1=new GregorianCalendar();
        alarmT1.set(second, minute, hour, monthDay, month, year);
        waveSnap=alarmwave;
        isChecked=0;
        alarmType=alarmType_;
    }

    //---------contructor2-------------
    public BreathAlarm(Calendar c1, int[] alarmwave, int alarmType_){
        alarmT1=c1;
        waveSnap=alarmwave;
        isChecked=0;
        alarmType=alarmType_;
    }

    //
    public void setChecked(){
        isChecked=1;
    }


    // so the array can be sorted using, Collections.sort
    @Override
    public int compareTo(BreathAlarm another) {
        if(isChecked > another.isChecked){
            return 1;
        }else if(isChecked < another.isChecked) {
            return -1;
        }else{
            if(alarmT1.before(another.getTime())){
                return 1;
            }else if(alarmT1.after(another.getTime())){
                return -1;
            }else{
                return 0;
            }
        }
    }


    public String getTimeStr(){
        Calendar c=this.getTime();
        String ret=c.get(Calendar.YEAR)+"年"+(c.get(Calendar.MONTH)+1)+"月"+c.get(Calendar.DAY_OF_MONTH) +"日"
                +c.get(Calendar.HOUR_OF_DAY)+"时"+c.get(Calendar.MINUTE)+"分"+c.get(Calendar.SECOND)+"秒";
        return ret;
    }

}
