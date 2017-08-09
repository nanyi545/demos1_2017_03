package test1.nh.com.demos1.activities.tracking.tracking_utils;

/**
 * Created by Administrator on 2017/5/25.
 */

public class LogError extends LogItem {

    public LogError(String errorStr) {
        super(LogItem.HANDLE_THROWABLE, errorStr);
    }

}
