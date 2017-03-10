package test1.nh.com.demos1.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

import test1.nh.com.demos1.R;

public class FbReboundActivity extends AppCompatActivity implements View.OnTouchListener{

    public static void start(Context context){
        final Intent i1=new Intent(context,FbReboundActivity.class);
        context.startActivity(i1);
    }



    SpringSystem springSystem;
    Spring scaleSpring;
    private static double TENSION = 100;// overshoot --> the higher the higher overshoot
    private static double DAMPER = 4; //friction --> the higher the faster to steady state


    Spring translateSpring;
    boolean mMoveUp=false;
    float mOrigY;


    Handler myUIhander=new Handler(){
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mOrigY=iv_translate.getY();
            translateSpring.setEndValue(mOrigY);
        }
    };


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scaleSpring.setEndValue(1f);
                return true;
            case MotionEvent.ACTION_UP:
                scaleSpring.setEndValue(0f);
                return true;
        }
        return false;
    }

    ImageView iv1;
    Button b1,b2,b3,b4,b5,b6;

    ImageView iv_translate;

    View.OnClickListener setSpringEnd=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.b_setEnd1:
                    scaleSpring.setEndValue(0);
                    break;
                case R.id.b_setEnd2:
                    scaleSpring.setEndValue(0.2);
                    break;
                case R.id.b_setEnd3:
                    scaleSpring.setEndValue(0.4);
                    break;
                case R.id.b_setEnd4:
                    scaleSpring.setEndValue(0.6);
                    break;
                case R.id.b_setEnd5:
                    scaleSpring.setEndValue(0.8);
                    break;
                case R.id.b_setEnd6:
                    scaleSpring.setEndValue(1.0);
                    break;
            }
        }
    };


    View.OnClickListener translateSpringSetter=new View.OnClickListener(){
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onClick(View v) {
            if(mMoveUp){
                translateSpring.setEndValue(mOrigY);
            } else{
                mOrigY=iv_translate.getY();
                translateSpring.setEndValue(mOrigY-100f);
            }
            mMoveUp=!mMoveUp;
        }
    };


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_rebound);
        initScaleViews();

        // Create a system to run the physics loop for a set of springs.
        springSystem = SpringSystem.create();

        // scale spring
        createScaleSpring();

        // translate spring
        createTranslateSpring();
        iv_translate=(ImageView)findViewById(R.id.iv_translate);
        iv_translate.setOnClickListener(translateSpringSetter);
        // view.getY() will not return proper value in oncreat/onresume.....
        // view.getY() will only return proper value after the UI has been fully established
        myUIhander.sendEmptyMessageDelayed(1,500);

    }


    private void initScaleViews() {
        b1=(Button)findViewById(R.id.b_setEnd1);
        b2=(Button)findViewById(R.id.b_setEnd2);
        b3=(Button)findViewById(R.id.b_setEnd3);
        b4=(Button)findViewById(R.id.b_setEnd4);
        b5=(Button)findViewById(R.id.b_setEnd5);
        b6=(Button)findViewById(R.id.b_setEnd6);
        b1.setOnClickListener(setSpringEnd);
        b2.setOnClickListener(setSpringEnd);
        b3.setOnClickListener(setSpringEnd);
        b4.setOnClickListener(setSpringEnd);
        b5.setOnClickListener(setSpringEnd);
        b6.setOnClickListener(setSpringEnd);
        iv1=(ImageView)findViewById(R.id.iv_red);
        iv1.setOnTouchListener(this);
    }


    private void createScaleSpring() {
        // Add a scaleSpring to the system.
        scaleSpring = springSystem.createSpring();
        // Add a listener to observe the motion of the scaleSpring.
        scaleSpring.addListener(new SimpleSpringListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onSpringUpdate(Spring spring) {   // callback on every scaleSpring update --> active scaleSpring
                // Log.i("REBOUND","onSpringUpdate--"+Thread.currentThread().getName()); // main thread
                // You can observe the updates in the scaleSpring state by asking its current value in onSpringUpdate.
                float value = (float) spring.getCurrentValue();
                float scale = 1f - (value * 0.5f);
                iv1.setScaleX(scale);     // this part of the code set the scale of the image such that
                iv1.setScaleY(scale);
                // scaleSpring.setEndValue(1)-->animate to 50%
                // scaleSpring.setEndValue(0.8)-->animate to 60%  : 1-0.8*0.5=0.6
                // 。。。
                // scaleSpring.setEndValue(0)-->animate to100%
            }

            @Override
            public void onSpringActivate(Spring spring) {
                Log.i("REBOUND","onSpringActivate--"+Thread.currentThread().getName());
            }

            @Override
            public void onSpringAtRest(Spring spring) {
                Log.i("REBOUND","onSpringAtRest--"+Thread.currentThread().getName());
            }

            @Override
            public void onSpringEndStateChange(Spring spring) {
                Log.i("REBOUND","onSpringEndStateChange--"+Thread.currentThread().getName());
            }
        });

        // set tension and friction
        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        scaleSpring.setSpringConfig(config);
    }





    private void createTranslateSpring() {
        // Add a scaleSpring to the system.
        translateSpring = springSystem.createSpring();
        // Add a listener to observe the motion of the scaleSpring.
        translateSpring.addListener(new SimpleSpringListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                iv_translate.setY(value);
            }
        });

        // set tension and friction
        SpringConfig config = new SpringConfig(TENSION, DAMPER);
        translateSpring.setSpringConfig(config);
    }

}
