package test1.nh.com.demos1.customView;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/7/7.
 */
public class DragLayout extends LinearLayout {




    private final ViewDragHelper mDragHelper;
    private View mDragView;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 100.0f, new DragHelperCallback());
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        return true;
    }



    public void setmDragView(View mDragView) {
        this.mDragView = mDragView;
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {


        @Override
        public boolean tryCaptureView(View child, int pointerId)
        {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx)
        {
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - mDragView.getWidth() - leftBound;

            Log.i("drag","leftBound:"+leftBound+"   rightBound:"+rightBound);

//            final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            final int newLeft = Math.min(Math.max(left, -300), 300);
            Log.i("drag","left:"+left+"   newLeft:"+newLeft);

//            int newLeft=left;
//            Log.i("drag","left:"+left+"   newLeft:"+newLeft);
            DragLayout.this.setTranslationX(newLeft);
            return newLeft;

        }


        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.i("drag","on view released :  xvel:"+xvel+" yvel:"+yvel);
            if (xvel<-200f){
                DragLayout.this.setTranslationX(-40);
            }
            if (xvel>200f){
                DragLayout.this.setTranslationX(0);
            }

        }



    }


}