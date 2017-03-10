package com.webcon.sus.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.webcon.sus.adapter.RVDeviceListAdapter;
import com.webcon.sus.demo.R;
import com.webcon.sus.entity.BaseDevice;
import com.webcon.sus.entity.StationNode;
import com.webcon.sus.eventObjects.DeviceEvent;
import com.webcon.sus.eventObjects.StationEvent;
import com.webcon.sus.utils.CommunicationUtils;
import com.webcon.sus.utils.OnRVItemClickListener;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.WPApplication;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 站场主界面の设备列表页面
 * @author m
 */
public class StationDevicesFragment extends Fragment {
    private static final String TAG = "StationDevicesFragment";
    public static final String KEY_DEVICE = "deviceId";

    private Context mContext;
    private int mStationId = -1;

    //显示的列表
    private RecyclerView mListView;
    private RVDeviceListAdapter mListAdapter;

    //站场结点集合，用于构造列表
    private List<BaseDevice> mDeviceList;
    private StationNode mStation;
    //
    private Button bn_defence;
    private CircleProgressBar mProgressBar;
    private boolean isDefenceOpened = false;
    //---------------
    public StationDevicesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        init(getArguments());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_station_devices, container, false);
        mListView = (RecyclerView) layout.findViewById(R.id.rvlist_devices);
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        mListView.setHasFixedSize(true);
        mListView.setItemAnimator(new DefaultItemAnimator());

        // 布防按钮
        bn_defence = (Button)layout.findViewById(R.id.bn_switch_defence);
        mProgressBar = (CircleProgressBar)layout.findViewById(R.id.mlp_loading_solve);
        // 初始化布防按钮状态
        isDefenceOpened = mStation.isDefend();
        switchDefenceButton(isDefenceOpened);
        // 按钮监听
        bn_defence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                bn_defence.setText("");
                mProgressBar.setVisibility(View.VISIBLE);
                if(isDefenceOpened){
                    CommunicationUtils.getInstance().closeDefence(mStationId);
                }else{
                    CommunicationUtils.getInstance().openDefence(mStationId);
                }
            }
        });
        // ===initList===
        refreshFragment();
        return layout;
    }


    private void init(Bundle bundle){
        mContext = getActivity();

        if(bundle == null){
            errorQuit(2001);
        }else{
            mStationId = bundle.getInt(MainStationListFragment.KEY_ID, -1);
            mStation = WPApplication.getInstance().getStationNode(mStationId);
            if(mStation != null && mStation.getDeviceList() != null){
                mDeviceList = mStation.getDeviceList();
            }
            if(mStationId == -1){
                errorQuit(2002);
                getActivity().finish();
            }
        }
    }

    /**
     * 刷新界面 (被动)
     */
    private void refreshFragment() {
        if(mDeviceList != null){
            if(mListAdapter == null){
                mListAdapter = new RVDeviceListAdapter(mDeviceList);
                mListAdapter.setOnItemClickListener(mClickListener);
                mListView.setAdapter(mListAdapter);
            }else{
                refreshAdapter();
            }
        }
    }

    public void refreshAdapter(){
        if(mListAdapter != null){
            mListAdapter.notifyDataSetChanged();
        }
    }

    private OnRVItemClickListener mClickListener = new OnRVItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
                if (mDeviceList.get(position).getType() == SUConstant.DEVICE_TYPE_MONITOR) {
                    Intent intent = new Intent(mContext, MonitorActivityCompat.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(MainStationListFragment.KEY_ID, mStationId);
                    bundle.putString(StationDevicesFragment.KEY_DEVICE,
                            mStation.getDeviceList().get(position).getName());
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Click Sensor!!", Toast.LENGTH_SHORT).show();
                }
        }

        @Override
        public void onItemLongClick(View view, int position) {
                //##TODO 弹出设备详细信息页面 DeviceInformationActivity
//                Intent intent = new Intent(mContext, StationInformationActivity.class);
//                startActivity(intent);
        }
    };

    private void errorQuit(int errorCode){
        Toast.makeText(mContext, "初始化失败: " + errorCode, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    private void switchDefenceButton(boolean isOpened){
        mProgressBar.setVisibility(View.GONE);
        if(isOpened){
            // 布防开启的状态。。显示关闭布防
            bn_defence.setBackgroundResource(R.drawable.selector_bn_close_defence);
            bn_defence.setText("关闭布防");
        }else{
            // 布防关闭的状态。。显示开启布防
            bn_defence.setBackgroundResource(R.drawable.selector_bn_open_defence);
            bn_defence.setText("开启布防");
        }
        bn_defence.setClickable(true);
    }

    //----
    public void onBackPressed() {
    }

    //##-------------
    public void onEventMainThread(StationEvent event){
        if(event.stationId != mStationId){
            Log.e(TAG, "not matched: " + event.stationId + " --> " + "stationId: " + mStationId);
            return;
        }
        switch (event.getType()){
            case StationEvent.STATION_EVENT_REFRESH:
                Log.i(TAG, "refresh station device list");
                if(event.msg == SUConstant.FLAG_STATION_OPENED){
                    isDefenceOpened = true;
                }else if(event.msg == SUConstant.FLAG_STATION_CLOSED){
                    isDefenceOpened = false;
                }else{
                    // 空信息或者其他信息 刷新列表
                    refreshFragment();
                }
                switchDefenceButton(isDefenceOpened);
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(DeviceEvent event){
        //##TODO: 设备状态变更
        switch(event.getType()){
            case DeviceEvent.DEVICE_ONLINE:
                break;
            case DeviceEvent.DEVICE_OFLINE:
                break;
            case DeviceEvent.DEVICE_EXCEPTION:
                break;
            default:
                break;
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy(){
        EventBus.getDefault().unregister(this);
        mContext = null;
        super.onDestroy();
    }


}
