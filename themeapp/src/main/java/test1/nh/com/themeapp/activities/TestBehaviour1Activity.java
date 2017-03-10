package test1.nh.com.themeapp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import test1.nh.com.themeapp.R;
import test1.nh.com.themeapp.utils.MyRVAdapter;

public class TestBehaviour1Activity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,TestBehaviour1Activity.class);
        c.startActivity(i);
    }

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MyRVAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_behaviour1);

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
