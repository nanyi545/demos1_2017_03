package com.webcon.sus.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webcon.sus.demo.R;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.entity.ImageNode;
import com.webcon.sus.entity.StationNode;
import com.webcon.sus.eventObjects.ErrorEvent;
import com.webcon.sus.eventObjects.MessageEvent;
import com.webcon.sus.eventObjects.RVrefreshEvent;
import com.webcon.sus.utils.AlarmMsgManager;
import com.webcon.sus.utils.CommunicationUtils;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.ApplicationManager;
import com.webcon.wp.utils.NotificationCancelManager;
import com.webcon.wp.utils.WPApplication;

import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * 详细报警消息页面
 * @author m
 */
public class AlarmDetailsActivityCompat extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "AlarmDetails";
    public static final String ALARM_DETAIL_FLAG = "alarmDetailedIntentFlag";
    public static final String ALARM_DETAIL_TEMP = "alarmTemp";
    public static final String ALARM_DETAIL_DATA = "alarmData";

    private Context mContext;
    private ImageView mImage;
    private TextView tv_title, tv_name, tv_date, tv_solveState, tv_solver, tv_solveDate,
            tv_positionValue, tv_loadingfailure;
    private RelativeLayout rl_solveState, rl_solver, rl_solveDate, rl_position;
    private ProgressBar mLoadingProgress;

    private int alarmId;
    private int intentFlag; // 跳转标识
    private int alarmTemp;  // 保留字段

    private AlarmNode mAlarm;
    private ImageButton bn_back;
    private Button bn_confirm_solve;
    private ImageNode imageNode;

    private com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar progressBar_confirm_solve;


    private boolean isProcessing = false;
    private boolean isQuit = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mLoadingProgress.setVisibility(View.GONE);
            switch (msg.what) {
                // 请求成功
                case 0:
                    mImage.setImageBitmap(imageNode.getBitmap());
                    break;
                // 请求失败
                case 1:
                    tv_loadingfailure.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        init();
    }

//    public void testFun(View view){
//        WPApplication.getInstance().printAlarmStatus(999);// show the status of the alarms
//    }

    private void init() {
        Log.i(TAG, "init");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.layout_alarm_details_compat);
        // 设置宽度
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = getScreenWidth();
        getWindow().setAttributes(p);

        mContext = this;

        tv_title = (TextView) findViewById(R.id.title_tv);
        tv_title.setText("报警详情");
        tv_name = (TextView) findViewById(R.id.tv_alarm_detail_name_value);
        tv_date = (TextView) findViewById(R.id.tv_alarm_detail_date_value);
//        tv_content = (TextView) findViewById(R.id.tv_alarm_detail_content_value);
        tv_solveState = (TextView) findViewById(R.id.tv_alarm_detail_solve_state_value);
        tv_solver = (TextView) findViewById(R.id.tv_alarm_detail_solver_value);
        tv_solveDate = (TextView) findViewById(R.id.tv_alarm_detail_solve_date_value);

        rl_solveState = (RelativeLayout) findViewById(R.id.rl_alarm_detail_solve_state);
        rl_solver = (RelativeLayout) findViewById(R.id.rl_alarm_detail_solver);
        rl_solveDate = (RelativeLayout) findViewById(R.id.rl_alarm_detail_solve_date);
        rl_position = (RelativeLayout) findViewById(R.id.rl_alarm_detail_position);

        tv_loadingfailure = (TextView) findViewById(R.id.tv_alarm_capture_loading_failure);
        tv_positionValue = (TextView) findViewById(R.id.tv_alarm_detail_positionType_value);
        mLoadingProgress = (ProgressBar) findViewById(R.id.progress_loading_capture);
        mImage = (ImageView) findViewById(R.id.iv_alarm_capture);
        mImage.setOnClickListener(this);

        bn_back = (ImageButton) findViewById(R.id.usermsg_back);
        bn_back.setOnClickListener(this);

        bn_confirm_solve = (Button) findViewById(R.id.bn_alarm_details_solver_confirm);
        bn_confirm_solve.setOnClickListener(this);
        progressBar_confirm_solve=(com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar)findViewById(R.id.mlp_loading_defence123);
//        progressBar_confirm_solve.setVisibility(View.VISIBLE);
//        Log.i("AAA","--"+(null==progressBar_confirm_solve)+"  button--"+(null==bn_confirm_solve));//+"   class:"+progressBar_confirm_solve.getClass().getName()

        //赋值
        parseDate(getIntent());
        ApplicationManager.getInstance().addActivity(this);
    }

    private void parseDate(Intent intent) {
        Log.i(TAG, "parseData");
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            Log.e(TAG, "no data found");
            return;
        }
        // 跳转标识 flag
        // 0:程序未启动的情况下从登陆页面直接跳转
        // 1:点击报警列表跳转
        // 2:程序在启动状态下点击通知跳转
        intentFlag = bundle.getInt(ALARM_DETAIL_FLAG, 0);
        // 保留字段
        alarmTemp = bundle.getInt(ALARM_DETAIL_TEMP, -1);
        // 报警数据
