package com.webcon.sus.rvhelper;

import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.webcon.sus.demo.R;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.utils.AlarmMsgManager;
import com.webcon.sus.utils.CommonUtils;
import com.webcon.sus.utils.OnRVItemClickListener;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.WPApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 15-11-24.
 */
public class RVdragAdapter extends RecyclerView.Adapter<RVdragAdapter.RVHolder> implements ItemTouchHelperAdapter {


    private  OnStartDragListener mDragStartListener;
    private List<AlarmNode> mAlarms;

    //constructor
    public RVdragAdapter(List<AlarmNode> alarmList,OnStartDragListener dragStartListener){
        this.mAlarms = alarmList;
        mDragStartListener = dragStartListener;
    }


    // 自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class RVHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{
        public RelativeLayout container;
        public RelativeLayout state;
        //        public CheckBox selectCB;
        public com.andexert.library.RippleView rv;
        public TextView seq;
        public TextView device;
        public TextView date;
        public TextView info;
        public RVHolder(RelativeLayout v) {
            super(v);
            container = v;
        }

        @Override
        public void onItemClear() {
            container.setBackgroundColor(Color.WHITE);
        }

        @Override
        public void onItemSelected() {
            container.setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    public RVdragAdapter.RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        holder.rv=(RippleView)layout.findViewById(R.id.rippleview_container);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RVHolder holder, int position) {

        // Start a drag whenever the handle view it touched
        holder.container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });



        if(mOnItemClickListener != null){
            holder.rv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, pos);
                }
            });
            holder.rv.setOnLongClickListener(new View.OnLongClickListener() {
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


    @Override
    public void onItemDismiss(int position) {
        AlarmNode alarm = WPApplication.getInstance().getAllAlarmList_norefresh().get(position);
        ArrayList<AlarmNode> list = new ArrayList<>();
        list.add(alarm);
        //转发处理  --->发给AlarmMsgManager 删除alarm + 更新UI
        AlarmMsgManager.getInstance().transmitCenter(AlarmMsgManager.ALARM_MANAGE_DELETE, list);
//        notifyItemRemoved(position); //
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mAlarms, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }




    // ---接口 处理点击事件 ------
    private OnRVItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRVItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
