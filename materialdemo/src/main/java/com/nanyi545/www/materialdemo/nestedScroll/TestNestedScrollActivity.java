package com.nanyi545.www.materialdemo.nestedScroll;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;

import com.nanyi545.www.materialdemo.R;

public class TestNestedScrollActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,TestNestedScrollActivity.class);
        c.startActivity(i);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_nested_scroll);
        AppBarLayout lo= (AppBarLayout) findViewById(R.id.app_bar_lo);
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) lo.getLayoutParams();
        Log.i("BBB","app bar layout behaviour:"+(params.getBehavior()==null));  // here behaviour will not be displayed
        test.sendEmptyMessageDelayed(0,2000);
    }


    Handler test=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AppBarLayout lo= (AppBarLayout) findViewById(R.id.app_bar_lo);
            CoordinatorLayout.LayoutParams params =
                    (CoordinatorLayout.LayoutParams) lo.getLayoutParams();
            if ((params.getBehavior()!=null)){
                Log.i("BBB","app bar layout behaviour:"+params.getBehavior().getClass().getName());   // behaviour can only be correctly retrieved when the layout is fully established ....
            }


        }
    };


}
