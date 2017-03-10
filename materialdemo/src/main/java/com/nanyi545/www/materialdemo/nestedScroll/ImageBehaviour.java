package com.nanyi545.www.materialdemo.nestedScroll;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/3/1.
 */
public class ImageBehaviour extends CoordinatorLayout.Behavior<ImageView> {


    public ImageBehaviour() {
    }

    public ImageBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, ImageView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i("bbb","imageView---- nested scroll");
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, ImageView child, View target, int dx, int dy, int[] consumed) {
        Log.i("bbb","imageView---- nested pre scroll");
        coordinatorLayout.scrollBy(dx,dy);
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, ImageView child, View directTargetChild, View target, int nestedScrollAxes) {
        boolean ret=super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        Log.i("bbb","imageView---- onStartNestedScroll  default ret:"+ret+"  directTargetChild:"+directTargetChild.getClass().getName()+"  target:"+target.getClass().getName());
        return ((directTargetChild instanceof NestedScrollingChildView)||(target instanceof NestedScrollingChildView)) ;
    }


}
