package com.webcon.sus.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webcon.sus.adapter.StationPagerAdapter;
import com.webcon.sus.demo.R;
import com.webcon.sus.entity.StationNode;
import com.webcon.sus.eventObjects.MessageEvent;
import com.webcon.sus.eventObjects.StationEvent;
import com.webcon.sus.utils.AlarmMsgManager;
import com.webcon.sus.utils.BitmapUtil;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.ApplicationManager;
import com.webcon.wp.utils.WPApplication;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 站场主界面，包含设备列表和报警列表
 * @author m
 */
public class StationDetailsActivity extends BaseActivity {
	//-------------
    private static final String TAG = "StationDetailsActivity";

	//-------------
	private Context mContext;
	private int stationId;
	
	private ViewPager viewPager;
	private StationPagerAdapter mAdapter;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private LinearLayout mToolBar;
    private Button bn_devices, bn_alarms;
    private ImageButton bn_back;
    private int initPage = 0;
	
	/* 报警信息相关 */
	// 显示新报警个数
    private TextView newAlarmNum;
	// cursorImage
    private ImageView cursorIV;
    // 图片偏移量
    private int cursorOffset = 0;
    // 当前页卡编号
    private int cursorIndex = 0;
    // 图片宽度
    private int cursorImgWidth = 0;

    private StationNode mStation;
	private StationDevicesFragment mDeviceFragment;
	private AlarmSecFragment mAlarmFragment;

    private android.support.design.widget.FloatingActionButton fab;

