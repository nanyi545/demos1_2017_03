package com.webcon.sus.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hikvision.netsdk.HCNetSDK;
import com.webcon.g722.TransferDecoding;
import com.webcon.sus.demo.R;
import com.webcon.sus.entity.BaseDevice;
import com.webcon.sus.entity.MonitorNode;
import com.webcon.sus.entity.StationNode;
import com.webcon.sus.eventObjects.MonitorEvent;
import com.webcon.sus.eventObjects.StationEvent;
import com.webcon.sus.media.SelfVideoDataBuffer2;
import com.webcon.sus.utils.AudioUtils;
import com.webcon.sus.utils.CommunicationUtils;
import com.webcon.sus.utils.IVideoCallback;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.ApplicationManager;
import com.webcon.wp.utils.PublicMethodUtil;
import com.webcon.wp.utils.WPApplication;

import org.MediaPlayer.PlayM4.Player;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;
import vieboo.test.record.utils.IRecordData;

/**
 * 视频预览界面
 * <p>包括监控视频预览，云台控制，语音喊话等</p>
 *
 * @author m
 */
public class MonitorActivityCompat extends BaseActivity implements View.OnClickListener,
        View.OnTouchListener, IRecordData, IVideoCallback, SurfaceHolder.Callback {

    public static final String TAG = "Mornitor";
    private static final int WAITING_TIME = 20000;   //20000


    private static int mPlayerPort = 0;
    private final int bg_on = R.drawable.selector_monitor_voice;
    private final int bg_off = R.drawable.selector_monitor_voice_2;
    private final int bg_ctrl = R.drawable.selector_monitor_controller;
    private final int tx_show = R.color.monitor_text;
    private final int tx_dim = R.color.monitor_text_dim;
    private final int tx_on = R.color.monitor_text_on;
    private final boolean DEBUG = WPApplication.DEBUG;

    private Context mContext;
    private int stationId;
    private String deviceName;
    private MonitorNode mDevice;

    private Player mPlayer;
    private AudioUtils mAudioUtils;
    private SelfVideoDataBuffer2 mAudioBuffer;

    private SurfaceView mSurfaceView;
    /**
     * 退出按钮
     */
    private ImageButton bn_quit;
    /**
     * 增减控制按钮容器
     */
    private LinearLayout linear_controller;
    /**
     * 摄像头移动表盘
     */
    private RelativeLayout rl_menu;
    /**
     * 报警语音列表
     */
    private ListView mTrackListView;
    private ArrayAdapter<Integer> mTrackAdapter;
    private boolean haveTrackList = false;

    private Button bn_track, bn_speaker, bn_openMenu, bn_closeMenu,
            bn_move_up, bn_move_down, bn_move_right, bn_move_left,
            bn_sharp, bn_zoom, bn_aperture, bn_increase, bn_decrease;

    private boolean isMenu, isControl, isTracking, isSpeaking, callAllowed;
    // only when callAllowed==true speaking is allowed, callAllowed 常false,请求服务器同意后才能为true
    // call allowed states,determined by server
    private static final int CALL_ALLOWED = 1;
    private static final int CALL_NOT_ALLOWED = 0;
    private static final int CALL_REQ_FAIL = 2;
    private static final int CALL_REQ_INIT = 3;

    private int ptzType = -1;
    private Button temp;
    private int color_show, color_dim, color_on;

    private ProgressDialog mLoading;
    private Timer mTimer;
    RequestFailedTask rft1=new RequestFailedTask();

    //gesture control
    private GestureDetectorCompat mDetector;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        this.mDetector.onTouchEvent(event);  // ---use gesture control----
        return super.onTouchEvent(event);
    }


    // GestureDetector.OnGestureListener
    private GestureDetector.OnGestureListener myGesture = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        //        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            long t1 = 120;
//        Log.i("GESTURE","onScroll---X:"+distanceX+"   Y:"+distanceY);
            if ((distanceY > 15) & (distanceX < 5)) {
                Log.i("GESTURE", "scroll up");
                moveCameraOnSroll(SUConstant.PTZ_UP, t1);
            }

            if ((distanceY < -15) & (distanceX < 5)) {
                Log.i("GESTURE", "scroll down");
                moveCameraOnSroll(SUConstant.PTZ_DOWN, t1);
            }

            if ((distanceY < 5) & (distanceX > 15)) {
                Log.i("GESTURE", "scroll left");
                moveCameraOnSroll(SUConstant.PTZ_LEFT, t1);
            }

            if ((distanceY < 5) & (distanceX < -15)) {
                Log.i("GESTURE", "scroll right");
                moveCameraOnSroll(SUConstant.PTZ_RIGHT, t1);
            }

            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    };


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("TAG","life---restart");
        if (WPApplication.getInstance().monitorBundle_oncreate!=null)
        onCreate(WPApplication.getInstance().monitorBundle_oncreate);
    }

    /* ----------------- */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"life---create----");
        WPApplication.getInstance().monitorBundle_oncreate=savedInstanceState;
        initData(getIntent());  //从获取 stationId deviceName
        initView();
        // init gesture detect
