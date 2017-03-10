package com.nanyi545.www.materialdemo.nestedScroll.no_coordinator_test.testWithCostumView;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/3/3.
 */
public class CostumScrollView2 extends NestedScrollView {

    public CostumScrollView2(Context context) {
        super(context);
    }

    public CostumScrollView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CostumScrollView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        boolean ret=super.onStartNestedScroll(child, target, nestedScrollAxes);
        Log.i("mmm","NestedScrollingParent:  onStartNestedScroll"+ret);
        return ret ;
    }


    @Override
    public void onStopNestedScroll(View target) {
        Log.i("mmm","NestedScrollingParent:  onStopNestedScroll");
        super.onStopNestedScroll(target);
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        super.onNestedScrollAccepted(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        scrollBy(dx,dy);
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        scrollBy(dxUnconsumed,dyUnconsumed);
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }
}