	//----------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
		mContext = this;
		EventBus.getDefault().register(this);
        initData(getIntent());
		initView();
    }

    private void initData(Intent intent){
        Log.w(TAG, "init Data");
        Bundle bundle = intent.getExtras();
        if(bundle == null){
            errorQuit(1001);
        }else{
            stationId = bundle.getInt(MainStationListFragment.KEY_ID, -1);
            initPage = bundle.getInt(MainStationListFragment.KEY_PAGE, initPage);
            mStation = WPApplication.getInstance().getStationNode(stationId);
            Log.i("dataTest", "StationDetails--to--- stationId:" + stationId);
            //Test:
            Log.i(TAG, "stationId:" + stationId);
            if(stationId == -1){
                errorQuit(1002);
                finish();
            }else if(mStation == null){
                errorQuit(1010);
                finish();
            } else if(stationId != mStation.getId()){
                errorQuit(1003);
                finish();
            }
        }
    }
	
	private void initView(){
		setContentView(R.layout.layout_station_details);
        mToolBar = (LinearLayout)findViewById(R.id.toolbar_station);

        mDeviceFragment = new StationDevicesFragment();
        mAlarmFragment = new AlarmSecFragment();
        initFragmentArg();
        fragments.add(mDeviceFragment);
        fragments.add(mAlarmFragment);

        //##TODO newAlarm
        newAlarmNum = (TextView) findViewById(R.id.main_tv_tab_newalarmnum);
        setAlarmText();

        bn_devices = (Button)findViewById(R.id.main_bn_tab_userlist);
        bn_alarms = (Button)findViewById(R.id.main_bn_tab_alarmlist);
        bn_devices.setOnClickListener(mClickListener);
        bn_alarms.setOnClickListener(mClickListener);

        bn_back = (ImageButton)findViewById(R.id.bn_station_back);
        bn_back.setOnClickListener(mClickListener);

        ((TextView) findViewById(R.id.tv_station_head_name)).setText(mStation.getName());
//        ((TextView) findViewById(R.id.tv_station_head_detail)).setText("ID:" + getNode(stationId).getId());

        //---
        if(!mStation.isDefend()){
            changeStateColor(SUConstant.COLOR_HEAD_GREY_SEC);
        }else{
            changeStateColor(SUConstant.COLOR_HEAD_BLUE_SEC);
        }

        adjustAlarmImage();
        initCursorImage();
        initViewPager();
        ApplicationManager.getInstance().addActivity(this);
    }
	
	/**
     * 初始化动画
     */
    private void initCursorImage() {
        Bitmap mp = BitmapUtil.overlapBitmapColor(
                BitmapFactory.decodeResource(getResources(), R.drawable.bg_mutual_main_cursorimg),
                getResources().getColor(R.color.White));
        cursorIV = (ImageView) findViewById(R.id.main_iv_cursor);
        cursorIV.setImageBitmap(mp);
        cursorImgWidth = mp.getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screentWidth = dm.widthPixels;
        cursorOffset = (screentWidth / 2 - cursorImgWidth) / 2;
        Matrix matrix = new Matrix();
        matrix.postTranslate(cursorOffset, 0);
        cursorIV.setImageMatrix(matrix);
    }

	/**
     * 初始化界面元素
     */
    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.main_vp_pager);
        mAdapter = new StationPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new MyPageChangeListener());
        viewPager.setCurrentItem(initPage);

        fab=(FloatingActionButton) findViewById(R.id.fab_support);
        if (viewPager.getCurrentItem()==0) fab.hide();
        else fab.show();
        //dialog confirming whether to clear the alarms-------------
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(new ContextThemeWrapper(mContext,R.style.DialogThemeMaterial))  // use dialog theme
//                        .setTitle(R.string.dialog_title_hint)
                        .setMessage(R.string.dialog_warning)
                        .setCancelable(false)
                        .setNegativeButton("否",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // clear alarm list
                                        Log.i("FAB", "POSITIVE CLICK");
                                        AlarmMsgManager.getInstance().transmitCenter(AlarmMsgManager.ALARM_MANAGE_CLEAR_STATION, mStation.getAlarmList());
                                    }
                                }).show();
            }
        });




    }

    private void initFragmentArg(){
        Bundle bundle = new Bundle();
        bundle.putInt(MainStationListFragment.KEY_ID, stationId);
        mDeviceFragment.setArguments(bundle);
        mAlarmFragment.setArguments(bundle);
    }

    private void adjustAlarmImage(){
        int quartWidth = dm.widthPixels >> 2;
        int textWidth = (int)bn_alarms.getPaint().measureText((String)bn_alarms.getText());
        int rightMargin = quartWidth - textWidth/2 - getDimensionPixel(R.dimen.dimen_alarm_image_width) + 4;
        int topPadding = getDimensionPixel(R.dimen.dimen_tab_padding_top) - 4;
        int topMargin = topPadding > 0 ? topPadding : 0;
        // 设置布局
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)newAlarmNum.getLayoutParams();
        //  in pixels
        layoutParams.setMargins(0, topMargin, rightMargin, 0);
        newAlarmNum.setLayoutParams(layoutParams);
    }
	
	/**
     * 头标点击监听
     */
    private View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(v == bn_devices){
                initPage = 0;
                viewPager.setCurrentItem(initPage);
            }else if(v == bn_alarms){
                initPage = 1;
                viewPager.setCurrentItem(initPage);
            }else if(v == bn_back){
//                mAlarmFragment.onBackPressed();
                mDeviceFragment.onBackPressed();
                finish();
                overridePendingTransition(android.R.anim.fade_in, R.anim.page_right_out);
            }
        }
    };

