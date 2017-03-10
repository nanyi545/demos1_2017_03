package com.webcon.sus.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.webcon.sus.demo.R;
import com.webcon.sus.dateview.NumericWheelAdapter;
import com.webcon.sus.dateview.WheelView;
import com.webcon.wp.utils.PublicMethodUtil;


/**
 * 时间选择dialog
 */
public class TimeDialog {
	
	private AlertDialog timeDialog;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private int[] startTime, endTime;
	private TextView startTimeTV, endTimeTV;

	public TimeDialog(Context context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * 设置dialog所需的值
	 * @param start 起始时间
	 * @param end	结束时间
	 * @param startTV	显示起始时间的TextView
	 * @param endTV		显示结束时间的TextView
	 */
	public void setValues(int[] start, int[] end, TextView startTV, TextView endTV){
		startTime = start;
		endTime = end;
		startTimeTV = startTV;
		endTimeTV = endTV;
	}
	
	/**
	 * 初始化生成显示选择时间的dialog
	 */
	public void timeDialogInit(final int flag){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		
		View view = mLayoutInflater.inflate(R.layout.layout_dialog_time, null);
		
		TextView timeTitle = (TextView)view.findViewById(R.id.dialog_time_title);
		
		//时
		final WheelView hourWheel = (WheelView)view.findViewById(R.id.dialog_time_hour);
		hourWheel.setCyclic(true);
		hourWheel.setAdapter(new NumericWheelAdapter(0, 23));
		hourWheel.setLabel(mContext.getString(R.string.str_camera_monitor_annal_dialog_time_hour));
		
		//分
		final WheelView minuteWheel = (WheelView)view.findViewById(R.id.dialog_time_minute);
		minuteWheel.setCyclic(true);
		minuteWheel.setAdapter(new NumericWheelAdapter(0, 59));
		minuteWheel.setLabel(mContext.getString(R.string.str_camera_monitor_annal_dialog_time_minute));
		
		//秒
		final WheelView secondWheel = (WheelView)view.findViewById(R.id.dialog_time_second);
		secondWheel.setCyclic(true);
		secondWheel.setAdapter(new NumericWheelAdapter(0, 59));
		secondWheel.setLabel(mContext.getString(R.string.str_camera_monitor_annal_dialog_time_second));
				
		if(flag == 0){
			timeTitle.setText(R.string.str_camera_monitor_annal_dialog_time_start_title);
			hourWheel.setCurrentItem(startTime[0]);
			minuteWheel.setCurrentItem(startTime[1]);
			secondWheel.setCurrentItem(startTime[2]);
		}else{
			timeTitle.setText(R.string.str_camera_monitor_annal_dialog_time_end_title);
			hourWheel.setCurrentItem(endTime[0]);
			minuteWheel.setCurrentItem(endTime[1]);
			secondWheel.setCurrentItem(endTime[2]);
		}
		
		final Button timeSubBT = (Button)view.findViewById(R.id.dialog_time_bottom_bt_ok);
		final Button timeCancelBT = (Button)view.findViewById(R.id.dialog_time_bottom_bt_cancel);
		
		OnClickListener timeClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v == timeSubBT){
					if(flag == 0){
						startTimeTV.setText(getTimeText(hourWheel.getCurrentItem(), 
								minuteWheel.getCurrentItem(), secondWheel.getCurrentItem(), flag));
					}else{
						endTimeTV.setText(getTimeText(hourWheel.getCurrentItem(), 
								minuteWheel.getCurrentItem(), secondWheel.getCurrentItem(), flag));
					}
					timeDialog.dismiss();
					return ;
				}
				else if(v == timeCancelBT){
					timeDialog.dismiss();
					return ;
				}
			}
		};

		timeSubBT.setOnClickListener(timeClickListener);
		timeCancelBT.setOnClickListener(timeClickListener);
		
		// 根据屏幕密度来指定选择器字体的大小
		int textSize = PublicMethodUtil.getInstance().px2dip(mContext, 14);

		hourWheel.TEXT_SIZE = textSize;
		minuteWheel.TEXT_SIZE = textSize;
		secondWheel.TEXT_SIZE = textSize;
		
		builder.setView(view);
		
		timeDialog = builder.create();
		timeDialog.setView(view, 0, 0, 0, 0);
		timeDialog.show();
	}
	
	/**
	 * 生成时间字符串
	 */
	public String getTimeText(int hourCurrent, int minuteCurrent, int secondCurrent, int flag){
		if(flag == 0){
			startTime[0] = hourCurrent;
			startTime[1] = minuteCurrent;
			startTime[2] = secondCurrent;
		}else{
			endTime[0] = hourCurrent;
			endTime[1] = minuteCurrent;
			endTime[2] = secondCurrent;
		}
		return PublicMethodUtil.getInstance().makeDoubleNumber(hourCurrent) + ":" + 
				PublicMethodUtil.getInstance().makeDoubleNumber(minuteCurrent) + ":" + 
				PublicMethodUtil.getInstance().makeDoubleNumber(secondCurrent);
	}
	
	/**
	 * 当前时间对话框是否在显示
	 * @return	true：显示	false：未显示
	 */
	public boolean getIsShowing(){
		if(timeDialog == null){
			return false;
		}
		return timeDialog.isShowing();
	}
}
