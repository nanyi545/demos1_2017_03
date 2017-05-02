package com.nanyi545.www.materialdemo.full_screen;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.nanyi545.www.materialdemo.R;

public class FullScreenActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,FullScreenActivity.class);
        c.startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT  ) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_full_screen);

    }



    public void click(View v){
        int id=v.getId();
        switch(id){
            case R.id.window_manager_test:
                showPopupByWindowManager();
                break;

        }


    }



    private void showPopupByWindowManager(){


        WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.gravity= Gravity.LEFT | Gravity.TOP;
        mWindowParams.x = 0;
        mWindowParams.y = 0;
        mWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        mWindowParams.x = 110;
//        mWindowParams.y = 110;
//        mWindowParams.height = 99;
//        mWindowParams.width = 99;

        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_DIM_BEHIND
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        ;

        mWindowParams.dimAmount=0.4f;
        mWindowParams.format = PixelFormat.TRANSLUCENT;


        final View added=getLayoutInflater().inflate(R.layout.test_layout,null);
//        added.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        WindowManager mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams temp=getWindow().getAttributes();
//        temp.format=PixelFormat.TRANSLUCENT;
//        temp.format=PixelFormat.OPAQUE;
//        mWindowManager.addView(added, temp);
        mWindowManager.addView(added, mWindowParams);

        Log.i("aaa","window attr:      format:"+temp.format+"   flags:"+temp.flags);   //                         -2122252032       1000 0001 1000 0001 0000 0001 0000 0000
        Log.i("aaa","new win attr:     format:"+mWindowParams.format+"   flags:"+mWindowParams.flags);//           17039752         0000 0001 0000 0100 0000 0001 1000 1000

        //   FLAG_NOT_FOCUSABLE   8             1000
        //  FLAG_NOT_TOUCHABLE              00010000
        // FLAG_KEEP_SCREEN_ON              10000000
// FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS   0x80000000   1000 0000 0000 0000 0000 0000 0000 0000


        added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                added.setVisibility(View.GONE);
            }
        });


    }





}
