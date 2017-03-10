package com.nanyi545.www.materialdemo.coordinatorWithoutAppbarLO;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nanyi545.www.materialdemo.collapse_layout.CollapsHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */
public class CustomHeaderScrollingViewBehavior extends CustomOffsetBehavior<View> {



    private final Rect mTempRect1 = new Rect();
    private final Rect mTempRect2 = new Rect();

    private int mVerticalLayoutGap = 0;
    private int mOverlayTop;

    public CustomHeaderScrollingViewBehavior() {}

    public CustomHeaderScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        boolean ret=(child instanceof RecyclerView)&&(dependency instanceof RelativeLayout);
        if (ret) dependencyRL= (RelativeLayout) dependency;
        return ret;
    }

    RelativeLayout dependencyRL;




    CollapsHolder.CollapsHolderManager manager;

    public void setManager(CollapsHolder.CollapsHolderManager manager) {
        this.manager = manager;
    }



    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        Log.i("mmm","onNestedPreScroll  child.getScrollY():"+child.getScrollY()+"  dy:"+dy+"  target:"+target.getClass().getName());

        RecyclerView casted= (RecyclerView) child;

        boolean recyclerAtTop =(casted.computeVerticalScrollOffset()<=0);
        boolean collapsHoldersFullyCollapsed =manager.isFullyCollapsed();
        boolean collapsHoldersFullyExpanded =manager.isFullyExpanded();


        if (dy>0)   //  scroll up   --> to collapse  dy > 0
        {
            if (!collapsHoldersFullyCollapsed){
                if (manager!=null){
                    manager.collapse(dy);
                }
            }
        }
        if (dy<0)  //    scroll down --> to expand    deltaY<0
        {
            if (!collapsHoldersFullyExpanded){
                if (manager!=null){
                    manager.collapse(dy);
                }
            }
        }



        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }


    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i("mmm","onNestedScroll  child.getScrollY():"+child.getScrollY()+"  dyConsumed:"+dyConsumed+"  dyUnconsumed:"+dyUnconsumed+"  target:"+target.getClass().getName());

        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }


    /**
     *
     * @param coordinatorLayout
     * @param child
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes
     * @return
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {

        RecyclerView casted= (RecyclerView) child;

        boolean recyclerAtTop =(casted.computeVerticalScrollOffset()<=10);
        boolean collapsHoldersFullyCollapsed =manager.isFullyCollapsed();
        boolean collapsHoldersFullyExpanded =manager.isFullyExpanded();

        //    !recyclerAtTop   &&   collapsHoldersFullyCollapsed    -->  false
        //    recyclerAtTop    --> true
        Log.i("mmm","onStartNestedScroll:"+recyclerAtTop);

        return recyclerAtTop;

    }




    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child,
                                  int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec,
                                  int heightUsed) {
        final int childLpHeight = child.getLayoutParams().height;
        if (childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                || childLpHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // If the menu's height is set to match_parent/wrap_content then measure it
            // with the maximum visible height

            final List<View> dependencies = parent.getDependencies(child);
            final View header = findFirstDependency(dependencies);
            if (header != null) {
                if (ViewCompat.getFitsSystemWindows(header)
                        && !ViewCompat.getFitsSystemWindows(child)) {
                    // If the header is fitting system windows then we need to also,
                    // otherwise we'll get CoL's compatible measuring
                    ViewCompat.setFitsSystemWindows(child, true);

                    if (ViewCompat.getFitsSystemWindows(child)) {
                        // If the set succeeded, trigger a new layout and return true
                        child.requestLayout();
                        return true;
                    }
                }

                if (ViewCompat.isLaidOut(header)) {
                    int availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec);
                    if (availableHeight == 0) {
                        // If the measure spec doesn't specify a size, use the current height
                        availableHeight = parent.getHeight();
                    }

                    final int height = availableHeight - header.getMeasuredHeight()
                            + getScrollRange(header);
                    final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,
                            childLpHeight == ViewGroup.LayoutParams.MATCH_PARENT
                                    ? View.MeasureSpec.EXACTLY
                                    : View.MeasureSpec.AT_MOST);

                    // Now measure the scrolling view with the correct height
                    parent.onMeasureChild(child, parentWidthMeasureSpec,
                            widthUsed, heightMeasureSpec, heightUsed);

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void layoutChild(final CoordinatorLayout parent, final View child,
                               final int layoutDirection) {
        final List<View> dependencies = parent.getDependencies(child);
        final View header = findFirstDependency(dependencies);

        if (header != null) {
            final CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            final Rect available = mTempRect1;
            available.set(parent.getPaddingLeft() + lp.leftMargin,
                    header.getBottom() + lp.topMargin,
                    parent.getWidth() - parent.getPaddingRight() - lp.rightMargin,
                    parent.getHeight() + header.getBottom()
                            - parent.getPaddingBottom() - lp.bottomMargin);

            final Rect out = mTempRect2;
            GravityCompat.apply(resolveGravity(lp.gravity), child.getMeasuredWidth(),
                    child.getMeasuredHeight(), available, out, layoutDirection);

            final int overlap = getOverlapPixelsForOffset(header);
//            Log.i("mmm","layoutChild--overlap:"+overlap);


            child.layout(out.left, out.top - overlap, out.right, out.bottom - overlap);
            mVerticalLayoutGap = out.top - header.getBottom();
        } else {
            // If we don't have a dependency, let super handle it
            super.layoutChild(parent, child, layoutDirection);
            mVerticalLayoutGap = 0;
        }
    }

    float getOverlapRatioForOffset(final View header) {
        return 1f;
    }

    final int getOverlapPixelsForOffset(final View header) {
        return mOverlayTop == 0
                ? 0
                : MathUtils.constrain(Math.round(getOverlapRatioForOffset(header) * mOverlayTop),
                0, mOverlayTop);

    }

    private static int resolveGravity(int gravity) {
        return gravity == Gravity.NO_GRAVITY ? GravityCompat.START | Gravity.TOP : gravity;
    }

    View findFirstDependency(List<View> views){
        for (View view:views
             ) {
            if (view instanceof RelativeLayout) return view;
        }
        return null;
    }

    int getScrollRange(View v) {
        return 0;
//        return v.getMeasuredHeight();
    }

    /**
     * The gap between the top of the scrolling view and the bottom of the header layout in pixels.
     */
    final int getVerticalLayoutGap() {
        return mVerticalLayoutGap;
    }

    /**
     * Set the distance that this view should overlap any {@link AppBarLayout}.
     *
     * @param overlayTop the distance in px
     */
    public final void setOverlayTop(int overlayTop) {
        mOverlayTop = overlayTop;
    }

    /**
     * Returns the distance that this view should overlap any {@link AppBarLayout}.
     */
    public final int getOverlayTop() {
        return mOverlayTop;
    }



    static class MathUtils {

        static int constrain(int amount, int low, int high) {
            return amount < low ? low : (amount > high ? high : amount);
        }

        static float constrain(float amount, float low, float high) {
            return amount < low ? low : (amount > high ? high : amount);
        }

    }



}
