package com.webcon.sus.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webcon.sus.adapter.MainPagerAdapter;
import com.webcon.sus.demo.R;
import com.webcon.sus.eventObjects.FABevent;
import com.webcon.sus.eventObjects.MessageEvent;
import com.webcon.sus.service.CService;
import com.webcon.sus.utils.AlarmMsgManager;
import com.webcon.sus.utils.BitmapUtil;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.ApplicationManager;
import com.webcon.wp.utils.EncrypAES;
import com.webcon.wp.utils.NotificationCancelManager;
import com.webcon.wp.utils.PublicMethodUtil;
import com.webcon.wp.utils.WPApplication;
import com.webcon.wp.utils.WPConstant;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 主界面
 * @author m
 */
public class MainActivityCompat extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    // indicator to switch to alarm fragment:  viewPager.setCurrentItem(1);
    public static final String START_ALARM_FRAG="start_alarm_fragment";
    private boolean startAlarmFrag=false;

    private static final String TAG = "MainActivityCompat";
    public static final String SERVICE_TAG  = "cservice intent";

    private Context mContext;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    // 页面切换按钮
    private Button bn_stations, bn_alarms;
    // 显示新报警个数
    private TextView newAlarmNum;
    // 注销loading对话框
    private ProgressDialog cancellationDialog;

    // 滑动下标view
    private ImageView cursorIV;
    // 图片偏移量
    private int cursorOffset = 0;
    // 当前页卡编号
    private int cursorIndex = 0;
    // 图片宽度
    private int cursorImgWidth = 0;

    //Fragment instance
    private MainStationListFragment stationList;
    private AlarmMainFragment alarmList;
    //drawer
    private DrawerLayout drawerLayout;

    //Toolbar
    private Toolbar mToolbar;
    private AppBarLayout mAppBar;
    private CollapsingToolbarLayout mCollapsingBar;

    // 考虑使用NavigationView，但是menu的字体大小不好设置
    private NavigationDrawerFragment mNavigation;

    private TextView testTV;
    private android.support.design.widget.FloatingActionButton fab;

    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 注销结束（关闭注销的progressDialog）
                case -11:
                    if (cancellationDialog.isShowing()) {
                        cancellationDialog.dismiss();
                    }
                    break;

