package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/2/28.
 */

public class CostumRV extends RecyclerView {


    public CostumRV(Context context) {
        super(context);
    }

    public CostumRV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CostumRV(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // last position ---relative to the view
    private float mLastX=0;
    private float mLastY=0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean ret=super.onInterceptTouchEvent(e);
        Log.i("bbb","custom rv:  onInterceptTouchEvent RETURN:"+ret);  // this is always false
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float xTouch = e.getX();
        float yTouch = e.getY();
        float deltaY = yTouch-mLastY;
        mLastX = xTouch;
        mLastY = yTouch;
        Log.i("eee","custom rv-------   onTouchEvent-ACTION:"+e.getAction()+"  OFFSET-V():"+computeVerticalScrollOffset() +" deltaY:"+deltaY+"   lastY:"+mLastY);

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

//        if ((computeVerticalScrollOffset()==0)&&(deltaY>0) ){
//            Log.i("bbb","custom rv:   onTouchEvent-ACTION:"+e.getAction()+"  OFFSET-V():"+computeVerticalScrollOffset() +" deltaY:"+deltaY);
//            return false;
//        }

        boolean returnV=super.onTouchEvent(e);
        Log.i("ggg","custom rv-------   onTouchEvent-ACTION:"+e.getAction()+"  OFFSET-V():"+computeVerticalScrollOffset() +" deltaY:"+deltaY+"     default-return:"+returnV);

//        Log.i("aaa","custom rv-------   onTouchEvent-ACTION:"+e.getAction()+"  OFFSET-V():"+computeVerticalScrollOffset() +" deltaY:"+deltaY+"   lastY:"+mLastY +"     default-return:"+returnV);
        return returnV;   //  returnV
    }





}
