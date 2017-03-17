package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewConfigurationCompat;
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
import com.nanyi545.www.materialdemo.testPullToRefresh.eleme.TestPullRefreshActivity4;

/**
 * Created by Administrator on 2017/3/9.
 *
 *
 *
 * only used in  {@link TestPullRefreshActivity4}
 */
public class CustomScrollingViewBehaviour4 extends AppBarLayout.ScrollingViewBehavior {

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        Log.i("kkk", "CustomScrollingViewBehaviour4   onInterceptTouchEvent->child:" + child.getClass().getName());
        if (parent instanceof CoordinatorPullToRefresh2) {
            return ((CoordinatorPullToRefresh2) parent).shouldIntercept();
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }


    public CustomScrollingViewBehaviour4() {
    }

    /**
     * YOU HAVE GOT TO HAVE THIS CONSTRUCTOR !!1
     *
     * @param context
     * @param attrs
     */
    public CustomScrollingViewBehaviour4(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupConfigurations(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        boolean ret = super.layoutDependsOn(parent, child, dependency);

        if (dependency instanceof AppBarLayout) {
            final AppBarLayout appBar = (AppBarLayout) dependency;
            if (manager == null) {
                manager = CollapsHolder.CollapsHolderManager.getInstance(appBar, R.id.collapse_holder2, R.id.collapse_holder1);
            }
            if (pullToRefreshHeader == null) {
//                pullToRefreshHeader = (CollapsHolder) appBar.findViewById(R.id.collapse_holder_pull);


                // TODO  modify this ...
                ViewTreeObserver viewTreeObserver = appBar.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            pullToRefreshHeader.collapseAll();

                            if(appBar.getViewTreeObserver().isAlive()){
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                    appBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                } else {
                                    appBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                            }


                        }
                    });
                }


            }
        }

        return ret;
    }

    CollapsHolder.CollapsHolderManager manager;
    CollapsHolder pullToRefreshHeader;


    //   dy < 0  scroll down
    //   dy > 0  scroll up
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i("kkk", "onNestedScroll   dyConsumed:" + dyConsumed + "   dyUnconsumed:" + dyUnconsumed);
        if (dyUnconsumed < 0) {
            if (manager != null) {


                if (!manager.isFullyExpanded()) {  //   ...  collapse holder manager not yet fully expanded
                    manager.collapse(dyUnconsumed); // expand it
                } else {  //  ...  collapse holder manager already  fully expanded

                    if (!pullToRefreshHeader.isFullyExpanded()){
                        pullToRefreshHeader.collapse(dyUnconsumed);
                    }

                }


            }
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }


    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        Log.i("kkk", "onNestedPreScroll   dy:" + dy);
        if (dy > 0) {
            if (manager != null) {

                if (!pullToRefreshHeader.isFullyCollapsed()) {
                    Log.i("kkk", "onNestedPreScroll---> collapse pullToRefreshHeader   dy:" + dy);
                    pullToRefreshHeader.collapse(dy);


                } else {
                    Log.i("kkk", "onNestedPreScroll---> collapse manager   dy:" + dy);
                    if (!manager.isFullyCollapsed()) {
                        manager.collapse(dy);
                    } else {

                    }


                }

            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        boolean defaultRet = super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
//        Log.i("kkk","CustomScrollingViewBehaviour3-------onStartNestedScroll:default ret"+defaultRet+"  child:"+child.getClass().getName()+"   directTargetChild:"+directTargetChild.getClass().getName()+"    target:"+target.getClass().getName());
        RecyclerView casted = (RecyclerView) child;
        boolean recyclerAtTop = (casted.computeVerticalScrollOffset() <= 0);
        return true;
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {

//        pullToRefreshHeader.collapseAllSmooth(true);

        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    /**
     * handle nested fling .....
     */
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;

    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;
    private static final int MAX_FLING_VELOCITY_ADJUSTMENT = 8;

    private void setupConfigurations(Context context) {

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity() / MAX_FLING_VELOCITY_ADJUSTMENT;
//        Log.i("kkk","mTouchSlop:"+mTouchSlop+"   mMinimumFlingVelocity:"+mMinimumFlingVelocity+"   mMaximumFlingVelocity:"+mMaximumFlingVelocity);

    }


    //   velocityY >  0   fling up            velocityY < 0   fling down ....
    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {

        RecyclerView casted = (RecyclerView) child;
        boolean recyclerAtTop = (casted.computeVerticalScrollOffset() <= 5);

        if (velocityY < 0) {
            if (recyclerAtTop) {
                if (manager != null) {
                    manager.smoothCollapseAll(false);  // smooth expand
                }
            }
        }

        boolean defaultRet = super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);

        Log.i("kkk", "onNestedFling  velocityY:" + velocityY + "   consumed:" + consumed + "   defaultRet:" + defaultRet + "   RV offset:" + casted.computeVerticalScrollOffset());
        return defaultRet;
    }


    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        if (velocityY > 0) {
            if (manager != null) {
                manager.smoothCollapseAll(true);   // smooth collapse
            }
        }


        boolean defaultRet = super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        Log.i("kkk", "onNestedPreFling  velocityY:" + velocityY + "   defaultRet:" + defaultRet);

        return defaultRet;
    }


}
