package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/3/11.
 */

public class StoppableRv extends RecyclerView {




    public StoppableRv(Context context) {
        super(context);
    }

    public StoppableRv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StoppableRv(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean scrollable=false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(!scrollable) return false;
        else return super.onTouchEvent(e);
    }



}
