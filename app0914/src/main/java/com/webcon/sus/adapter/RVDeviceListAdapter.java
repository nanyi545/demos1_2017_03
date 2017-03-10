package com.webcon.sus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webcon.sus.demo.R;
import com.webcon.sus.entity.BaseDevice;
import com.webcon.sus.utils.OnRVItemClickListener;
import com.webcon.sus.utils.SUConstant;

import java.util.ArrayList;
import java.util.List;

/**设备列表
 * @author m
 */
public class RVDeviceListAdapter extends RecyclerView.Adapter<RVDeviceListAdapter.RVHolder>{
    private final List<BaseDevice> mDevices;

    public RVDeviceListAdapter(List<BaseDevice> list){
        if(list == null){
            this.mDevices = new ArrayList<>();
        }else{
            this.mDevices = list;
        }
    }

    @Override
    public RVDeviceListAdapter.RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_device_rv, parent, false);
        RelativeLayout container = (RelativeLayout)layout.findViewById(R.id.device_item_container);
        // 创建holder
        RVHolder holder = new RVHolder(container);
        // 添加组件
        holder.link_begin = (ImageView)layout.findViewById(R.id.iv_link_begin);
        holder.link_end = (ImageView)layout.findViewById(R.id.iv_link_end);
        holder.name = (TextView)layout.findViewById(R.id.device_item_name);
        holder.detail = (TextView)layout.findViewById(R.id.device_item_detail);
        holder.check = (ImageView)layout.findViewById(R.id.device_item_check);

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
        if(mDevices.size() <= 1){
            holder.link_begin.setVisibility(View.INVISIBLE);
            holder.link_end.setVisibility(View.INVISIBLE);
        }else if(position == 0){
            holder.link_begin.setVisibility(View.INVISIBLE);
            holder.link_end.setVisibility(View.VISIBLE);
        }else if(position == mDevices.size() - 1){
            holder.link_begin.setVisibility(View.VISIBLE);
            holder.link_end.setVisibility(View.INVISIBLE);
        }else{
            holder.link_begin.setVisibility(View.VISIBLE);
            holder.link_end.setVisibility(View.VISIBLE);
        }
        setState(holder, mDevices.get(position).getType());
        holder.name.setText(mDevices.get(position).getName());
    }

    private void setState(RVHolder holder, int type){
        switch(type){
            case SUConstant.DEVICE_TYPE_MONITOR:
                holder.check.setImageResource(SUConstant.DEVICE_CHECK_WEBC);
                break;
            default:
                holder.check.setImageDrawable(null);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }


    // 自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class RVHolder extends RecyclerView.ViewHolder{
        RelativeLayout container;
        ImageView link_begin;
        ImageView link_end;
        TextView name;
        TextView detail;
        ImageView check;
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
