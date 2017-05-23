package test1.nh.com.demos1.activities.tracking.tracking_utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/21.
 */

class LogSession {

    protected List<LogItem> viewLogs,netIoLogs,userActionLogs,errorLogs;

    public LogSession() {
        this.viewLogs = new ArrayList<>();
        this.netIoLogs =  new ArrayList<>();
        this.userActionLogs =  new ArrayList<>();
        this.errorLogs =  new ArrayList<>();
    }

    protected long sessionStart,sessionEnd;


    protected void computeDuration(){
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
        sessionStart=min;
        sessionEnd=max+5000;  // give room to show last item
    }

}