//        mDetector=new GestureDetectorCompat(this,myGesture);
        showScreenStat();
    }

    private void showScreenStat() {
        DisplayMetrics o = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(o);
        int widthPixels = o.widthPixels;
        int heightPixels = o.heightPixels;
        float density = o.density;  //
        Log.i("SCREEN", "widthPixels=" + widthPixels + "   heightPixels=" + heightPixels + "   density=" + density);
    }

    @Override
    public void onResume() {
        super.onResume();
        initResume();
        Log.i(TAG,"life---resume----");
    }

    private void initData(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            errorQuit(1001);
            finish();
        } else {
            stationId = bundle.getInt(MainStationListFragment.KEY_ID, -1);
            deviceName = bundle.getString(StationDevicesFragment.KEY_DEVICE, "");
            if (WPApplication.DEBUG) {
                Log.w(TAG, "stationId:" + stationId);
                Log.w(TAG, "deviceName:" + deviceName);
            }
            if (stationId == -1 || deviceName == null || deviceName.equals("")) {
                errorQuit(1002);
                finish();
            }
        }
        StationNode node = WPApplication.getInstance().getStationNode(stationId);
        if (node != null) {
            if (stationId != node.getId()) {
                errorQuit(1003);
                finish();
            }
            for (BaseDevice n : node.getDeviceList()) {
                if (n.getName().equals(deviceName)) {
                    if (n instanceof MonitorNode) {
                        mDevice = (MonitorNode) n;
                    } else {
                        errorQuit(6001);
                        finish();
                    }
                    break;
                }
            }
            int tracks = node.getRecordSum();
////-----------------之前播放报警与 场站的alarm相关
//            if(tracks > 0){
//                Integer[] ll = new Integer[tracks];
//                for(int i = 0; i < tracks; i++){
//                    ll[i] = i + 1;
//                }
//                mTrackAdapter = new ArrayAdapter<>(this, R.layout.list_item_track, ll);
//                haveTrackList = true;
//            }else{
//                haveTrackList = false;
//            }
////-----------------end of 之前播放报警与 场站的alarm相关


            //-----9-24 播放警报与alarm无关--播放警报给入侵者听--只有两种警报
            if (tracks > 0) {
                haveTrackList = true;
            } else {
                haveTrackList = false;
            }

            Integer[] ll = new Integer[2];
            ll[0] = 0;
            ll[1] = 1;
            mTrackAdapter = new ArrayAdapter<>(this, R.layout.list_item_track, ll);
            //-----end of 9-24 播放警报与alarm无关--播放警报给入侵者听--只有两种警报

        } else {
            return;
        }
        mTimer = new Timer();
        mAudioBuffer = SelfVideoDataBuffer2.getVideoDataBuffer();
        EventBus.getDefault().register(this);
    }

    private void initView() {
//        // 切换成横屏
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        // 切换成竖屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        // 全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 保持屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mContext = this;
        setContentView(R.layout.layout_monitor_compat);
        mSurfaceView = (SurfaceView) findViewById(R.id.monitor_surface);
        mSurfaceView.getHolder().addCallback(this);

        // 报警语音列表
        mTrackListView = (ListView) findViewById(R.id.list_monitor_tracklist);
        mTrackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (DEBUG) {
                    Log.i(TAG, "click track item:" + position);
                }
                Log.i("BBB", "POSI" + position);
                //  position=0-->request 报警track0          position=1-->request 报警track1
                new Thread(new RequestRunnable(SUConstant.SUB_REQUEST_ALARM_PLAYER, position)).start();
                hideTrackList();
                isTracking = false;
                switchBackground(bn_track, getString(R.string.str_monitor_select_track), bg_on, color_show);
            }
        });
        mTrackListView.setAdapter(mTrackAdapter);
        initButton();

        ApplicationManager.getInstance().addActivity(this);
    }

    private void initResume() {
        callAllowed = false;

        //加载等待对话框
        mLoading = PublicMethodUtil.getInstance().makeProgressDialog(
                mContext, R.string.str_null, R.string.str_monitoringvideo_loading_loadvideo, false, true,
                new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                            quit(0);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
        );
        mLoading.setCanceledOnTouchOutside(false);
        showLoading();
        // 请求视频(10s)后无数据关闭当前请求页面
//        clearTimer();

        mTimer.cancel();
        mTimer=new Timer();
        mTimer.schedule(rft1, WAITING_TIME);    // TODO error???

        //录音工具
        mAudioUtils = new AudioUtils(this);
    }

    private void initButton() {
        //退出按钮
        bn_quit = (ImageButton) findViewById(R.id.monitor_quit);
        bn_quit.setOnClickListener(this);

        linear_controller = (LinearLayout) findViewById(R.id.linear_monitor_list_controller);
        rl_menu = (RelativeLayout) findViewById(R.id.monitor_ptz_menu);

        bn_track = (Button) findViewById(R.id.bn_monitor_track);
        bn_speaker = (Button) findViewById(R.id.bn_monitor_speaker);
        bn_openMenu = (Button) findViewById(R.id.bn_monitor_menu);
        bn_closeMenu = (Button) findViewById(R.id.bn_monitor_ptz_move);
        bn_move_up = (Button) findViewById(R.id.bn_monitor_ptz_move_up);
        bn_move_down = (Button) findViewById(R.id.bn_monitor_ptz_move_down);
        bn_move_right = (Button) findViewById(R.id.bn_monitor_ptz_move_right);
        bn_move_left = (Button) findViewById(R.id.bn_monitor_ptz_move_left);
        bn_sharp = (Button) findViewById(R.id.bn_monitor_sharp);
        bn_zoom = (Button) findViewById(R.id.bn_monitor_zoom);
        bn_aperture = (Button) findViewById(R.id.bn_monitor_aperture);
        bn_increase = (Button) findViewById(R.id.bn_monitor_increase);
        bn_decrease = (Button) findViewById(R.id.bn_monitor_decrease);

        bn_track.setOnClickListener(this);
        bn_speaker.setOnClickListener(this);
        bn_openMenu.setOnClickListener(this);
        bn_closeMenu.setOnClickListener(this);
        bn_sharp.setOnClickListener(this);
        bn_zoom.setOnClickListener(this);
        bn_aperture.setOnClickListener(this);

        bn_move_up.setOnTouchListener(this);
        bn_move_down.setOnTouchListener(this);
        bn_move_left.setOnTouchListener(this);
        bn_move_right.setOnTouchListener(this);
        bn_increase.setOnTouchListener(this);
        bn_decrease.setOnTouchListener(this);

        Resources res = getResources();
        color_dim = res.getColor(tx_dim);
        color_on = res.getColor(tx_on);
        color_show = res.getColor(tx_show);
    }


    /**
     * 初始化播放库
     */
    private boolean initPlayerSDK() {
        mPlayer = Player.getInstance();
        if (mPlayer == null) {
            quit(-3);
            return false;
        }
        mPlayerPort = mPlayer.getPort();
        if (!mPlayer.setStreamOpenMode(mPlayerPort, Player.STREAM_REALTIME)) {
            quit(-3);
            return false;
        }
        return true;
    }

    /**
     * 监控视频页面的Handler
     * <p>用于关闭dialog、以及显示错误原因</p>
     */
    Handler monitorVideoHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            closeLoading();
            switch (msg.what) {
                case 10:
                    break;
                case -1:
                    showToast(mContext, R.string.str_monitoringvideo_toast_loadvideofaild);
                    break;
                case -2:
                    Log.i(TAG, "-----------超时？？-----------");
                    Log.i("CCC", "-----------超时？？-----------");
                    showToast(mContext, R.string.str_monitoringvideo_toast_flowused_f);
                    break;
                case -3:
                    showToast(mContext, R.string.str_monitoringvideo_toast_flowused_g);
                    break;
                case -4:
                    showToast(mContext, R.string.str_monitoringvideo_toast_flowused_h);
                    break;
                case -5:
                    showToast(mContext, R.string.str_monitoringvideo_toast_flowused_i);
                    break;
                case -6:  //其它终端喊话中--暂时不能喊话
                    showToast(mContext, R.string.str_monitoringvideo_toast_flowused_j);
                    // 按键切换到  "开启喊话"状态  (再按开启喊话)
                    switchBackground(bn_speaker, getString(R.string.str_monitor_open_speak), bg_on, color_show);
                    bn_speaker.setClickable(true);
                    isSpeaking = false;
                    break;
                case -7:  //可以喊话，喊话开启
                    bn_speaker.setClickable(true);
//                    showToast(mContext, R.string.str_monitoringvideo_toast_flowused_k);
                    Toast.makeText(mContext, R.string.str_monitoringvideo_toast_flowused_k, Toast.LENGTH_SHORT).show();
                    // 按键切换到  "关闭喊话"状态  (再按关闭喊话)
                    switchBackground(bn_speaker, getString(R.string.str_monitor_close_speak), bg_off, color_on);
                    isSpeaking = true;
                    break;
                case -8:  //  喊话结束
                    bn_speaker.setClickable(true);
                    showToast(mContext, R.string.str_monitoringvideo_toast_flowused_l);
                    // 按键切换到  "开启喊话"状态  (再按开启喊话)
                    switchBackground(bn_speaker, getString(R.string.str_monitor_open_speak), bg_on, color_show);
                    isSpeaking = false;
                    break;
                case -9:  //  单声道不支持
                    showToast(mContext, R.string.str_monitoringvideo_toast_flowused_m);
                    // 按键切换到  "开启喊话"状态  (再按开启喊话)
                    switchBackground(bn_speaker, getString(R.string.str_monitor_open_speak), bg_on, color_show);
                    isSpeaking = false;
                    break;


                default:
                    break;
            }
        }
    };

    @Override
    public void releaseHandler() {
        monitorVideoHandler.removeCallbacksAndMessages(null);
        monitorVideoHandler = null;
    }

    /**
     * 开启Loading对话框
     */
    private void showLoading() {
        if (WPApplication.DEBUG_NO_MONITOR_LOADING) {
            return;
        }
        if (mLoading != null && !mLoading.isShowing()) {
            mLoading.show();
        }
    }

    /**
     * 退出处理
     * <p>主要包括关闭Loading的处理</p>
     */
    private void quit(int what) {
        if (monitorVideoHandler == null) return;
        monitorVideoHandler.sendEmptyMessage(what);  // TODO monitorVideoHandler should not be null
        quit();
    }

    /**
     * 退出
     */
    private void quit() {
        finish();
    }

    /**
     * 清理资源
     * <p>主要是释放资源、清理线程、发送关闭请求等，不涉及UI处理</p>
     */
    private void release() {
        // 向监控端发送停止视频请求
        new Thread(new RequestRunnable(SUConstant.SUB_REQUEST_STOP_PREVIEW, 0)).start();
        Log.i(TAG,"life---pause--release----");
        isTracking = false;
        isSpeaking = false;
        if (closeLoading()) Log.i(TAG,"life---pause----release---closeLoading");
//        clearTimer();
        releaseResource();
    }

    /**
     * 关闭Loading对话框
     */
    private boolean closeLoading() {
        if (mLoading != null) {
            if (mLoading.isShowing()) {
                mLoading.dismiss();
            }
            mLoading.cancel();
            mLoading = null;
        }
        return true;
    }

    /**
     * 资源释放
     */
    private void releaseResource() {
        if (mPlayer != null) {
            mPlayer.stop(mPlayerPort);
            mPlayer.closeStream(mPlayerPort);
            mPlayer.freePort(mPlayerPort);
            mPlayer = null;
        }
        mAudioUtils.release();
        EventBus.getDefault().unregister(this);
        CommunicationUtils.getInstance().unregisterVideoCallback();  //反注册报错，写成空接口不传输？？
    }

    /**
     * 清理计时器---> TODO this is redundant
     */
