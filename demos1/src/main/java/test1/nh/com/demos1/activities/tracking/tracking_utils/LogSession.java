package test1.nh.com.demos1.activities.tracking.tracking_utils;

import android.os.Build;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/21.
 */

public class LogSession {

    protected List<LogItem> viewLogs,netIoLogs,userActionLogs,errorLogs;

    public LogSession() {
        this.viewLogs = new ArrayList<>();
        this.netIoLogs =  new ArrayList<>();
        this.userActionLogs =  new ArrayList<>();
        this.errorLogs =  new ArrayList<>();
    }



    protected long[] computeDuration(){
        long min=System.currentTimeMillis(),max=0;
        List<LogItem> totalList=new ArrayList();
        totalList.addAll(viewLogs);
        totalList.addAll(netIoLogs);
        totalList.addAll(userActionLogs);
        totalList.addAll(errorLogs);
        for (LogItem item:totalList){
            if(item.getStartTime()<min){
                min=item.getStartTime();
            }
            if(item.getStartTime()>max){
                max=item.getStartTime();
            }
        }
        long[] duration=new long[2];
        duration[0]=min;
        duration[1]=max+5000;  // give room to show last item
        return duration;
    }


    @Override
    public String toString() {
        Gson gson=new Gson();
        return gson.toJson(LogSession.this);
    }

    protected int getDeviceType(){return 1;}  //  android:1, ios:2,   web:3
    protected String getDeviceInfo(){
        return "MANUFACTURER:"+ Build.MANUFACTURER+" model:"+android.os.Build.MODEL+" sdk:"+android.os.Build.VERSION.SDK_INT;
    }




}
