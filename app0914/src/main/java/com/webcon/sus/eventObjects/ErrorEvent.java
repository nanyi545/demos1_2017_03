package com.webcon.sus.eventObjects;

/**
 * 错误事件，通知service显示错误原因
 * @author m
 */
public class ErrorEvent extends BaseEvent{
    public static final int ERROR_EVENT_TYPE_PARSE_SUB_PDU  = 1;
    public static final int ERROR_EVENT_TYPE_CAPTURE_OVERTIME = 2;

    public int code = 0;
    public String description = "未知错误";

    public ErrorEvent(int type) {
        super(type);
    }

}
