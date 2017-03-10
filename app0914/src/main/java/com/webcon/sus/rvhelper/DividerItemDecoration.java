package com.webcon.sus.rvhelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.webcon.sus.demo.R;


/**
 * Created by Administrator on 15-10-17.
 * draw item dividing lines(horizontal for vertical) for
 *             recyclerView that uses LinearLayoutManager
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider  // 由theme中决定 <item name="android:listDivider">@drawable/divider_bg</item>
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private Drawable mDivider;

    private Drawable mDivider_teal;
    // the orientation of the recyclerView(which uses LinearLayoutManager)
    private int mOrientation;


    public DividerItemDecoration(Context context, int orientation) {
        setOrientation(orientation);

        //method 1: obtain divider drawable
//        final TypedArray a = context.obtainStyledAttributes(ATTRS);
//        mDivider = a.getDrawable(0);
//        a.recycle();
        //method 2: obtain divider drawable
        mDivider=context.getResources().getDrawable(R.drawable.divider_1);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    //  onDraw     --> draw before the views are drawn
    //  onDrawOver --> draw after the views are drawn
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        super.onDraw(c, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

// draw  dividing line for vertical list **
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();  //in pixels
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
//            android.support.v7.widget.RecyclerView v = new android.support.v7.widget.RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin; //bottom margin in pixels of the child.
//            Log.i("AAA","i-child.getBottom()-params.bottomMargin"+i+"*"+child.getBottom()+"*"+params.bottomMargin);
            final int bottom = top + mDivider.getIntrinsicHeight();  // height of the divider drawable

            mDivider.setBounds(left, top, right, bottom);
//            Log.i("AAA","i-left-top-right-bottom:"+i+"*"+left+"*"+top+"*"+right+"*"+bottom+"*");
            mDivider.draw(c);
        }
    }

// draw  dividing line for Horizontal list
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);


        // outRect.top/buttom/left/right decides the distances between the recycler view items

        // set distance(top bottom) for the XXth item
        if (view == parent.getChildAt(0)){
            int space=30;
            outRect.bottom = space;
            outRect.top = space;
        }

//        int space=10;
//        outRect.left = space;
//        outRect.right = space;
//        outRect.bottom = space;

        // Set outRect values
//        if (mOrientation == VERTICAL_LIST) {
//            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
//        } else {
//            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
//        }

    }
}
