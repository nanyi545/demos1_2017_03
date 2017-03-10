package com.nanyi545.www.materialdemo.quickRet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nanyi545.www.materialdemo.R;

import java.util.Arrays;

public class QuickRetActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,QuickRetActivity.class);
        c.startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_ret);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(Arrays.asList(Cheeses.sCheeseStrings)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
