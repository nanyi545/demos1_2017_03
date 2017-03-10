package com.nanyi545.www.materialdemo.nestedScroll.no_coordinator_test;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/3/3.
 */
public class CostumScrollView extends NestedScrollView {

    public CostumScrollView(Context context) {
        super(context);
    }

    public CostumScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CostumScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.i("aaa","parent onNestedPreScroll----dy:"+dy+"  consumed:"+consumed[1]);
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i("aaa","parent onNestedScroll----dyConsumed:"+dyConsumed+"   dyUnconsumed:"+dyUnconsumed);
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }




}