//    private StationNode getNode(int stationId){
//        return WPApplication.getInstance().getStationNode(stationId);
//    }

    private void setAlarmText(){
        Log.i("CLEARSTATION","setAlarmText");
        int num = mStation.getNewAlarm();
        if(num > 0){
            newAlarmNum.setVisibility(View.VISIBLE);
            newAlarmNum.setText("" + num);
        }else{
            newAlarmNum.setVisibility(View.GONE);
        }
    }
	
	/**
     * ViewPager滑动监听事件
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = cursorOffset * 2 + cursorImgWidth; // 卡1 --> 卡2 偏移量

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                // 移动到0
                case 0:
                    if (cursorIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    }
                    fab.hide();
                    break;
                // 移动到1
                case 1:
                    if (cursorIndex == 0) {
                        animation = new TranslateAnimation(cursorOffset, one, 0, 0);
                    }
                    fab.show();
                    break;
            }
            cursorIndex = arg0;
            if(animation != null){
                animation.setFillAfter(true); // 图片停在动画停止位置
                animation.setDuration(300);
                cursorIV.startAnimation(animation);
            }
        }
    }
	

	/**
     * ##TODO #3 监听按键事件
     */
    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        //FIXME: 返回处理，关于保存状态的处理
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0){
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//                mAlarmFragment.onBackPressed();
                mDeviceFragment.onBackPressed();
                finish();
                overridePendingTransition(android.R.anim.fade_in, R.anim.page_right_out);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
	
	
	/**
     * Activity已经存在，再次跳转到当前Activty调用此方法 切换到报警列表标签
     */
    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, "----OnNewIntent----");
        initData(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void releaseHandler(){

    }
	
	//--------------------
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
    public void onPause(){
		super.onPause();
	}
	
	@Override
	public void onDestroy(){
        EventBus.getDefault().unregister(this);
		super.onDestroy();
	}
	
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_station_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_settings:
				break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 报警消息事件
     * @param event
     */
	public void onEventMainThread(MessageEvent event){
        Log.i("CLEARSTATION","receive message event--"+event.getType());

		switch(event.getType()){
            case MessageEvent.ALARM_FLAG_REFRESH:
                Log.i("CLEARSTATION","event.stationId"+event.stationId+"-----stationId"+stationId);
                if(event.stationId == stationId){
                    setAlarmText();
                }
                break;
            case MessageEvent.ALARM_SWIPE_DELETED:  // reset number when alarm is swipe deleted
                if(event.stationId == stationId){
                    setAlarmText();
                }
                break;

            default:
                break;
		}
		
	}

    /**
     * 站场消息事件
     */
    public void onEventMainThread(StationEvent event){
        switch(event.getType()){
            case StationEvent.STATION_EVENT_REFRESH:
                if(event.msg == SUConstant.FLAG_STATION_OPENED){
                    changeStateColor(SUConstant.COLOR_HEAD_BLUE_SEC);
                }else if(event.msg == SUConstant.FLAG_STATION_CLOSED){
                    changeStateColor(SUConstant.COLOR_HEAD_GREY_SEC);
                }
                break;
            case StationEvent.STATION_EVENT_ONLINE_STATE_CHANGED:
                // 当前在线状态改变，则退出该站场页面
                if(!mStation.isOnline()){
                    Log.i("stationChange","当前在线状态改变，则退出该站场页面");
                    String no = "站场[" + mStation.getName() + "]当前已离线！！";
                    Toast.makeText(this, no, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case StationEvent.STATION_EVENT_EXCEPTION:
                break;
        }
    }

    private void changeStateColor(int color){
        mToolBar.setBackgroundColor(color);
        bn_alarms.setBackgroundColor(color);
        bn_devices.setBackgroundColor(color);
//        mToolBar.setBackgroundColor(getResources().getColor(R.color.Red400));
//        bn_alarms.setBackgroundColor(getResources().getColor(R.color.Red400));
//        bn_devices.setBackgroundColor(getResources().getColor(R.color.Red400));
//        Log.i("DEFENSE", "400" + getResources().getColor(R.color.BlueGrey400));
//        Log.i("DEFENSE", "900" + getResources().getColor(R.color.BlueGrey900));
//        Log.i("DEFENSE", "300" + getResources().getColor(R.color.BlueGrey300));
//        Log.i("DEFENSE", "200" + getResources().getColor(R.color.BlueGrey200));
//        Log.i("DEFENSE", "300" + getResources().getColor(R.color.Grey300));
//        Log.i("DEFENSE", "400" + getResources().getColor(R.color.Grey400));
//        Log.i("DEFENSE", "500" + getResources().getColor(R.color.Grey500));
//        Log.i("DEFENSE", "450" + getResources().getColor(R.color.Grey450));
    }
}
