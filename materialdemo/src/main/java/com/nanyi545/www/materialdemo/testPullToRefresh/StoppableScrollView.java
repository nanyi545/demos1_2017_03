package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/3/11.
 */

public class StoppableScrollView extends NestedScrollView {



    public StoppableScrollView(Context context) {
        super(context);
    }

    public StoppableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StoppableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     *   this way the StoppableScrollView  is not scrollable by itself
     *
     *   only in coordinator layout
     *   -->
     *   assigning it a behaviour   {@link CustomScrollingViewBehaviour5 }  so that the scroll is handled/distributed  in coordinator layout
     *
     */
    private boolean scrollable=false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(!scrollable) return false;
        else return super.onTouchEvent(e);
    }

    public void setScrollable(boolean scrollable){
        Log.i("kkk","setScrollable:"+scrollable);
        this.scrollable=scrollable;
    }



}
