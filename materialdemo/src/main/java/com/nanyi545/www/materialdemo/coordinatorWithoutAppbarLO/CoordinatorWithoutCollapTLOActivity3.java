package com.nanyi545.www.materialdemo.coordinatorWithoutAppbarLO;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nanyi545.www.materialdemo.R;
import com.nanyi545.www.materialdemo.utils.MyRVAdapter;

public class CoordinatorWithoutCollapTLOActivity3 extends AppCompatActivity {


    RecyclerView mRecyclerView;
    MyRVAdapter mAdapter;


    public static void start(Context c ){
        Intent i=new Intent(c,CoordinatorWithoutCollapTLOActivity3.class);
        c.startActivity(i);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);   // use this to draw below status bar
        setContentView(R.layout.activity_coordinator_without_collap_tlo3);


        mRecyclerView = (RecyclerView) findViewById(R.id.rvToDoList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String[] myDataset={"item1","item2","item3","item4","item5","item1","item2","item3","item4","item5","item1","item2","item3","item4","item5"};
        mAdapter = new MyRVAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }







}