//    private void clearTimer() {
//        if (mTimer != null) {
//            mTimer.purge();
////            mTimer.cancel();
//        }
//    }

    /* ------------- listview ---------------- */

    /**
     * 显示警告语音列表
     */
    private void showTrackList() {
        isTracking = true;
        mTrackListView.setVisibility(View.VISIBLE);
        switchBackground(bn_track, getString(R.string.str_monitor_close_select), bg_off, color_on);
    }

    /**
     * 关闭警告语音列表
     */
    private void hideTrackList() {
        isTracking = false;
        mTrackListView.setVisibility(View.INVISIBLE);
        switchBackground(bn_track, getString(R.string.str_monitor_select_track), bg_on, color_show);
    }

    /**
     * 切换按钮显示
     *
     * @param v         bn_track，bn_speakder者两个
     * @param str       显示的文字
     * @param srcId     按钮背景
     * @param textColor 字体颜色
     */
    private void switchBackground(View v, String str, int srcId, int textColor) {
        ((TextView) v).setText(str);
        ((TextView) v).setTextColor(textColor);
        v.setBackgroundResource(srcId);
    }

    /* ------------- button --------------- */

    /**
     * 处理云台控制按钮的Touch事件
     *
     * @param type  pdu类型
     * @param event touch事件
     */
    private void perform(int type, MotionEvent event) {
        switch (event.getAction()) {
            // 按下，发送开始请求
            case MotionEvent.ACTION_DOWN:
                if (DEBUG) {
                    Log.i(TAG, "touch down");
                }
                new Thread(new RequestPtzRunnable(type, true)).start();
                break;
            // 撤销或放开，发送停止请求
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (DEBUG) {
                    Log.i(TAG, "touch up");
                }
                new Thread(new RequestPtzRunnable(type, false)).start();
                break;
        }
    }


    // single thread tool to handle scroll action to control camera move
    private ExecutorService pool;

    private class MoveCameraRunn implements Runnable {
        private int type;
        private long time;

        public MoveCameraRunn(int type, long time) {
            this.type = type;
            this.time = time;
        }

        @Override
        public void run() {
            new Thread(new RequestPtzRunnable(type, true)).start();
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(new RequestPtzRunnable(type, false)).start();
            Log.i("GESTURE", "1run");
        }
    }

    //
    private void moveCameraOnSroll(int type, long time) {
        if (pool == null) {
            pool = Executors.newSingleThreadExecutor();
        }
        pool.execute(new MoveCameraRunn(type, time));
    }


    /**
     * 检查增减控制按钮容器的状态，并做相应的处理
     *
     * @param v 包括bn_sharp，bn_zoom，bn_aperture
     */
    private void detectController(View v) {
        if (isControl) {
            if (ptzType != getPtzType(v) && temp != null) {
                switchController(v);
            } else {
                closeController(v);
            }
        } else {
            openController(v);
        }
    }

    /**
     * 打开增减控制
     *
     * @param v 当前选中的功能键
     */
    private void openController(View v) {
        temp = (Button) v;
        temp.setTextColor(color_show);
        temp.setBackgroundResource(bg_ctrl);
        linear_controller.setVisibility(View.VISIBLE);
        isControl = true;
        ptzType = getPtzType(v);
    }

    /**
     * 增减控制在打开的情况下，切换功能键按钮的显示
     *
     * @param v 当前选中的功能键
     */
    private void switchController(View v) {
        temp.setTextColor(color_dim);
        temp.setBackgroundResource(bg_on);
        temp = (Button) v;
        temp.setTextColor(color_show);
        temp.setBackgroundResource(bg_ctrl);
        ptzType = getPtzType(v);
    }

    /**
     * 关闭增减控制
     *
     * @param v 当前选中的功能键
     */
    private void closeController(View v) {
        ((TextView) v).setTextColor(color_dim);
        v.setBackgroundResource(bg_on);
        temp = null;
        ptzType = getPtzType(null);
        linear_controller.setVisibility(View.INVISIBLE);
        isControl = false;
    }

    /**
     * 根据功能键生成相应的子Pdu类型
     *
     * @param v 包括bn_sharp，bn_zoom，bn_aperture
     */
    private int getPtzType(View v) {
        int ret = -1;
        if (v == bn_sharp) {
            ret = SUConstant.PTZ_FOCUS_SHARPNESS_DECREASE;
        } else if (v == bn_zoom) {
            ret = SUConstant.PTZ_FOCUS_ZOOM_DECREASE;
        } else if (v == bn_aperture) {
            ret = SUConstant.PTZ_APERTURE_SHRINK;
        }
        return ret;
    }

    /* --------------------- surface view ----------------------- */
    /* onCreate -> onResume -> surfaceCreated */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //初始化播放库
        initPlayerSDK();
        //向通信库注册回调监听，用于回调视频数据
        CommunicationUtils.getInstance().registerVideoCallback(this);
        //发送预览请求  PDU=SUConstant.SUB_REQUEST_START_PREVIEW
        new Thread(new RequestRunnable(SUConstant.SUB_REQUEST_START_PREVIEW, 0)).start();
