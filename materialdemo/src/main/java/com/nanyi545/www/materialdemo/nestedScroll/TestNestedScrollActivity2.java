package com.nanyi545.www.materialdemo.nestedScroll;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nanyi545.www.materialdemo.R;

public class TestNestedScrollActivity2 extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,TestNestedScrollActivity2.class);
        c.startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);   // use this to draw below status bar
        setContentView(R.layout.activity_test_nested_scroll2);
        test.sendEmptyMessageDelayed(0,2000);
    }


    Handler test=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageView iv= (ImageView) findViewById(R.id.test_iv);
            CoordinatorLayout.LayoutParams params =
                    (CoordinatorLayout.LayoutParams) iv.getLayoutParams();
            if ((params.getBehavior()!=null)){
                Log.i("BBB","iv behaviour:"+params.getBehavior().getClass().getName());   // behaviour can only be correctly retrieved when the layout is fully established ....
            }
        }
    };

}
