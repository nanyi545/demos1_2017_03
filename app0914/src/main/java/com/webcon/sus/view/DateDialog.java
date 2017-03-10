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
import com.webcon.sus.dateview.OnWheelChangedListener;
import com.webcon.sus.dateview.WheelView;
import com.webcon.wp.utils.PublicMethodUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 日期选择dialog
 */
public class DateDialog {

	private AlertDialog dateDialog;
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private int[] startDate, endDate;
	private TextView startDateTV, endDateTV;
	private static final int START_YEAR = 1900, END_YEAR = 2100;
	
	public DateDialog(Context context){
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * 设置dialog所需的值
	 * @param start 起始日期
	 * @param end	结束日期
	 * @param startTV	显示起始日期的TextView
	 * @param endTV		显示结束日期的TextView
	 */
	public void setValues(int[] start, int[] end, TextView startTV, TextView endTV){
		startDate = start;
		endDate = end;
		startDateTV = startTV;
		endDateTV = endTV;
	}
	
	/**
	 * 初始化生成显示选择日期的dialog
	 */
	public void dateDialogInit(final int flag){
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		
		View view = mLayoutInflater
				.inflate(R.layout.layout_dialog_date, null);
		
		//title
		TextView titleView = (TextView)view.findViewById(R.id.dialog_date_title);
		if(flag == 0){
			titleView.setText(R.string.str_camera_monitor_annal_dialog_date_start_title);
		}else{
			titleView.setText(R.string.str_camera_monitor_annal_dialog_date_end_title);
		}
		
		//年
		final WheelView yearWheel = (WheelView)view.findViewById(R.id.dialog_date_year);
		yearWheel.setCyclic(true);	//可循环滚动
		yearWheel.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));	//设置显示数据
		yearWheel.setLabel(mContext.getString(R.string.str_camera_monitor_annal_dialog_date_year));	//添加文字
		
		// 月
		final WheelView monthWheel = (WheelView) view.findViewById(R.id.dialog_date_month);
		monthWheel.setAdapter(new NumericWheelAdapter(1, 12));
		monthWheel.setCyclic(true);
		monthWheel.setLabel(mContext.getString(R.string.str_camera_monitor_annal_dialog_date_month));
		
		//日
		final WheelView dayWheel = (WheelView)view.findViewById(R.id.dialog_date_day);
		dayWheel.setCyclic(true);
		dayWheel.setLabel(mContext.getString(R.string.str_camera_monitor_annal_dialog_date_day));
		
		if(flag == 0){
			
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (list_big.contains(String.valueOf(startDate[1] + 1))) {
				dayWheel.setAdapter(new NumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(startDate[1] + 1))) {
				dayWheel.setAdapter(new NumericWheelAdapter(1, 30));
			} else {
				// 闰年
				if ((startDate[0] % 4 == 0 && startDate[0] % 100 != 0) || startDate[0] % 400 == 0)
					dayWheel.setAdapter(new NumericWheelAdapter(1, 29));
				else
					dayWheel.setAdapter(new NumericWheelAdapter(1, 28));
			}
			
			yearWheel.setCurrentItem(startDate[0] - START_YEAR);	//初始化时显示的数据
			monthWheel.setCurrentItem(startDate[1]);
			dayWheel.setCurrentItem(startDate[2] - 1);
		}else{

			// 判断大小月及是否闰年,用来确定"日"的数据
			if (list_big.contains(String.valueOf(endDate[1] + 1))) {
				dayWheel.setAdapter(new NumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(endDate[1] + 1))) {
				dayWheel.setAdapter(new NumericWheelAdapter(1, 30));
			} else {
				// 闰年
				if ((endDate[0] % 4 == 0 && endDate[0] % 100 != 0) || endDate[0] % 400 == 0)
					dayWheel.setAdapter(new NumericWheelAdapter(1, 29));
				else
					dayWheel.setAdapter(new NumericWheelAdapter(1, 28));
			}
			
			yearWheel.setCurrentItem(endDate[0] - START_YEAR);	//初始化时显示的数据
			monthWheel.setCurrentItem(endDate[1]);
			dayWheel.setCurrentItem(endDate[2] - 1);
		}
		
