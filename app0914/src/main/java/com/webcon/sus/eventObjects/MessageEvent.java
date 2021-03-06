package com.webcon.sus.eventObjects;

/**报警消息事件
 * @author m
 */
public class MessageEvent extends BaseEvent{
    public static final int ALARM_FLAG_INIT         = 1;
    public static final int ALARM_FLAG_REFRESH      = 2;
    public static final int ALARM_SWIPE_DELETED      = 3;
    

    public int stationId = -1;
    public int alarmId = -1;
    public boolean success = false;
    public boolean reload = false;

    public MessageEvent(int type){
        super(type);
    }

}
