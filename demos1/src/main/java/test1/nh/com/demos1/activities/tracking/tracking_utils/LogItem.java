package test1.nh.com.demos1.activities.tracking.tracking_utils;

/**
 * Created by Administrator on 2017/5/14.
 */

public class LogItem {

    /**
     * time of creating
     */
    private long time = 0;

    private LogType type;
    public enum LogType {
        VIEW_CHANGE,
        IO_NETWORK,
        USER_ACTION,
        HANDLE_THROWABLE
    }


    /**
     * for {@link LogType#VIEW_CHANGE}  :  View name
     * for {@link LogType#IO_NETWORK}   :  api name
     * for {@link LogType#USER_ACTION}  :  button/action name
     * for {@link LogType#HANDLE_THROWABLE}  :  throwable name
     */
    private String logItemName;


    public long getTime() {
        return time;
    }

    public LogType getType() {
        return type;
    }

    public String getLogItemName() {
        return logItemName;
    }


    protected LogItem(LogType type, String logItemName) {
        this.time = System.currentTimeMillis();
        this.type = type;
        this.logItemName=logItemName;
    }


    @Override
    public String toString() {
        String logName;
        switch (type) {
            case VIEW_CHANGE:
                logName = "view_change";
                break;
            case IO_NETWORK:
                logName = "network_io";
                break;
            case USER_ACTION:
                logName = "user_action";
                break;
            case HANDLE_THROWABLE:
                logName = "handle_throwable";
                break;
            default:
                logName = "undefined";
                break;
        }
        return logName;
    }


}
