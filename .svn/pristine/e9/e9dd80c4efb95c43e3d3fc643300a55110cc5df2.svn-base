package com.webcon.sus.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.webcon.sus.demo.R;
import com.webcon.sus.entity.StationNode;
import com.webcon.sus.utils.CommonUtils;
import com.webcon.sus.utils.SUConstant;

import java.util.List;


/**
 * 站场列表适配器
 * @author m
 */
public class StationListAdapter extends BaseAdapter{

    private List<StationNode> mStations;
    private LayoutInflater mInflater;

    public StationListAdapter(Context context, List<StationNode> list){
        mStations = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mStations.size();
    }

    @Override
    public Object getItem(int position) {
        return mStations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_station_list, null);
            holder.state = (ImageView)convertView.findViewById(R.id.station_item_state);
            holder.name = (TextView)convertView.findViewById(R.id.station_item_name);
            holder.msg = (TextView)convertView.findViewById(R.id.station_item_msg);
            holder.online = (TextView)convertView.findViewById(R.id.station_item_online);
            holder.defence = (TextView)convertView.findViewById(R.id.station_item_defence);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        //--赋值---
        int alarms = mStations.get(position).getNewAlarm();
        holder.name.setText(mStations.get(position).getName());
        switchState(holder, mStations.get(position).isOnline(),
                mStations.get(position).getState(), alarms);

        return convertView;
    }

    /**
     * 切换状态
     */
    private void switchState(ViewHolder holder, boolean isOnline, int stateFlag, int alarms){
        Log.i("check adapter", "alarms:" + alarms);
        if(isOnline){
            holder.name.setTextColor(SUConstant.COLOR_STATE_ONLINE);
            holder.msg.setVisibility(View.VISIBLE);
            holder.online.setText(CommonUtils.ORIGINAL_ONLINE);
            holder.online.setTextColor(SUConstant.COLOR_ALARM_PURPLE);
            holder.defence.setVisibility(View.VISIBLE);
            switch(stateFlag){
                case SUConstant.FLAG_STATION_CLOSED:
                    holder.state.setImageResource(SUConstant.STATION_STATE_ALARM_1);
                    if(alarms > 0){
                        holder.msg.setTextColor(SUConstant.COLOR_ALARM_ORANGE_FADE);
                        holder.msg.setText(CommonUtils.placeHolderSwitch(alarms));
                    }else{
                        holder.msg.setTextColor(SUConstant.COLOR_ALARM_GREEN_FADE);
                        holder.msg.setText(CommonUtils.ORIGINAL_NO_ALARM);
                    }
                    holder.defence.setText(CommonUtils.ORIGINAL_DEFENCE_CLOSED);
                    holder.defence.setTextColor(SUConstant.COLOR_ALARM_PURPLE_FADE);
                    break;
                case SUConstant.FLAG_STATION_OPENED:
                    holder.state.setImageResource(SUConstant.STATION_STATE_NORMAL);
                    holder.msg.setTextColor(SUConstant.COLOR_ALARM_GREEN);
                    holder.msg.setText(CommonUtils.ORIGINAL_NO_ALARM);
                    holder.defence.setText(CommonUtils.ORIGINAL_DEFENCE_OPENED);
                    holder.defence.setTextColor(SUConstant.COLOR_ALARM_PURPLE);
                    break;
                case SUConstant.FLAG_STATION_ALARM:
                    holder.state.setImageResource(SUConstant.STATION_STATE_ALARM_2);
                    holder.msg.setTextColor(SUConstant.COLOR_ALARM_ORANGE);
                    holder.msg.setText(CommonUtils.placeHolderSwitch(alarms));
                    holder.defence.setText(CommonUtils.ORIGINAL_DEFENCE_OPENED);
                    holder.defence.setTextColor(SUConstant.COLOR_ALARM_PURPLE);
                    break;
                default:
                    holder.state.setImageResource(SUConstant.STATION_STATE_CLOSED);
                    holder.msg.setTextColor(SUConstant.COLOR_ALARM_GREY_FADE);
                    holder.msg.setText("");
                    holder.defence.setText("");
                    break;
            }
        }else{
            holder.name.setTextColor(SUConstant.COLOR_STATE_OFFLINE);
            holder.state.setImageResource(SUConstant.STATION_STATE_OFFLINE);
            holder.msg.setVisibility(View.GONE);
            holder.online.setText(CommonUtils.ORIGINAL_OFFLINE);
            holder.online.setTextColor(SUConstant.COLOR_ALARM_PURPLE_FADE);
            holder.defence.setVisibility(View.GONE);
        }

    }


    //----
    private class ViewHolder{
        ImageView state;
        TextView name;
        TextView msg;
        TextView online;
        TextView defence;
    }
}
