package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.nanyi545.www.materialdemo.coordinatorWithoutAppbarLO.CustomScrollingViewBehaviour3;

/**
 * Created by Administrator on 2017/3/1.
 */
public class CustomAppbarLoBehaviour3 extends AppBarLayout.Behavior {

    public CustomAppbarLoBehaviour3() {
    }

    public CustomAppbarLoBehaviour3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed) {
        Log.i("kkk","----nested pre scroll::::"+dy+"  child:"+child.getClass().getName()+"   target:"+target.getClass().getName()+"  AppBarLayout:"  +"  coor scrollY:"+coordinatorLayout.getScrollY());  //

        boolean appBarFullyExpanded=(child.getHeight()==child.getBottom());

        if(coordinatorLayout instanceof CoordinatorPullToRefresh){
            if((dy<0)&&(target instanceof RecyclerView)&&(((RecyclerView) target).computeVerticalScrollOffset()==0)&&appBarFullyExpanded){
                ((CoordinatorPullToRefresh)coordinatorLayout).dragDown1(dy);
            }

            if((dy<0)&&(target instanceof NestedScrollView)&&(((NestedScrollView) target).computeVerticalScrollOffset()==0)&&appBarFullyExpanded){
                ((CoordinatorPullToRefresh)coordinatorLayout).dragDown1(dy);
            }
        }

        if(coordinatorLayout instanceof CoordinatorPullToRefresh2){
            if((target instanceof RecyclerView)&&(((RecyclerView) target).computeVerticalScrollOffset()==0)&&appBarFullyExpanded){
                ((CoordinatorPullToRefresh2)coordinatorLayout).dragDown(dy);
            }

        }

//        if (coordinatorLayout.getScrollY()<0)return; ///???

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i("kkk","----nested scroll:dyConsumed:"+dyConsumed+"   dyUnconsumed:"+dyUnconsumed);  //


//        if (coordinatorLayout.getScrollY()<0)return; // ???

        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }



    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        boolean ret=super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
//        Log.i("lll","extended AppBarLayout.Behavior---- onStartNestedScroll  default ret:"+ret+"  directTargetChild:"+directTargetChild.getClass().getName()+"  target:"+target.getClass().getName());
        if (target instanceof RecyclerView){
            RecyclerView casted= (RecyclerView) target;
            CoordinatorLayout.Behavior behavior= ((CoordinatorLayout.LayoutParams)casted.getLayoutParams()).getBehavior();
            if (behavior instanceof CustomScrollingViewBehaviour3){
                CustomScrollingViewBehaviour3 castedBehavior= (CustomScrollingViewBehaviour3) behavior;
                Log.i("lll","extended AppBarLayout.Behavior---- onStartNestedScroll      fully expanded:"+castedBehavior.ifFullyExpanded()+"  default ret:"+ret);
                return castedBehavior.ifFullyExpanded();
            }
        }
        return ret;
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target) {
//        int dy=coordinatorLayout.getScrollY();
//        coordinatorLayout.scrollBy(0,-dy);
//        Log.i("bbb","coordinatorLayout dy:"+dy);
        Log.i("lll","extended AppBarLayout.Behavior   --- onStopNestedScroll:"+"   target:"+target.getClass().getName());

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
        if (coordinatorLayout instanceof  CoordinatorPullToRefresh) ((CoordinatorPullToRefresh)coordinatorLayout).releaseDrag();
        if (coordinatorLayout instanceof  CoordinatorPullToRefresh2) ((CoordinatorPullToRefresh2)coordinatorLayout).releaseDrag();


        super.onStopNestedScroll(coordinatorLayout, abl, target);
    }


}


