package com.nanyi545.www.materialdemo.test_frags.views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nanyi545.www.materialdemo.R;
import com.nanyi545.www.materialdemo.customView.TestCustomViewActivity;

public class TestFragmentActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,TestFragmentActivity.class);
        c.startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
    }


}
