package com.webcon.sus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.webcon.sus.activities.AlarmDetailsActivityCompat;
import com.webcon.sus.activities.MainActivityCompat;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.WPApplication;
import com.webcon.wp.utils.WPConstant;

/**
 * 点击notification触发的广播
 * @author Vieboo
 * ##TODO: 通知的管理。。。。
 */
public class NotificationClickReceiver extends BroadcastReceiver {
    private static final String TAG = "Notification";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Bundle bundle = intent.getExtras();
		//报警通知点击触发事件
		if(action.equals(WPConstant.ALARM_NOTIFICATION_CLICK_ACTION)){
            //保留字段
			int pMid = bundle.getInt(AlarmDetailsActivityCompat.ALARM_DETAIL_TEMP);
            //报警数据
			AlarmNode alarmInfo = (AlarmNode) bundle.getSerializable(
                    AlarmDetailsActivityCompat.ALARM_DETAIL_DATA);
			Intent mIntent = new Intent();
			Bundle mBundle = new Bundle();

			//0:当前程序没有运行	1:当前程序有运行Main		2：当前程序只运行了AlarmDetailed
//            int appStatus = PublicMethodUtil.getInstance().getRunningProcess(context);

            if(WPApplication.getInstance() == null){
                // ``·..(;¬_¬)
                Log.e("Error", "Application不存在");
            }else{
                int appState = WPApplication.getInstance().appFlag;
                switch(appState){
                    case SUConstant.APP_RUNNING_M:
                        if(WPApplication.DEBUG){
                            Log.i(TAG, "APP_RUNNING_M");
                        }
//                        // 启动alarmDetailActivity
//                        mBundle.putInt(AlarmDetailsActivityCompat.ALARM_DETAIL_FLAG, appState);
//				        mIntent.setClass(context, AlarmDetailsActivityCompat.class);
                        // 启动 MainActivityCompat
                        mBundle.putString(MainActivityCompat.START_ALARM_FRAG, MainActivityCompat.START_ALARM_FRAG);
				        mIntent.setClass(context, MainActivityCompat.class);
                        break;
                    case SUConstant.APP_EXIT_MAIN:
                        if(WPApplication.DEBUG){
                            Log.i(TAG, "APP_EXIT_MAIN");
                        }
                        // 跳转到登陆页面的标识 		1:注销登陆跳转		2:点击报警通知跳转
                        mBundle.putInt(WPConstant.INTENT_LOGIN_FLAG, 2);    // FIXME: 多余了
                        // 登陆跳转的标识	0：正常登陆到主页面	1：跳转到报警详细信息页面
                        mBundle.putInt("loginIntentType", 1);
                        mBundle.putInt(AlarmDetailsActivityCompat.ALARM_DETAIL_FLAG, appState);
                        // 直接显示详细报警页面，根据推断不用经过Login（所以Login页面逻辑也可以改了）
                        mIntent.setClass(context, AlarmDetailsActivityCompat.class);
//                        mIntent.setClass(context, LoginActivityCompat.class);
                        WPApplication.getInstance().appFlag = SUConstant.APP_EXIT_DETAIL;
                        break;
                    case SUConstant.APP_EXIT_DETAIL:
                        if(WPApplication.DEBUG){
                            Log.i(TAG, "APP_EXIT_DETAIL");
                        }
                        mBundle.putInt(AlarmDetailsActivityCompat.ALARM_DETAIL_FLAG, appState);
                        mIntent.setClass(context, AlarmDetailsActivityCompat.class);
                        break;
                    default:
                        if(WPApplication.DEBUG){
                            Log.e("Error", "未知应用状态");
                        }
                        break;
                }
            }

			//保留字段
			mBundle.putInt(AlarmDetailsActivityCompat.ALARM_DETAIL_TEMP, pMid);
			//报警数据
			mBundle.putSerializable(AlarmDetailsActivityCompat.ALARM_DETAIL_DATA, alarmInfo);

			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mIntent.putExtras(mBundle);

			//启动Activity
            context.startActivity(mIntent);

		}

	}

}
