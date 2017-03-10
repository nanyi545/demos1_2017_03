package com.webcon.sus.eventObjects;

/**修改事件
 * <p>包括用户昵称修改，用户密码修改等</p>
 * @author m
 */
public class ModifyEvent extends BaseEvent{
    public static final int MODIFY_EVENT_CHANGE_NAME    = 1;
    public static final int MODIFY_EVENT_CHANGE_PWD     = 2;
    public static final int MODIFY_EVENT_CHANGE_STATION = 3;

    public ModifyEvent(int type) {
        super(type);
    }
}
