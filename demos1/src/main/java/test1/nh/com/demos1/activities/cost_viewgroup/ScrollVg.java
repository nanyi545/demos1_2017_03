package test1.nh.com.demos1.activities.cost_viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import test1.nh.com.demos1.utils.math.MathVector2D;

/**
 * Created by Administrator on 2016/10/21.
 */
public class ScrollVg extends ViewGroup {


    public ScrollVg(Context context) {
        super(context);
        init();
    }

    public ScrollVg(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollVg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        testPaint=new Paint();
        testPaint.setAntiAlias(true);
        testPaint.setColor(Color.rgb(200,20,20));
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count=getChildCount();
        int viewWidth=getMeasuredWidth();
// ------what's this for ??
//        MarginLayoutParams mlp= (MarginLayoutParams) getLayoutParams();
//        mlp.height=viewHeight*count;
//        setLayoutParams(mlp);

        for (int  i=0;i<count;i++){
            View childView=getChildAt(i);
            if (childView.getVisibility()!=View.GONE){
                childView.layout(viewWidth*i,t,viewWidth*(i+1),b);
            }
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count=getChildCount();
        for (int  i=0;i<count;i++){
            View childView=getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
    }


    int mLastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getActionMasked();

        int x= (int) event.getX();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX=x;

                break;

            case MotionEvent.ACTION_MOVE:

                int dx=mLastX-x;
                scrollBy(dx,0);
                mLastX=x;

                break;

        }

        Log.i("BBB","mLastX:"+mLastX);
//        invalidate();
        return true;
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }


    Paint testPaint;



    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
//        canvas.drawCircle(mLastX+50,50,30,testPaint);
    }
}