//        new Thread(new RequestRunnable(SUConstant.SUB_REQUEST_STOP_PREVIEW, 0)).start();//停止预览请求

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                // 在播放列表打开的情况下，拦截退出事件
                if (isTracking) {
                    hideTrackList();
                    return true;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"life---stop----");
    }

    @Override
    public void onPause() {
        Log.i(TAG,"life---PAUSE----");
        release();
        mAudioBuffer.clearOldData();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"life---destroy----");
        super.onDestroy();
//        release();
//        new Thread(new RequestRunnable(SUConstant.SUB_REQUEST_STOP_PREVIEW, 0)).start();//停止预览请求//release中已经有了
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            quit(0);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (bn_move_up == v) {
//            Log.d("BBB","up");
            perform(SUConstant.PTZ_UP, event);
        } else if (bn_move_down == v) {
//            Log.d("BBB","down");
            perform(SUConstant.PTZ_DOWN, event);
        } else if (bn_move_left == v) {
//            Log.d("BBB","left");
            perform(SUConstant.PTZ_LEFT, event);
        } else if (bn_move_right == v) {
//            Log.d("BBB","right");
            perform(SUConstant.PTZ_RIGHT, event);
        } else if (bn_decrease == v) {
//            Log.d("BBB","decrease");
            if (ptzType != -1) {
                perform(ptzType, event);
            }
        } else if (bn_increase == v) {
//            Log.d("BBB","increase");
            if (ptzType != -1) {
                perform(ptzType + 1, event);
            }
        }
        return false;  //返回false，onclick回调可以执行
    }

    @Override
    public void onClick(View v) {
        // 退出按钮//语音//云台控制
        if (v == bn_quit) {
            // loading显示的时候也点击不了，就不处理loading了
            quit();
//-------------------------------
//            new Thread(new RequestRunnable(SUConstant.SUB_REQUEST_STOP_PREVIEW, 0)).start();
//            isTracking = false;
//            isSpeaking = false;
//            closeLoading();
//            clearTimer();
//            if(mPlayer != null){
//                mPlayer.stop(mPlayerPort);
//                mPlayer.closeStream(mPlayerPort);
//                mPlayer.freePort(mPlayerPort);
//                mPlayer = null;
//            }
//            mAudioUtils.release();
//            EventBus.getDefault().unregister(this);
//            ------------------------
////            CommunicationUtils.getInstance().unregisterVideoCallback();

        } else if (bn_track == v) {    // 9-24 播放报警与是否有警报无关---  if条件中删掉 && haveTrackList
            if (!isSpeaking) {
                if (!isTracking) {
                    showTrackList();
                } else {
                    hideTrackList();
                }
            }
        } else if (bn_speaker == v) {   //喊话
            if (!isTracking) {
                if (!isSpeaking) { //从isSpeaking=false  进入到 isSpeaking=true

                    try {
                        TransferDecoding mEncoder = new TransferDecoding();
                    } catch (Error e) {
                        isSpeaking = false;
                        switchBackground(v, getString(R.string.str_monitor_open_speak), bg_on, color_show);
                        monitorVideoHandler.sendEmptyMessage(-9);
                        e.printStackTrace();
                        return;
                    }

                    isSpeaking = true;

                    new Thread(new SendVoiceRunnable()).start();     // 录音在收到回复后开启
                    switchBackground(v, getString(R.string.str_monitor_opening), bg_off, color_on);
                    v.setClickable(false);

                } else {  //从isSpeaking=true  进入到 isSpeaking=false
                    mAudioUtils.stopRecord();
                    isSpeaking = false;
                    switchBackground(v, getString(R.string.str_monitor_closing), bg_on, color_show);
                    v.setClickable(false);
                    callAllowed = false;
                }
            }
        } else if (bn_openMenu == v) {
            if (!isMenu) {
                v.setVisibility(View.INVISIBLE);
                rl_menu.setVisibility(View.VISIBLE);
                isMenu = !isMenu;
            }
        } else if (bn_closeMenu == v) {
            if (isMenu) {
                bn_openMenu.setVisibility(View.VISIBLE);
                rl_menu.setVisibility(View.INVISIBLE);
                isMenu = !isMenu;
            }
        } else if (bn_sharp == v) {
            detectController(v);
        } else if (bn_zoom == v) {
            detectController(v);
        } else if (bn_aperture == v) {
            detectController(v);
        }

    }

    //语音回调：本地工具->接口->发送
    @Override
    public void onRecordData(byte[] data, int length) {
//        Log.i("BBB","mAudioBuffer null?"+(mAudioBuffer==null)+"     onRecordData--LENGTH"+length);
        if (mAudioBuffer != null) {
            mAudioBuffer.enQueue(data);
        }
        Log.i("BBB", "onRecordData--LENGTH" + length);
    }

    @Override
    public void onData(int iDataType, byte[] pDataBuffer, int iDataSize) {
        if (DEBUG) {
            Log.i(TAG, "--------------------Video onData------------iDataType:" + iDataType);
        }
        switch (iDataType) {
            // 视频头
            case HCNetSDK.NET_DVR_SYSHEAD:  //头数据--> 视屏的第一个包
                // 关闭Loading
                closeLoading();
                // 清除等待计时器
//                clearTimer();
                if (iDataSize > 0) {
                    mPlayer.openStream(mPlayerPort, pDataBuffer, iDataSize, 1024 * 800);
                    mPlayer.setDisplayBuf(mPlayerPort, 15);
                    mPlayer.play(mPlayerPort, mSurfaceView.getHolder());
                    //nPort [1st in] 播放通道号 HWND [2nd in] 播放视频的窗口句柄
//                    Log.i(TAG, "HCNetSDK.NET_DVR_SYSHEAD" );
                    Log.i("AAA", "HCNetSDK.NET_DVR_SYSHEAD");
                    break;
                }
                // 发送一个msg通知UI线程关闭Progressdialog
                monitorVideoHandler.sendEmptyMessage(10);
                break;
            case HCNetSDK.NET_DVR_STREAMDATA: //码流数据  --->
                if ((iDataSize > 0) & (mPlayer != null)) {
                    if (!mPlayer.inputData(mPlayerPort, pDataBuffer, iDataSize)) {
//                        Log.i(TAG, "HCNetSDK.NET_DVR_STREAMDATA" );
                        Log.i("AAA", "HCNetSDK.NET_DVR_STREAMDATA");
                        break;
                    }
                }
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(MonitorEvent event) {
        //TODO: ##设备掉线、用户被挤下线
        switch (event.getType()) {
            case MonitorEvent.MONITOR_EXCEPTION_QUIT:
                mSurfaceView.setBackgroundColor(Color.BLACK);
                monitorVideoHandler.sendEmptyMessage(-99);
                quit();
                break;
            case MonitorEvent.MONITOR_EXCEPTION_LINE:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("警告");
                builder.setMessage("设备网络异常，请稍后重新预览！");
                builder.setPositiveButton("退出",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                quit();
                            }
                        });
                builder.create().show();
                break;
            default:
                break;
        }
    }

    /**
     * 站场退出事件
     */
    public void onEventMainThread(StationEvent event) {
        switch (event.getType()) {
            case StationEvent.STATION_EVENT_ONLINE_STATE_CHANGED:
                // 当前在线状态改变，则退出该站场页面
                if (!WPApplication.getInstance().getStationNode(stationId).isOnline()) {
                    quit(10);
                }
                break;
            case StationEvent.STATION_EVENT_EXCEPTION:
                break;
        }
    }

    /**
     * 不使用了，都写入到  SendVoiceRunnable 中
     */
