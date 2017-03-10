package com.webcon.sus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.webcon.sus.demo.R;
import com.webcon.sus.entity.AlarmInfo;

import java.util.List;

/**
 * 和报警查询列表adapter
 * 
 * @author Vieboo
 * 
 */
public class MonitorAnnalListAdapter extends BaseAdapter {

	private List<AlarmInfo> alarmInfoList;
	private LayoutInflater lInflater;
	private Context context;
	private String[] typeArray;

	public MonitorAnnalListAdapter(Context context, List<AlarmInfo> list,
			String[] typeArray) {
		lInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.alarmInfoList = list;
		this.context = context;
		this.typeArray = typeArray;
	}

	@Override
	public int getCount() {
		return alarmInfoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return alarmInfoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder holder = null;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = lInflater.inflate(
					R.layout.layout_camera_monitor_annal_msglist_item, null);
			holder.numTV = (TextView) arg1
					.findViewById(R.id.cma_msglist_item_tv_number);
			holder.typeTV = (TextView) arg1
					.findViewById(R.id.cma_msglist_item_tv_type);
			holder.timeTV = (TextView) arg1
					.findViewById(R.id.cma_msglist_item_tv_time);
			holder.messageTV = (TextView) arg1
					.findViewById(R.id.cma_msglist_item_tv_message);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		if (arg0 % 2 == 0) {
			arg1.setBackgroundColor(context.getResources().getColor(
					R.color.color_camera_monitor_annal_bg));
		} else {
			arg1.setBackgroundColor(context.getResources().getColor(
					R.color.color_white));
		}

		holder.numTV.setText((arg0 + 1) + "");
		// 报警类型
		if (0 < alarmInfoList.get(arg0).getAlarmType()
				&& alarmInfoList.get(arg0).getAlarmType() < 7)
			holder.typeTV.setText(typeArray[alarmInfoList.get(arg0)
					.getAlarmType() - 1]);
		// 报警时间和日期
		if (alarmInfoList.get(arg0).getAlarmDateTime() != null) {
			String dateTime = alarmInfoList.get(arg0).getAlarmDateTime();
			dateTime = dateTime.replaceFirst(" ", "\n");
			holder.timeTV.setText(dateTime);
		}

		// 如果为摄像头报警，则报警信息为报警强度
		if (alarmInfoList.get(arg0).getAlarmType() == 2) {
			short alarmStrength = (short) ((Short.parseShort(alarmInfoList.get(
					arg0).getAlarmInfo()) >> 8) & 0xff);
			// 轻度报警
			if (alarmStrength == 1) {
				holder.messageTV.setText(context
						.getString(R.string.str_notifi_alarm_level_light));
				holder.messageTV.setTextColor(context.getResources().getColor(
						R.color.color_alarm_light));
			}
			// 重度报警
			else if (alarmStrength == 2) {
				holder.messageTV.setText(context
						.getString(R.string.str_notifi_alarm_level_weight));
				holder.messageTV.setTextColor(context.getResources().getColor(
						R.color.color_alarm_weight));
			}
			// 紧急报警
			else if (alarmStrength == 3) {
				holder.messageTV.setText(context
						.getString(R.string.str_notifi_alarm_level_crash));
				holder.messageTV.setTextColor(context.getResources().getColor(
						R.color.color_alarm_crash));
			}
		} else {
			// 不知道显示什么
		}

		return arg1;
	}

	private class ViewHolder {
		public TextView numTV;
		public TextView typeTV;
		public TextView timeTV;
		public TextView messageTV;
	}

}
