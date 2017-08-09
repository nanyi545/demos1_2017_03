package test1.nh.com.demos1.activities.tracking.tracking_utils;

/**
 * Created by Administrator on 2017/5/14.
 */

public class LogItem {


    public static final int VIEW_CHANGE=1, IO_NETWORK=2, USER_ACTION=3, HANDLE_THROWABLE=4;

    /**
     * startTime of creating
     */
    private long start = 0;
    private long end = 0;
    private int type;

    /**
     * only for {@link #IO_NETWORK} :
     */
    private boolean success;


    /**
     * for {@link #VIEW_CHANGE}  :  View name
     * for {@link #IO_NETWORK}   :  api name
     * for {@link #USER_ACTION}  :  button/action name
     * for {@link #HANDLE_THROWABLE}  :  throwable name
     */
    private String logItemName;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
        setEndTime();
    }

    public long getEndTime() {
        return end;
    }

    public void setEndTime() {
        this.end = System.currentTimeMillis();
    }

    public long getStartTime() {
        return start;
    }

    public int getType() {
        return type;
    }

    public String getLogItemName() {
        return logItemName;
    }


    protected LogItem(int type, String logItemName) {
        this.start = System.currentTimeMillis();
        this.type = type;
        this.logItemName=logItemName;
    }

    protected LogItem(int type, String logItemName,long startTime,long endTime,boolean success) {
        this.start =startTime;
        this.type = type;
        this.logItemName=logItemName;
        this.end=endTime;
        this.success=success;
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
                logName = "error";
                break;
            default:
                logName = "undefined";
                break;
        }
        return logName;
    }


}
