package com.nanyi545.www.materialdemo.customView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nanyi545.www.materialdemo.R;

public class TestCustomViewActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,TestCustomViewActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_custom_view);
    }



}