//                //drawer 中点击-->跳转到个人信息界面
//                case 0:
//                    Intent intent1 = new Intent(mContext, PersonalInformationActivity.class);
//                    startActivity(intent1);
                case -10000:
                    drawerLayout.closeDrawers();  // 延时以后(显示ripple)-->关闭drawer

                    Intent intent1 = new Intent(mContext, PersonalInformationActivity.class);
                    startActivity(intent1);
                    break;
                case -10001: //drawer 中点击-->登出
                    drawerLayout.closeDrawers();  // 延时以后(显示ripple)-->关闭drawer
                    logout();
                    break;
                case -10002: //drawer 中点击-->退出
                    drawerLayout.closeDrawers();  // 延时以后(显示ripple)-->关闭drawer
                    WPApplication.flag_logout = false;
                    new ExitThread().start();
                    break;



            }
        }

    };





    @Override
    public void releaseHandler(){
        mainHandler.removeCallbacksAndMessages(null);
        mainHandler = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(WPApplication.DEBUG){
//            Log.i(TAG, "-- onCreate --");
//        }
//        Log.i("AAA","Main --oncreate "+WPApplication.getInstance().getNewAlarms());
//        Log.i("FAB", "main activity:" + Thread.currentThread().getName());
        Log.i(TAG, "-- onCreate --");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_compat);
        mContext = this;
        EventBus.getDefault().register(this);
        initView();
        initFragment(savedInstanceState);
        ApplicationManager.getInstance().addActivity(this);
        WPApplication.getInstance().appFlag = SUConstant.APP_RUNNING_M;

//        //------------debug-----------------
//        testTV= (TextView) findViewById(R.id.test_tv);
//        testTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("AAA", "STATION numbers" + WPApplication.getInstance().getStationList().size());
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        CommunicationUtils.getInstance().refreshAllStationInformationTest(null);
//                    }
//                }).start();
//
//            }
//        });
    }

    private void initView(){
        mAppBar = (AppBarLayout)findViewById(R.id.layout_main_appbar);
        mToolbar = (Toolbar)findViewById(R.id.toolbar_main);
        drawerLayout=(DrawerLayout) findViewById(R.id.navigation_drawerlayout);

        setSupportActionBar(mToolbar); // 加上以后没有actionBar效果？？

        mCollapsingBar = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
//        mCollapsingBar.setTitle("站场无人值守系统");
//        mCollapsingBar.setCollapsedTitleTextAppearance(R.style.style_title_collapsed);
//        mCollapsingBar.setExpandedTitleTextAppearance(R.style.style_title_expanded);
        // 注销的progressdialog
        cancellationDialog = PublicMethodUtil.getInstance().makeProgressDialog(
                mContext, R.string.str_null, R.string.str_loading_cancellation, false, false);
        initNavigation();
        initCursorImage();
        initTextView();
    }

    private void initNavigation(){
        mNavigation = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

//        //TODO CHANGE THE ICON!! //why doesn't work
//        mToolbar.setNavigationIcon(R.drawable.icon_android_yellow);

        //setUp() links the navigation drawer to the icon on the toolbar ---
        mNavigation.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.navigation_drawerlayout), mToolbar);
    }
    //---------------

    /**
     * 初始化Fragment
     * @param savedInstanceState 保存的状态
     */
    private void initFragment(Bundle savedInstanceState) {
        if (findViewById(R.id.main_vp_pager) != null) {
            // 如果从先前的状态恢复，那么不需要做任何事，直接返回
            if (savedInstanceState != null) {
                return;
            }
        }
        stationList = new MainStationListFragment();
        alarmList = new AlarmMainFragment();
        fragments.add(stationList);
        fragments.add(alarmList);
        initViewPager();
    }

    /**
     * 初始化界面元素
     */
    private void initViewPager() {

        viewPager = (ViewPager) findViewById(R.id.main_vp_pager);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new MyPageChangeListener());

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
                                        AlarmMsgManager.getInstance().transmitCenter(AlarmMsgManager.ALARM_MANAGE_CLEAR, null);
                                    }
                                }).show();
            }
        });


    }

    /**
     * 初始化游标
     */
    private void initCursorImage() {
        Bitmap mp = BitmapUtil.overlapBitmapColor(BitmapFactory.decodeResource(
                getResources(), R.drawable.bg_mutual_main_cursorimg),
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
    private void initTextView() {
        newAlarmNum = (TextView) findViewById(R.id.main_tv_tab_newalarmnum);
        bn_stations = (Button)findViewById(R.id.main_bn_tab_userlist);
        bn_alarms = (Button)findViewById(R.id.main_bn_tab_alarmlist);
        bn_stations.setOnClickListener(mClickListenr);
        bn_alarms.setOnClickListener(mClickListenr);
        adjustAlarmImage();
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
    private View.OnClickListener mClickListenr = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if(bn_stations == v){
                viewPager.setCurrentItem(0);
            }
            if(bn_alarms == v){
                viewPager.setCurrentItem(1);
            }
        }
    };


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            //跳转到个人信息界面
            case 0:
//                Intent intent1 = new Intent(mContext, PersonalInformationActivity.class);
//                startActivity(intent1);

                new Thread(new Runnable(){   //延时300ms()，执行跳转
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mainHandler.sendEmptyMessage(-10000);
                    }
                }).start();
                break;
            // 注销账号，重新登录
            case 1:

                new Thread(new Runnable(){   //延时300ms()，执行注销账号
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mainHandler.sendEmptyMessage(-10001);
                    }
                }).start();

                break;
            // 退出
            case 2:
                new Thread(new Runnable(){   //延时300ms()，执行退出
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mainHandler.sendEmptyMessage(-10002);
                    }
                }).start();
                break;
        }
    }

    /**
     * ViewPager滑动监听事件
     */
    private class MyPageChangeListener implements OnPageChangeListener {

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
     * ##TODO 监听按键事件：返回键的处理
     */
    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0){
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                // 关闭侧滑菜单
                if(mNavigation != null && mNavigation.isDrawerOpen()){
                    mNavigation.closeDrawer();
                    return true;
                }
                // 退出判断：a.退出主界面 b.退出站场列表操作 c.退出消息列表操作
                int back = WPApplication.getInstance().getKeyBackFlag();
                if(back == SUConstant.KEYBACK_TYPE_CLEAR){
                    // 退出主界面
                    finish();
                }else{
                    // 退出站场列表的操作    ---暂无
                    if((back & 0x0F) == SUConstant.KEYBACK_TYPE_STATIONS){
                        stationList.onBackPressed();
                    }
                    // 退出消息列表的操作    ---暂无
                    if((back & 0xF0) == SUConstant.KEYBACK_TYPE_ALARMS){
                        alarmList.onBackPressed();
                    }
                }
                // 屏蔽返回键
                return true;
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                // 没有响应？
                mNavigation.openDrawer();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    // select fragment based on the intent   from notification-->viewPager.setCurrentItem(1);
    private void selectFragment(Intent mIntent){
        //---- check if the activity is started by the notification???
        Bundle mBundle;
        if (null!=mIntent){
            try {
                mBundle=mIntent.getExtras();
                startAlarmFrag=(mBundle.getString(this.START_ALARM_FRAG).equals(this.START_ALARM_FRAG));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        //----end of check if the activity is started by the notification???
        if (startAlarmFrag) {viewPager.setCurrentItem(1);startAlarmFrag=false;}
        else
            viewPager.setCurrentItem(0);
        drawerLayout.closeDrawers(); //close drawers
    }




    @Override
    protected void onResume() {
        if(WPApplication.DEBUG){
            Log.i(TAG, "-- onResume --");
        }
        super.onResume();
        if(mAppBar != null && stationList != null){
            mAppBar.addOnOffsetChangedListener(stationList);
        }
        Log.i(TAG,"on resume");
        refreshAlarmDisplay();

    }

    @Override
    public void onPostResume(){
        super.onPostResume();
    }

    /**
     * Activity已经存在，再次跳转到当前Activty调用此方法
     */
    @Override
    protected void onNewIntent(Intent intent) {
        if(WPApplication.DEBUG){
            Log.i(TAG, "on new Intent");
        }
        if(intent != null){
            if(intent.getIntExtra(SERVICE_TAG, -1) != -1){
                if(WPApplication.DEBUG){
                    Log.i(TAG, "--------intent from service-----");
                }
            }
        }
        selectFragment(intent);  // select fragment based on new intent
        super.onNewIntent(intent);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mAppBar != null && stationList != null){
            mAppBar.removeOnOffsetChangedListener(stationList);
        }
    }

    @Override
    protected void onDestroy() {
        if(WPApplication.DEBUG){
            Log.w(TAG, "----------- Main Destroy -----------");
        }
        EventBus.getDefault().unregister(this);
        WPApplication.getInstance().appFlag = SUConstant.APP_EXIT_MAIN;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_compat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 退出线程
     */
    class ExitThread extends Thread {
        @Override
        public void run() {
            if(WPApplication.DEBUG){
                Log.w(TAG, "----Exit Thread start----");
            }

            // 清除所有报警：TODO：通知管理
            NotificationCancelManager.getInstance().clearAllNotification(mContext);
            // 停止后台服务：TODO：清理工作
            stopService(new Intent(mContext, CService.class));

            /* 退出的处理 */
            if (WPApplication.flag_logout) {
                // 跳转到登录页面
                Intent loginIntent = new Intent(mContext, LoginActivityCompat.class);
                // flag == 1
                loginIntent.putExtra(WPConstant.INTENT_LOGIN_FLAG, 1);
                startActivity(loginIntent);
                // 关闭注销的progressDialog
                mainHandler.sendEmptyMessage(-11);
                // finish
                MainActivityCompat.this.finish();
            } else {
                // 退出应用
                ApplicationManager.getInstance().applicationExit();
                ApplicationManager.getInstance().applicationOtherExit();
            }
        }
    }

    /**
     * 注销操作
     */
    private void logout(){
        PublicMethodUtil.getInstance().makeAlertDialogBuilder(
                mContext,
                R.string.dialog_title_prompt,
                R.string.str_main_dialog_cancellation_message,
                R.string.btn_ok,
                R.string.btn_cancle,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 修改自动登陆标识
                        wpPreferences.edit().putString(
                                EncrypAES.getInstance().encrypt(WPConstant.AUTO_LOGIN),
                                EncrypAES.getInstance().encrypt("0")).apply();
                        // 修改注销标识
                        WPApplication.flag_logout = true;
                        // 启动注销的progressdialog
                        if (!cancellationDialog.isShowing()) {
                            cancellationDialog.show();
                            //启动退出线程
                            new ExitThread().start();
                        }
                    }
                }, null).show();
    }

    /**
     * 显示新报警消息的数量
     */
    private void refreshAlarmDisplay(){
        //获取全局变量
        int num = WPApplication.getInstance().correctNewAlarms();
        Log.i(TAG, "new Alarm:" + num);

        if (num > 0) {
            newAlarmNum.setVisibility(View.VISIBLE);
            newAlarmNum.setText(num + "");
        } else {
            newAlarmNum.setVisibility(View.GONE);
        }
    }

    /**
     * EventBus对消息事件的监听
     */
    public void onEventMainThread(MessageEvent event){
        //所有对消息列表的操作都会引起变化
        refreshAlarmDisplay();
    }


    /**
     * 监听fragment 控制eventbus
     */
    public void onEventMainThread(FABevent event){
        switch (event.getStatus()){
            case FABevent.FAB_SHOW:
                fab.animate().translationY(0).setInterpolator(new AccelerateInterpolator(0.1f)).start();
                break;
            case FABevent.FAB_HIDE:
                fab.animate().translationY(400).setInterpolator(new AccelerateInterpolator(2)).start();
                break;
        }
    }


}
