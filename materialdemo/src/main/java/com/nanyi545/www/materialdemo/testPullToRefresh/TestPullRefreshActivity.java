package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nanyi545.www.materialdemo.R;
import com.nanyi545.www.materialdemo.utils.MyRVAdapter;
import com.nanyi545.www.materialdemo.utils.TranslucentStatusBar;

public class TestPullRefreshActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,TestPullRefreshActivity.class);
        c.startActivity(i);
    }

    RecyclerView mRecyclerView;
    MyRVAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);   // use this to draw below status bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pull_refresh);
//        setContentView(R.layout.activity_test_pull_refresh1);

        PullToRefresh refreshHolder= (PullToRefresh) findViewById(R.id.pull_to_refresh_lo);
        refreshHolder.setRevealContent(PullToRefresh.RevealContentImp.class);

//        mRecyclerView = (RecyclerView) findViewById(R.id.rvToDoList);
//        mRecyclerView.setHasFixedSize(true);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        String[] myDataset={"item1","item2","item3","item4","item5"};
//        mAdapter = new MyRVAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);



    }


}