//        AlarmNode aNodeTemp= (AlarmNode) bundle.getSerializable(ALARM_DETAIL_DATA).clone();
        mAlarm = (AlarmNode) bundle.getSerializable(ALARM_DETAIL_DATA); // this is a copy of AlarmNode in WPapplication
//        Log.i("alarmRV","date:"+mAlarm.getAlarmDate()+"   is solved:"+mAlarm.getIsSolved()+"   station id"+mAlarm.getStationId()+"  hasPicture:"+mAlarm.isCapture());
//        Log.i("alarmRV","equal same memory address?"+(mAlarm==WPApplication.getInstance().getSameNode(mAlarm)));
//        Log.i("alarmRV","equal is same alarm?"+(mAlarm.isSameAlarm( WPApplication.getInstance().getSameNode(mAlarm))));

        if (mAlarm == null) {
            Log.e(TAG, "alarm not found");
            return;
        } else {
            mAlarm.printOut();
            alarmId = mAlarm.getId();
        }
        initCaptureState(mAlarm);
        assignText(mAlarm);
    }

    /**
     * 赋值
     * @param alarm 报警消息实体
     */
    private void assignText(AlarmNode alarm) {
        // 报警设备名称
        if (alarm.getDeviceName() != null && !alarm.getDeviceName().equals("")) {
            tv_name.setText(alarm.getDeviceName());
        } else {
            tv_name.setText(getResources().getString(R.string.str_notifi_alarm_tv_unknow));
        }

        // 报警日期
        if (alarm.getAlarmDate() != null && !alarm.getAlarmDate().equals("")) {
            tv_date.setText(alarm.getAlarmDate());
        } else {
            tv_date.setText(getResources().getString(R.string.str_notifi_alarm_tv_unknow));
        }

        // 位置类型
        if (alarm.getPositionType() == SUConstant.POSITION_MSG_TYPE_SEGMENT) {
            // 段位信息
            if (alarm.getPositionSegmentMsg() != -1) {
                tv_positionValue.setText("" + alarm.getPositionSegmentMsg());
            } else {
                tv_positionValue.setText(getResources().getString(R.string.str_notifi_alarm_tv_unknow));
            }
        } else {
            // 角度信息 或者其他
            rl_position.setVisibility(View.GONE);
        }

        if (isSolved(alarm)) {// 若消息已处理
            // 消息处理状态
            tv_solveState.setTextColor(getResources().getColor(R.color.Black));
            tv_solveState.setText("已处理");

            // 消息处理者
            if (alarm.getSolver() != null && !alarm.getSolver().equals("")) {
                tv_solver.setText(alarm.getSolver() + "");
            } else {
                tv_solver.setText(getResources().getString(R.string.str_notifi_alarm_tv_unknow));
            }
            // 消息处理时间
            if (alarm.getSolvedDateStr() != null) {
                tv_solveDate.setText(alarm.getSolvedDateStr());
            } else {
                tv_solveDate.setText(getResources().getString(R.string.str_notifi_alarm_tv_unknow));
            }

            // 显示
            rl_solver.setVisibility(View.VISIBLE);
            rl_solveDate.setVisibility(View.VISIBLE);
            // 隐藏按钮
            bn_confirm_solve.setVisibility(View.GONE);
            progressBar_confirm_solve.setVisibility(View.GONE);

        } else {// 消息未处理
            // 消息处理状态
            tv_solveState.setTextColor(getResources().getColor(R.color.Red_));
            tv_solveState.setText("未处理");

            // 隐藏
            rl_solver.setVisibility(View.GONE);
            rl_solveDate.setVisibility(View.GONE);

            // 显示按钮
            bn_confirm_solve.setVisibility(View.VISIBLE);
        }
    }

    private void initCaptureState(AlarmNode alarm){
        // 是否有抓图
        if (alarm.isCapture()) {
            Log.i(AlarmDetailsActivityCompat.TAG,"HAS PIC");
            mLoadingProgress.setVisibility(View.VISIBLE);
            tv_loadingfailure.setVisibility(View.GONE);
            new MyLoadingCaptureThread(alarm).start();
        } else {
            Log.i(AlarmDetailsActivityCompat.TAG,"NO PIC");
            tv_loadingfailure.setVisibility(View.VISIBLE);
            tv_loadingfailure.setText("无图片信息");
        }
    }

    private boolean isSolved(AlarmNode alarm) {
        return 1 == alarm.getIsSolved();
    }

    /* -------------------------------------- */

    /**
     * 返回关闭当前页面
     */
    private void pageBack() {
        isQuit = true;
        switch (intentFlag) {
            case SUConstant.APP_RUNNING_M:
                this.finish();
                overridePendingTransition(0, R.anim.page_bottom_out);
                break;
            default:
                intentToNewActivity(mContext, MainActivityCompat.class, AlarmDetailsActivityCompat.this, true);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        // 返回按钮
        if (v == bn_back) {
            pageBack();
        }
        if (v == bn_confirm_solve && !isProcessing) {

            progressBar_confirm_solve.setVisibility(View.VISIBLE);
            bn_confirm_solve.setText("处理中");

//            WPApplication.getInstance().printAlarmStatus(1);// show the status of the alarms

            isProcessing = true;
            // 请求处理报警消息
            // 通知的id
            mAlarm.setSolvedDate(new Date().getTime()); // /1000

//            WPApplication.getInstance().printAlarmStatus(2);// show the status of the alarms

            if (WPApplication.getInstance() != null) {
                mAlarm.setSolver(WPApplication.getInstance().getCurrentUser().getUserName());
            }
//            WPApplication.getInstance().printAlarmStatus(3);// show the status of the alarms


            ArrayList<AlarmNode> alarmList = new ArrayList<>();
            alarmList.add(mAlarm);
//            WPApplication.getInstance().printAlarmStatus(4);// show the status of the alarms

            AlarmMsgManager.getInstance().transmitCenter(   //solve one new alarm
                    AlarmMsgManager.ALARM_MANAGE_SOLVE_REQUEST, alarmList);

//            WPApplication.getInstance().printAlarmStatus(5);// show the status of the alarms

            int notificationID = -1;
            try {
                notificationID = mAlarm.getId();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
//            WPApplication.getInstance().printAlarmStatus(6);// show the status of the alarms

            if (notificationID != -1) {
                NotificationCancelManager.getInstance().clearOneNotification(
                        mContext, notificationID, NotificationCancelManager.TAG_ALARM);
            }
//            WPApplication.getInstance().printAlarmStatus(7);// show the status of the alarms

            //to refresh the RV in ALarmMainFragment
            RVrefreshEvent e1=new RVrefreshEvent();
            EventBus.getDefault().post(e1);
//            WPApplication.getInstance().printAlarmStatus(8);// show the status of the alarms
        }

        if (v == mImage) {
            Intent intent = new Intent(mContext, MonitorActivityCompat.class);
            Bundle bundle = new Bundle();
            bundle.putInt(MainStationListFragment.KEY_ID, mAlarm.getStationId());
            bundle.putString(StationDevicesFragment.KEY_DEVICE, mAlarm.getDeviceName());
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            pageBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void releaseHandler() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
    }

    /* -------------------- */
    @Override
    protected void onNewIntent(Intent intent) {
        Log.w(TAG, "on new intent");
        // 因为singleTask, 故在打开的情况下，点击新报警消息会由这里进入
        // 并且：onNewIntent ->  onRestart -> onStart -> onResume
        parseDate(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(imageNode != null){
            imageNode.release();
            imageNode = null;
        }
    }

    /**
     * 加载抓拍图片线程
     */
    private class MyLoadingCaptureThread extends Thread {
        private AlarmNode alarmNode;

        public MyLoadingCaptureThread(AlarmNode alarmNode) {
            this.alarmNode = alarmNode;
            Log.i(TAG,"alarm date:"+this.alarmNode.getAlarmDate()+"  device name:"+this.alarmNode.getDeviceName()+"  station id int:"+this.alarmNode.getStationId()+" alarm id"+this.alarmNode.getId());
        }

        @Override
        public void run() {
            if(WPApplication.getInstance().DEBUG_NO_ALARM){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            imageNode = CommunicationUtils.getInstance().requestAlarmCapture(alarmNode);
            if(isQuit){   //退出？？
                if(imageNode != null){
                    imageNode.release();
                    imageNode = null;
                }
            }else if (imageNode != null && imageNode.getBitmap() != null){
                if(handler != null){
                    handler.sendEmptyMessage(0);
                }
            }else{
                if(handler != null){
                    handler.sendEmptyMessage(1);
                }
            }
        }
    }


    public void onEventMainThread(ErrorEvent event) {
        if (event.getType() == ErrorEvent.ERROR_EVENT_TYPE_CAPTURE_OVERTIME){
            tv_loadingfailure.setVisibility(View.VISIBLE);
            tv_loadingfailure.setText("图片传输超时，请检查网络连接并查询服务器状态");
        }
    }

    public void onEventMainThread(MessageEvent event) {
        if (WPApplication.DEBUG) {
            Log.w(TAG, "get message event");
        }
        if (event.getType() == MessageEvent.ALARM_FLAG_REFRESH && event.alarmId == alarmId) {
            isProcessing = false;
            if (event.success) {
                StationNode node = WPApplication.getInstance().getStationNode(mAlarm.getStationId());
                if (node == null) {
                    return;
                }
                AlarmNode alarm = node.getAlarmNode(alarmId);  // 重设alarm----->自动显示第一个新的alarm ???
                if (alarm != null) {
                    mAlarm = alarm;
                    assignText(alarm);
                    Log.i("alarmRV","---refresh??");
                }
            } else {
                bn_confirm_solve.setVisibility(View.VISIBLE);
            }
        }
    }

}
