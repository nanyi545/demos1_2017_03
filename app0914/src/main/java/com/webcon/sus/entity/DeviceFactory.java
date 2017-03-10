package com.webcon.sus.entity;

import com.webcon.sus.utils.SUConstant;

/**
 * @author m
 */
public class DeviceFactory {
    public static DeviceFactory me;
    private DeviceFactory(){
    }
    public static DeviceFactory getInstance(){
        if(me == null){
            synchronized (DeviceFactory.class){
                if(me == null){
                    me = new DeviceFactory();
                }
            }
        }
        return me;
    }

    public BaseDevice createDevice(int type){
        BaseDevice device;
        switch (type){
            case SUConstant.DEVICE_TYPE_MONITOR:
                device = new MonitorNode(type);
                break;
            case SUConstant.DEVICE_TYPE_RADAR:
            case SUConstant.DEVICE_TPYE_INFRARED:
            default:
                device = new DeviceNode(type);
                break;
        }
        return device;
    }
}
