package com.webcon.sus.eventObjects;

/**设备状态改变事件
 * @author m
 */
public class DeviceEvent extends BaseEvent {
    /** 设备上线 */
    public static final int DEVICE_ONLINE       = 0x101;
    /** 设备离线 */
    public static final int DEVICE_OFLINE       = 0x102;
    /** 其他异常 */
    public static final int DEVICE_EXCEPTION    = 0x109;

    public int stationId;
    private final String deviceId;

    public DeviceEvent(int type, String deviceId) {
        super(type);
        this.deviceId = deviceId;
    }

    public String getDeviceId(){
        return deviceId;
    }
}
