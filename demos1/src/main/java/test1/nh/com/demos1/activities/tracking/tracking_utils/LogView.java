package test1.nh.com.demos1.activities.tracking.tracking_utils;

/**
 * Created by Administrator on 2017/5/14.
 */

public class LogView extends LogItem {


    private boolean enter;

    public LogView(boolean isEnter,String ViewName) {
        super(LogType.VIEW_CHANGE,ViewName);
        enter=isEnter;
    }




}
