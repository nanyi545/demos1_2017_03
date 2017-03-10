package com.nanyi545.www.materialdemo.coordinatorWithoutAppbarLO;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.nanyi545.www.materialdemo.R;
import com.nanyi545.www.materialdemo.collapse_layout.CollapsHolder;
import com.nanyi545.www.materialdemo.testPullToRefresh.CoordinatorPullToRefresh;

/**
 * Created by Administrator on 2017/3/1.
 */
public class CustomAppbarLoBehaviour2 extends AppBarLayout.Behavior {

    public CustomAppbarLoBehaviour2() {
    }

    public CustomAppbarLoBehaviour2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }





    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {

        RecyclerView casted= (RecyclerView) target;
        boolean recyclerAtTop =(casted.computeVerticalScrollOffset()<=0);

        if (dy>0){
            if (recyclerAtTop){
                if (manager!=null){
                    manager.collapse(dy);
                }
            }
            Log.i("kkk","----  collapse--------- nested pre scroll::::"+dy+"  rv scroll:"+casted.computeVerticalScrollOffset() +"   consumedY:"+consumed[1] );  //
        }

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }



    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {


        if(dyUnconsumed<0){
            RecyclerView casted= (RecyclerView) target;
            boolean recyclerAtTop =(casted.computeVerticalScrollOffset()<=0);

            if (recyclerAtTop){
                if (manager!=null){
                    manager.collapse(dyUnconsumed);
                }
            }
            Log.i("kkk","-----expand-----nested scroll:dyConsumed:"+dyConsumed+"   dyUnconsumed:"+dyUnconsumed);  //

        }

        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }



    CollapsHolder.CollapsHolderManager manager;

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        boolean ret=super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
        Log.i("kkk","extended AppBarLayout.Behavior---- onStartNestedScroll  default ret:"+ret+"  directTargetChild:"+directTargetChild.getClass().getName()+"  target:"+target.getClass().getName());

        if (manager==null){
            manager=CollapsHolder.CollapsHolderManager.getInstance(child, R.id.collapse_holder2,R.id.collapse_holder1);
        }

        RecyclerView casted= (RecyclerView) directTargetChild;
        boolean recyclerAtTop =(casted.computeVerticalScrollOffset()<=0);

        return true;
    }




    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {

        Log.i("kkk","extended AppBarLayout.Behavior   --- onStopNestedScroll:"+"   target:"+target.getClass().getName());

        /**
         *  for  at least extending   AppBarLayout.Behavior
         *
         *  this method (onStopNestedScroll) is called  only once in pre-L systems  --> at the very end
         *
         *  but is called twice on after-L systems --> both at the begining and at the end , the "extra call" at the begining hampers the scroll
         *
         *  ???---why called twice--???
         *
         */

        super.onStopNestedScroll(coordinatorLayout, abl, target);
    }





}


