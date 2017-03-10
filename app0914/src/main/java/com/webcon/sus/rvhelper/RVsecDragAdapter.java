package com.webcon.sus.rvhelper;

import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.entity.StationNode;
import com.webcon.sus.utils.AlarmMsgManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 15-11-25.
 */
public class RVsecDragAdapter extends RVdragAdapter {

    private StationNode stationNode;

    //constructor
    public RVsecDragAdapter(List<AlarmNode> alarmList,OnStartDragListener dragStartListener,StationNode stationNode){
        super(alarmList,dragStartListener);
        this.stationNode=stationNode;
    }

    @Override
    public void onItemDismiss(int position) {
        AlarmNode alarm = stationNode.getAlarmList().get(position);
        ArrayList<AlarmNode> list = new ArrayList<>();
        list.add(alarm);
        //转发处理  --->发给AlarmMsgManager 删除alarm + 更新UI
        AlarmMsgManager.getInstance().transmitCenter(AlarmMsgManager.ALARM_MANAGE_DELETE, list);
//        notifyItemRemoved(position); //
    }
}
