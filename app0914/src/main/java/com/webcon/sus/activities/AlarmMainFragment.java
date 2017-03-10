package com.webcon.sus.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.webcon.sus.demo.R;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.eventObjects.FABevent;
import com.webcon.sus.eventObjects.MessageEvent;
import com.webcon.sus.eventObjects.RVrefreshEvent;
import com.webcon.sus.eventObjects.ServiceEvent;
import com.webcon.sus.rvhelper.MyRecyclerScroll;
import com.webcon.sus.rvhelper.OnStartDragListener;
import com.webcon.sus.rvhelper.RVdragAdapter;
import com.webcon.sus.rvhelper.SimpleItemTouchHelperCallback;
import com.webcon.sus.utils.OnRVItemClickListener;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.WPApplication;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 主界面消息列表页面
 *
 * @author m
 */
public class AlarmMainFragment extends Fragment implements OnStartDragListener {

    private Context mContext;
    private LinearLayout linear_container;
    private NestedScrollView empty_container;

    // RecyclerLayout
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mRecyclerManager;
    //    private RVAlarmListAdapter mAdapter;
    // drag--
    private RVdragAdapter mAdapter2;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private boolean initialized = false;
    private boolean isWaiting = true;


    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1009:// other situations....
                    break;
                default:  // handles the delayed activity jump(to show the ripples)
                    int position = msg.what;
                    Bundle bundle = new Bundle();
                    AlarmNode alarm = getAlarmList().get(position);
                /* 跳转标识 flag
                 * 0:程序未启动的情况下从登陆页面直接跳转
                 * 1:点击报警列表跳转
                 * 2:程序在启动状态下点击通知跳转 */
                    bundle.putInt(AlarmDetailsActivityCompat.ALARM_DETAIL_FLAG, SUConstant.APP_RUNNING_M);
                    // 保留字段（推送的报警信息里会有，获取的默认填0）
                    bundle.putInt(AlarmDetailsActivityCompat.ALARM_DETAIL_TEMP, 0);
                    // 报警信息对象
                    bundle.putSerializable(AlarmDetailsActivityCompat.ALARM_DETAIL_DATA, alarm);
                    Log.i("BBB", "has PIC" + alarm.isCapture());
                    // 跳转到报警信息详细页面
                    Intent intent = new Intent(mContext, AlarmDetailsActivityCompat.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.page_bottom_in, 0);
                    break;
            }

        }
    };


    //------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_alarm_main, container, false);
        // 列表
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_alarms);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mRecyclerManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.getItemAnimator().setSupportsChangeAnimations(false); // blinking？？？ no use


//        mRecyclerView.addItemDecoration(new ListItemDecoration());
        // 组件
        empty_container = (NestedScrollView) layout.findViewById(R.id.nested_empty_container);
        linear_container = (LinearLayout) layout.findViewById(R.id.linear_alarm_container);

        if (!initialized) {
            EventBus.getDefault().post(new ServiceEvent(ServiceEvent.SERVICE_EVENT_INIT_ALARM_REQ));
        }
        return layout;
    }

    private void init() {
        mContext = getActivity();
        // 初始化时，从本地获取之前的报警消息>>保存到DB的情况下。。
        // ....（略）...则需要先初始化ListView
    }

//    private void initAdapter(){
//        mAdapter = new RVAlarmListAdapter(getAlarmList());
//        mAdapter.setOnItemClickListener(mRVListener);
//        mRecyclerView.setAdapter(mAdapter);
//    }

    private void initAdapter2() {
        mAdapter2 = new RVdragAdapter(getAlarmList(), this);
        mAdapter2.setOnItemClickListener(mRVListener);
        mRecyclerView.setAdapter(mAdapter2);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter2);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        // set scroll listener
        mRecyclerView.addOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                Log.i("FAB", "SHOW");
                FABevent e1 = new FABevent();
                e1.setStatus(FABevent.FAB_SHOW);
                EventBus.getDefault().post(e1);
            }

            @Override
            public void hide() {
                Log.i("FAB", "HIDE");
                FABevent e1 = new FABevent();
                e1.setStatus(FABevent.FAB_HIDE);
                EventBus.getDefault().post(e1);
            }
        });


    }


    private void refreshFragment() {
        if (isWaiting) {
            isWaiting = false;
        }
        if (mAdapter2 == null) {
            initAdapter2();
            checkAndRefresh(getAlarmList().size());
        } else {
            refreshAdapter();
        }
    }

    private void refreshAdapter() {
        checkAndRefresh(getAlarmList().size());
        WPApplication.getInstance().refreshAlarmList();
        mAdapter2.notifyDataSetChanged();
    }

    private void checkAndRefresh(int size) {
        if (size > 0) {
            linear_container.setVisibility(View.VISIBLE);
            empty_container.setVisibility(View.GONE);
        } else {
            linear_container.setVisibility(View.GONE);
            empty_container.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 列表项监听器
     */
    private OnRVItemClickListener mRVListener = new OnRVItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            mainHandler.sendEmptyMessageDelayed(position, 350);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            // longClick-->delete now disabled
            Log.i("alarm--", "longclick");
//            AlarmNode alarm = getAlarmList().get(position);
//            ArrayList<AlarmNode> list = new ArrayList<>();
//            list.add(alarm);
//            //转发处理  --->发给AlarmMsgManager 删除alarm + 更新UI
//            AlarmMsgManager.getInstance().transmitCenter(AlarmMsgManager.ALARM_MANAGE_DELETE, list);
        }
    };

    private List<AlarmNode> getAlarmList() {
        return WPApplication.getInstance().getAllAlarmList();
    }

    /**
     * view点击事件
     */
    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // ...
        }
    };


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    public void onBackPressed() {
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    public void onEventMainThread(MessageEvent event) {
        // 处理swipe to delete
        if (event.getType() == MessageEvent.ALARM_SWIPE_DELETED) {
            initAdapter2();
            mAdapter2.notifyDataSetChanged();
            return;
        }
        if (event.reload) {
            mAdapter2 = null;
        }
        refreshFragment();
        initialized = true;
    }


    //---- refresh RV----
    public void onEventMainThread(RVrefreshEvent event) {
        mRecyclerView.invalidate();
        Log.i("alarmRV", "RVrefreshEvent received");
    }


}
