package com.webcon.sus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webcon.sus.demo.R;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.utils.CommonUtils;
import com.webcon.sus.utils.OnRVItemClickListener;
import com.webcon.sus.utils.SUConstant;

import java.util.List;

/**
 * 消息Adapter
 * @author m
 */
public class RVAlarmListAdapter extends RecyclerView.Adapter<RVAlarmListAdapter.RVHolder>{

    private List<AlarmNode> mAlarms;

    public RVAlarmListAdapter(List<AlarmNode> alarmList){
        this.mAlarms = alarmList;
    }

    @Override
    public RVAlarmListAdapter.RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_tab_alarm_list_item, parent, false);
        RelativeLayout container = (RelativeLayout)layout.findViewById(R.id.list_item_alarm_container);
        // 创建holder
        RVHolder holder = new RVHolder(container);
        // 添加组件
        holder.state = (RelativeLayout) layout.findViewById(R.id.alarm_list_item_rl_num);
        holder.seq = (TextView) layout.findViewById(R.id.alarm_list_item_tv_number);
        holder.device = (TextView) layout.findViewById(R.id.alarm_list_item_tv_eqname);
        holder.date = (TextView) layout.findViewById(R.id.alarm_list_item_tv_time);
        holder.info = (TextView) layout.findViewById(R.id.alarm_list_item_tv_info);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RVHolder holder, int position) {
        if(mOnItemClickListener != null){
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, pos);
                }
            });
            holder.container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(v, pos);
                    return false;
                }
            });
        }

        // 赋值
        // 未处理加背景
        if (mAlarms.get(position).getIsSolved() == 0) {
            holder.state.setBackgroundResource(R.drawable.bg_tab_alarmlist_resolve);
        } else {
            holder.state.setBackgroundDrawable(null);
        }

        holder.seq.setText((position + 1) + "");
        holder.device.setText(mAlarms.get(position).getDeviceName());

        // 报警时间和日期
        String dateTime = mAlarms.get(position).getAlarmDate();
        dateTime = dateTime.replaceFirst(" ", "\n");
        holder.date.setText(dateTime);

        // 报警内容 --> 位置消息 --> 段位消息
        if(mAlarms.get(position).getPositionType() == SUConstant.POSITION_MSG_TYPE_SEGMENT){
            int segmentMsg = mAlarms.get(position).getPositionSegmentMsg();
            holder.info.setText(CommonUtils.segmentContentSwitch(segmentMsg));
        }else{
            holder.info.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }


    // 自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class RVHolder extends RecyclerView.ViewHolder{
        public RelativeLayout container;
        public RelativeLayout state;
//        public CheckBox selectCB;
        public TextView seq;
        public TextView device;
        public TextView date;
        public TextView info;
        public RVHolder(RelativeLayout v) {
            super(v);
            container = v;
        }
    }

    // ---接口 处理点击事件 ------
    private OnRVItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRVItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
