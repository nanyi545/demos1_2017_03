package com.nanyi545.www.materialdemo.nestedScroll.use_nested_scrollview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.nanyi545.www.materialdemo.R;
import com.nanyi545.www.materialdemo.testPullToRefresh.CoordinatorPullToRefresh;
import com.nanyi545.www.materialdemo.testPullToRefresh.CostumRV;
import com.nanyi545.www.materialdemo.testPullToRefresh.RevealContentImp;
import com.nanyi545.www.materialdemo.utils.MyRVAdapter;

public class TestNestedScrollActivity3 extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,TestNestedScrollActivity3.class);
        c.startActivity(i);
    }

    RecyclerView mRecyclerView;
    MyRVAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);   // use this to draw below status bar

        setContentView(R.layout.activity_test_nested_scroll3);

        CoordinatorPullToRefresh holder= (CoordinatorPullToRefresh) findViewById(R.id.coordinator);
        Log.i("nnn",""+(holder==null));
        holder.setRevealContent(RevealContentImp.class);


        mRecyclerView = (CostumRV) findViewById(R.id.rvToDoList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        String[] myDataset={"item1","item2","item3","item4","item5","item1","item2","item3","item4","item5","item1","item2","item3","item4","item5","item1","item2","item3","item4","item5"};
        mAdapter = new MyRVAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }







}