		//日期滑动监听事件
		OnWheelChangedListener wheelChangedListener = new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if(wheel == yearWheel){
					
					int year_num = newValue + START_YEAR;
					// 判断大小月及是否闰年,用来确定"日"的数据
					if (list_big.contains(String
							.valueOf(monthWheel.getCurrentItem() + 1))) {
						dayWheel.setAdapter(new NumericWheelAdapter(1, 31));
					} else if (list_little.contains(String.valueOf(monthWheel
							.getCurrentItem() + 1))) {
						dayWheel.setAdapter(new NumericWheelAdapter(1, 30));
					} else {
						if ((year_num % 4 == 0 && year_num % 100 != 0)
								|| year_num % 400 == 0){
							dayWheel.setAdapter(new NumericWheelAdapter(1, 29));
						}
						else{
							dayWheel.setAdapter(new NumericWheelAdapter(1, 28));
							if(dayWheel.getCurrentItem() > 27){
								dayWheel.setCurrentItem(27);
							}
						}
					}
					
					return ;
				}else if(wheel == monthWheel){
				
					int month_num = newValue + 1;
					// 判断大小月及是否闰年,用来确定"日"的数据
					if (list_big.contains(String.valueOf(month_num))) {
						dayWheel.setAdapter(new NumericWheelAdapter(1, 31));
					} else if (list_little.contains(String.valueOf(month_num))) {
						dayWheel.setAdapter(new NumericWheelAdapter(1, 30));
						if(dayWheel.getCurrentItem() > 29 ){
							dayWheel.setCurrentItem(29);
						}
					} else {
						//闰月
						if (((yearWheel.getCurrentItem() + START_YEAR) % 4 == 0 && (yearWheel
								.getCurrentItem() + START_YEAR) % 100 != 0)
								|| (yearWheel.getCurrentItem() + START_YEAR) % 400 == 0){
							dayWheel.setAdapter(new NumericWheelAdapter(1, 29));
							if(dayWheel.getCurrentItem() > 28){
								dayWheel.setCurrentItem(28);
							}
						}
						else{
							dayWheel.setAdapter(new NumericWheelAdapter(1, 28));
							if(dayWheel.getCurrentItem() > 27){
									dayWheel.setCurrentItem(27);
								}
						}
					}
					
					return ;
				}
			}
		};
		
		yearWheel.addChangingListener(wheelChangedListener);
		monthWheel.addChangingListener(wheelChangedListener);
		
		// 根据屏幕密度来指定选择器字体的大小
		int textSize = PublicMethodUtil.getInstance().px2dip(mContext, 14);

		yearWheel.TEXT_SIZE = textSize;
		monthWheel.TEXT_SIZE = textSize;
		dayWheel.TEXT_SIZE = textSize;
		
		final Button dateSubBT = (Button)view.findViewById(R.id.dialog_date_bottom_bt_ok);
		final Button dateCancelBT = (Button)view.findViewById(R.id.dialog_date_bottom_bt_cancel);
		
		OnClickListener dateClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v == dateSubBT){
					if(flag == 0){
						startDateTV.setText(getDateText(yearWheel.getCurrentItem(), 
								monthWheel.getCurrentItem(), dayWheel.getCurrentItem(), flag));
					}else{
						endDateTV.setText(getDateText(yearWheel.getCurrentItem(), 
								monthWheel.getCurrentItem(), dayWheel.getCurrentItem(), flag));
					}
					dateDialog.dismiss();
					return ;
				}
				else if(v == dateCancelBT){
					dateDialog.dismiss();
					return ;
				}
			}
		};
		
		dateSubBT.setOnClickListener(dateClickListener);
		dateCancelBT.setOnClickListener(dateClickListener);
		
		builder.setView(view);
		dateDialog = builder.create();
		dateDialog.setView(view, 0, 0, 0, 0);
		
		dateDialog.show();
	}
	

	/**
	 * 生成日期字符串
	 */
	private String getDateText(int yearCurrent, int monthCurrent, int dayCurrent, int flag){
		if(flag == 0){
			startDate[0] = yearCurrent + START_YEAR;
			startDate[1] = monthCurrent;
			startDate[2] = dayCurrent + 1;
		}else{
			endDate[0] = yearCurrent + START_YEAR;
			endDate[1] = monthCurrent;
			endDate[2] = dayCurrent + 1;
		}
		return (yearCurrent + START_YEAR) + "-" + 
				PublicMethodUtil.getInstance().makeDoubleNumber(monthCurrent + 1) + "-" + 
				PublicMethodUtil.getInstance().makeDoubleNumber(dayCurrent + 1);
	}
	
	/**
	 * 当前时间对话框是否在显示
	 * @return	true：显示	false：未显示
	 */
	public boolean getIsShowing(){
		if(dateDialog == null){
			return false;
		}
		return dateDialog.isShowing();
	}
}
