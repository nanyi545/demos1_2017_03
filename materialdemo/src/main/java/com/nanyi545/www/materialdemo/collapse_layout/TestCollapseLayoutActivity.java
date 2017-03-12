package com.nanyi545.www.materialdemo.collapse_layout;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.nanyi545.www.materialdemo.R;

public class TestCollapseLayoutActivity extends AppCompatActivity implements GestureDetector.OnGestureListener  {

    public static void start(Context c){
        Intent i=new Intent(c,TestCollapseLayoutActivity.class);
        c.startActivity(i);
    }


    CollapsHolder holder;


    GestureDetectorCompat detector;
    CollapsHolder.CollapsHolderManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_collapse_layout);
        holder= (CollapsHolder) findViewById(R.id.collapse_holder);

        detector = new GestureDetectorCompat(this, this);

        manager=CollapsHolder.CollapsHolderManager.getInstance(getWindow().getDecorView().getRootView(),R.id.collapse_holder2,R.id.collapse_holder1);


    }


    public void doExpand(View v){
        holder.collapse(-10);
//        holder.expand();
//        manager.collapse(-1);
//        holder.collapseAllSmooth(false);
    }

    public void doCollapse(View v){
//        holder.collapseAllSmooth(true);
        holder.collapse(10);
//        manager.collapse(1);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        manager.collapse((int) distanceY);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
