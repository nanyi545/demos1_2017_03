package com.webcon.sus.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webcon.sus.adapter.RVAlarmListAdapter;
import com.webcon.sus.demo.R;
import com.webcon.sus.entity.AlarmNode;
import com.webcon.sus.entity.StationNode;
import com.webcon.sus.eventObjects.MessageEvent;
import com.webcon.sus.rvhelper.OnStartDragListener;
import com.webcon.sus.rvhelper.RVsecDragAdapter;
import com.webcon.sus.rvhelper.SimpleItemTouchHelperCallback;
import com.webcon.sus.utils.OnRVItemClickListener;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.WPApplication;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 站场消息列表页面
 *
 * @author m
 */
public class AlarmSecFragment extends Fragment implements OnStartDragListener {

    private Context mContext;
    private int mStationId = -1;
    private StationNode mStation;

    private LinearLayout linear_container;
    private RecyclerView mRecyclerView;
    private RVAlarmListAdapter mAdapter;
    // drag--
    private RVsecDragAdapter mAdapter2;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    private TextView tv_empty;

    //---
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
        init(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_alarm_sec, container, false);
        //列表
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_alarms_sec);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        mRecyclerView.addItemDecoration(new ListItemDecoration());
        //组件
        tv_empty = (TextView) layout.findViewById(R.id.tv_empty_sec);
        linear_container = (LinearLayout) layout.findViewById(R.id.linear_alarm_container_sec);
        refreshFragment();
        return layout;
    }

    private void init(Bundle bundle) {
        mContext = getActivity();
        if (bundle == null) {
            errorQuit(3001);
        } else {
            mStationId = bundle.getInt(MainStationListFragment.KEY_ID, -1);
            mStation = WPApplication.getInstance().getStationNode(mStationId);
            if (mStationId == -1 || mStation == null) {
                errorQuit(3002);
                getActivity().finish();
            }
        }
    }

    private void errorQuit(int errorCode) {
        Toast.makeText(mContext, "初始化失败: " + errorCode, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

//    private void initAdapter(){
//        mAdapter = new RVAlarmListAdapter(getAlarmList());
//        mAdapter.setOnItemClickListener(mRVListener);
//        mRecyclerView.setAdapter(mAdapter);
//    }


    private void initAdapter2() {
        mAdapter2 = new RVsecDragAdapter(getAlarmList(), this,mStation );
        mAdapter2.setOnItemClickListener(mRVListener);
        mRecyclerView.setAdapter(mAdapter2);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter2);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }


    /**
     * 列表项监听器
     */
    private OnRVItemClickListener mRVListener = new OnRVItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            mainHandler.sendEmptyMessageDelayed(position,350);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            // 本来用于选项的批量删除处理，但是不实现了
        }
    };

    private void refreshFragment() {
        Log.i("AlarmSec", "refreshFragment");
        if (mStation == null || getAlarmList() == null) {
            return;
        }
        if (mAdapter2 == null) {
            initAdapter2();
            checkAndRefresh(getAlarmList().size());
        } else {
            refreshAdapter();
        }
    }

    private void refreshAdapter() {
        if (mAdapter2 != null) {
            checkAndRefresh(getAlarmList().size());
            mAdapter2.notifyDataSetChanged();
        }
    }

    private void checkAndRefresh(int size) {
        if (size > 0) {
            linear_container.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.GONE);
        } else {
            linear_container.setVisibility(View.GONE);
            tv_empty.setVisibility(View.VISIBLE);
        }
    }

    private List<AlarmNode> getAlarmList() {
        return mStation.getAlarmList();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    public void onEventMainThread(MessageEvent event) {
        switch (event.getType()) {
            case MessageEvent.ALARM_FLAG_REFRESH:
                if (event.stationId == mStationId) {
                    refreshFragment();
                }
                break;
            case MessageEvent.ALARM_SWIPE_DELETED:  // 处理swipe to delete
//                Log.i("swipe","sec fragment"+event.stationId+"---"+mStationId);
                if(event.stationId == mStationId){
                    refreshFragment();
                }
                break;
            default:
                break;
        }
    }
}