//    /* ------------------ 首次语音请求 ---------------------------- */
//    private void sendFirstVoiceRequest(){
//        new Thread(new Runnable(){
//            public void run(){
//                // 可以喊话 return （true）1 /不能喊话 return （false）0 / 请求失败 return--2
//                int callAllowedInt=CommunicationUtils.getInstance().sendFirstVoiceReq(stationId, deviceName);
//            }
//        }).start();
//    }
//    /* ------------------ 结束语音请求 ---------------------------- */
//    private void stopVoiceRequest(){
//        new Thread(new Runnable(){
//            public void run(){
//                CommunicationUtils.getInstance().stopVoiceReq(stationId, deviceName);
//            }
//        }).start();
//    }




    /* ------------------ Thread ---------------------------- */

    /**
     * 发送语音线程
     */
    private class SendVoiceRunnable implements Runnable {
        byte[] data;

        @Override
        public void run() {
            data = mAudioBuffer.deQueue();

            //首先询问服务器，是否可以喊话
            int callAllowedInt = CALL_REQ_INIT;
//            Log.i("BBB", "callAllowedInt--just after INIT" + callAllowedInt);
            callAllowedInt = CommunicationUtils.getInstance().sendFirstVoiceReq(stationId, deviceName);
//            Log.i("BBB", "callAllowedInt" + callAllowedInt);
            if (callAllowedInt == CALL_ALLOWED) {  //如果可以喊话

                mAudioUtils.startRecord(); // 开启录音

                callAllowed = true;
                // 通知UI，call allowed
                monitorVideoHandler.sendEmptyMessage(-7);
                Log.i("BBB", "sendEmptyMessage(-7)" + isSpeaking);

                while (isSpeaking) {   // isSpeaking=true时循环，
                    data = mAudioBuffer.deQueue();
//                    Log.i("BBB", "data null?" + (data == null));
//                Log.i("BBB", "data null?"+(data==null));
                    if (data != null) {
                        Log.i("BBB", "data length" + data.length);
                        CommunicationUtils.getInstance().sendVoice(stationId, deviceName, data, data.length);
                    } else {
                        try {
                            // NOTE: 腾出空闲时间片--> 啥用？
                            Thread.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (DEBUG) {
                            Log.e(TAG, "voice data is null");
                        }
                    }
                }

                //喊话结束，通知服务器--结束通话后发送停止喊话通知
                while (mAudioBuffer.listSize() > 0) {
                    data = mAudioBuffer.deQueue();
                }   // 音频队列清空

                CommunicationUtils.getInstance().stopVoiceReq(stationId, deviceName);
                // 喊话结束 通知UI(更新喊话按键)，call not allowed
                if (monitorVideoHandler!=null) monitorVideoHandler.sendEmptyMessage(-8);
                Log.i("BBB", "END OF RUN");

            } else if (callAllowedInt == CALL_NOT_ALLOWED) {
                callAllowed = false;
                // 通知UI(更新喊话按键)，call not allowed
                monitorVideoHandler.sendEmptyMessage(-6);
            }
            if (DEBUG) {
                Log.e(TAG, "-------------------------------stop send voice thread-----------------");
            }
            mAudioBuffer.clearOldData();
        }
    }

    private class RequestPtzRunnable implements Runnable {
        private int type;
        private boolean state;

        public RequestPtzRunnable(int type, boolean state) {
            this.type = type;
            this.state = state;
        }

        @Override
        public void run() {
            if (!WPApplication.DEBUG_NO_PTZ) {
                if (DEBUG) {
                    Log.i(TAG, "request ptz");
                }
                CommunicationUtils.getInstance().requestPtz(stationId, deviceName, type, state);
            }
        }
    }

    /**
     * 请求线程，包括：播放警告、开始预览、停止预览
     */
    private class RequestRunnable implements Runnable {
        private short pdu = 0;
        private int extra = 0;

        public RequestRunnable(short pdu, int extra) {
            this.pdu = pdu;
            this.extra = extra;
        }

        @Override
        public void run() {
            if (DEBUG) {
                Log.i(TAG, "------------------------------");
                Log.i(TAG, "RequestRunnable>> device");
                Log.i(TAG, "pdu:" + pdu);
            }

            //--stop preview: pdu=SUConstant.SUB_REQUEST_STOP_PREVIEW,extra=0,

            byte[] req = CommunicationUtils.getInstance().createRequestSys(pdu, stationId, deviceName, extra);
            int count = 0;
            int limited = 3;

            boolean ret = CommunicationUtils.getInstance().requestPtzDevice(SUConstant.PDU_C_CLI_REQ_WIN, req);

            // ret=false 且 count<3        且场站在线
            while (!ret && count < limited && WPApplication.getInstance().getStationNode(stationId).isOnline()) {
                ret = CommunicationUtils.getInstance().requestPtzDevice(SUConstant.PDU_C_CLI_REQ_WIN, req);
                Log.w(TAG, "request:" + pdu + " result:" + ret);
                count++;
            }

            if(pdu==SUConstant.SUB_REQUEST_STOP_PREVIEW)
            Log.i("CCC","STOP PREVIEW"+ret);


            if (!ret) {      //如果返回失败
                if (DEBUG) {
                    Log.e(TAG, "request failure!");
                }
                switch (pdu) {
                    case SUConstant.SUB_REQUEST_START_PREVIEW:
                        quit(-4);
                        break;
                    case SUConstant.SUB_REQUEST_STOP_PREVIEW:
                        break;
                    case SUConstant.SUB_REQUEST_ALARM_PLAYER:
                        monitorVideoHandler.sendEmptyMessage(-5);
                        break;
                    default:
                        break;
                }
            } else if (DEBUG) {
                Log.w(TAG, " ... request over ...");
            } else {
                switch (pdu) {
                    case SUConstant.SUB_REQUEST_START_PREVIEW:
                        rft1.handle=rft1.VIDEO_OK;   // change task to OK state
                        closeLoading();
                        break;
                }
            }
        }
    }

    /**
     * 请求视频失败(超时)，退出当前页面
     */
    private class RequestFailedTask extends TimerTask {

        private final int VIDEO_FAIL=1;
        private final int VIDEO_OK=2;
        private int handle=VIDEO_FAIL;

        @Override
        public void run() {
            // 超时退出页面
            if (handle==VIDEO_FAIL) {
                quit(-2);
            }
        }
    }
}
