package com.nanyi545.www.hybridtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void goto1(View v){
        FullWindowWebActivity.start(this);
    }

    public void goto2(View v){
        NonFullWindowActivity.start(this);
    }


}
