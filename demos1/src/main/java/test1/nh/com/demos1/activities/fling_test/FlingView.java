package test1.nh.com.demos1.activities.fling_test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/10/8.
 */
public class FlingView extends View {

    public FlingView(Context context) {
        super(context);
    }

    public FlingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    private VelocityTracker mVelocityTracker;
    private Scroller flingScroller;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);
        int action = event.getActionMasked();

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;

    }
}
