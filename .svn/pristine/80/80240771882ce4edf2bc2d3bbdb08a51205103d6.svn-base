package com.webcon.sus.eventObjects;

/**
 * 站场事件
 * @author m
 */
public class StationEvent extends BaseEvent{
    /** 初始化完成，更新数据 : <0 退出；=0 弹出对话框退出 */
    public static final int STATION_EVENT_INIT          = 0x1101;
    /** 刷新，更新列表 */
    public static final int STATION_EVENT_REFRESH       = 0x1102;
    /** 其他意外情况（保留） */
    public static final int STATION_EVENT_EXCEPTION     = 0x900;
    /** 站场在线状态改变 */
    public static final int STATION_EVENT_ONLINE_STATE_CHANGED  = 0x1201;


    //INIT：初始化结果    REFRESH：布防状态或者只刷新//
    public int msg = -1;
    public int stationId = -1;
    public boolean reload = false;

    public StationEvent(int type) {
        super(type);
    }
}
