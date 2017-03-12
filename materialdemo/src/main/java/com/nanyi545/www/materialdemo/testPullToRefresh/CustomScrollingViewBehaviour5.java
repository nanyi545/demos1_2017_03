package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

import com.nanyi545.www.materialdemo.R;
import com.nanyi545.www.materialdemo.collapse_layout.CollapsHolder;

/**
 * Created by Administrator on 2017/3/9.
 *
 *
 *
 * only used in  {@link TestPullRefreshActivity4}
 */
public class CustomScrollingViewBehaviour5 extends AppBarLayout.ScrollingViewBehavior {

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        Log.i("kkk", "CustomScrollingViewBehaviour4   onInterceptTouchEvent->child:" + child.getClass().getName());
//        if (parent instanceof CoordinatorPullToRefresh2) {
//            return ((CoordinatorPullToRefresh2) parent).shouldIntercept();
//        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }


    public CustomScrollingViewBehaviour5() {
    }

    /**
     * YOU HAVE GOT TO HAVE THIS CONSTRUCTOR !!1
     *
     * @param context
     * @param attrs
     */
    public CustomScrollingViewBehaviour5(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        boolean ret = super.layoutDependsOn(parent, child, dependency);

        return ret;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i("kkk", "onNestedScroll   dyConsumed:" + dyConsumed + "   dyUnconsumed:" + dyUnconsumed);



        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }


    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        Log.i("kkk", "onNestedPreScroll   dy:" + dy);
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        boolean defaultRet = super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
//        Log.i("kkk","CustomScrollingViewBehaviour3-------onStartNestedScroll:default ret"+defaultRet+"  child:"+child.getClass().getName()+"   directTargetChild:"+directTargetChild.getClass().getName()+"    target:"+target.getClass().getName());

        return true;
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {


        super.onStopNestedScroll(coordinatorLayout, child, target);
    }



    //   velocityY >  0   fling up            velocityY < 0   fling down ....
    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {

        NestedScrollView casted = (NestedScrollView) child;
        boolean recyclerAtTop = (casted.computeVerticalScrollOffset() <= 5);


        boolean defaultRet = super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);

        Log.i("kkk", "onNestedFling  velocityY:" + velocityY + "   consumed:" + consumed + "   defaultRet:" + defaultRet + "   RV offset:" + casted.computeVerticalScrollOffset());
        return defaultRet;
    }


    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {

        boolean defaultRet = super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        Log.i("kkk", "onNestedPreFling  velocityY:" + velocityY + "   defaultRet:" + defaultRet);

        return defaultRet;
    }


}
