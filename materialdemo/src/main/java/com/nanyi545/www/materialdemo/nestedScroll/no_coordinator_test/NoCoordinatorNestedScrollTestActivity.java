package com.nanyi545.www.materialdemo.nestedScroll.no_coordinator_test;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nanyi545.www.materialdemo.R;
import com.nanyi545.www.materialdemo.testPullToRefresh.CostumRV;
import com.nanyi545.www.materialdemo.utils.MyRVAdapter;

/**
 * simple test of default nested scroll behavior
 */
public class NoCoordinatorNestedScrollTestActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,NoCoordinatorNestedScrollTestActivity.class);
        c.startActivity(i);
    }

    RecyclerView mRecyclerView;
    MyRVAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_coordinator_nested_scroll_test);

        mRecyclerView = (CostumRV) findViewById(R.id.rvToDoList);
        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false); // ...

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        String[] myDataset={"item1","item2","item3","item4","item5","item1","item2","item3","item4","item5","item1","item2","item3","item4","item5","item1","item2","item3","item4","item5"};
        mAdapter = new MyRVAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }



}
