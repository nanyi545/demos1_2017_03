package com.nanyi545.www.materialdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nanyi545.www.materialdemo.utils.MyRVAdapter;

public class BehaviourActivity1 extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,BehaviourActivity1.class);
        c.startActivity(i);
    }

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MyRVAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_behaviour1);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Title");

        mRecyclerView = (RecyclerView) findViewById(R.id.rvToDoList);
        // use this setting to improve performance if you know that changes------------------
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager--------------------------------------------------------
        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter ---------------------------------------------------------------
        String[] myDataset={"item1","item2","item3","item4","item5"};
        mAdapter = new MyRVAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }
}
