package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.nanyi545.www.materialdemo.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 2017/2/28.
 *
 * this Class only supports pull down ....
 *
 */
public class CoordinatorPullToRefresh extends CoordinatorPullable {

    public CoordinatorPullToRefresh(Context context) {
        this(context,null);
    }

    public CoordinatorPullToRefresh(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CoordinatorPullToRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private Scroller mScroller;

    // last position on touch screen
    private float mLastX=0;
    private float mLastY=0;

    private void initView(Context context){
        mScroller=new Scroller(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean ret=super.onInterceptTouchEvent(e);
        Log.i("ccc","coordinator:  onInterceptTouchEvent RETURN:"+ret);  // this is always false
        return ret;
    }

    private int totalDrag=0;

    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
    }

    public void dragDown1(int dy){   // dy < 0
        if (mScroller.isFinished()){
            totalDrag=totalDrag+dy;
            Log.i("fff","--outer--coordinator LO drag down dy:"+dy+"  totalDrag:"+totalDrag+"   revealHeight:"+getRevealHeight());  //
            if ((-totalDrag)<getRevealHeight()){
                Log.i("aaa","----coordinator LO drag down dy:"+dy);  //
                Log.i("fff","--inner--coordinator LO drag down dy:"+dy+"  totalDrag:"+totalDrag+"   revealHeight:"+getRevealHeight());  //
                this.scrollBy(0, dy);
                float progress= (-totalDrag+0.0f) / getRevealHeight();
                getRevealContent().setProgress(progress);
            } else {
                totalDrag=-getRevealHeight();
                this.scrollTo(0, totalDrag);
                getRevealContent().setProgress(1f);
            }

        }
    }

    public void releaseDrag(){
        int dy =  - getScrollY();

        Log.i("fff","--release: dy:"+dy+"  trigger release:"+(dy>0)+"   totalDrag now at:"+totalDrag);

        if(dy>0){
            mScroller.startScroll(0, getScrollY(), 0, dy,400);
            totalDrag=0;
            invalidate();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xTouch = event.getX();
        float yTouch = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("ccc","--------action down-----------");
                break;
            case MotionEvent.ACTION_MOVE:


                float deltaX = xTouch-mLastX;
                float deltaY = yTouch-mLastY;
                float scrollByStart = deltaY;
                Log.i("ccc","--------action move-----------scrollByStart:"+scrollByStart+"    getScrollY:"+getScrollY());

                if (( getScrollY() <= 0)&& ( scrollByStart >= 0)){    // only when scroll down( scrollByStart>= 0 ) is allowed  !!!
                    if(((-getScrollY()+(scrollByStart)) < getRevealHeight())   ){     //  when scroll down within  revealHeight ( scrollByStart < revealHeight )
                        scrollBy(0, (int) -scrollByStart);
                    } else {
                        scrollTo(0, -getRevealHeight());
                    }
                }

                float progress= (-getScrollY()+0f) / getRevealHeight();
                getRevealContent().setProgress(progress);

                break;
            case MotionEvent.ACTION_UP:
                Log.i("ccc","--------action up-----------");
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                //-- full screen scroll --
//                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
//                int dx = targetIndex * getWidth() - getScrollX();

                float prog= (-getScrollY()+0f) / getRevealHeight();
                getRevealContent().setProgress(prog);

                int dy =  - getScrollY();

                //  ---scroll to with out animation
//                scrollTo(getScrollX()+dx,0);
                // ---use scroller, scroll with animation ---init scroller ...
                mScroller.startScroll(0, getScrollY(), 0, dy,400);
                invalidate();

                break;
            default:
                break;
        }

        mLastX = xTouch;  // this gets updated in MotionEvent.ACTION_DOWN
        mLastY = yTouch;
//        Log.i("ccc","---------mLastX:"+mLastX+"    mLastY:"+mLastY+"------------");

        boolean ontouchRet=super.onTouchEvent(event);
        Log.i("ccc","---ontouchRet:"+ontouchRet);
        return ontouchRet;

    }


    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，在其内部调用scrollTo或ScrollBy方法，完成滑动过程
        if (mScroller.computeScrollOffset()) {
            Log.i("ccc", "to x:" + mScroller.getCurrX() + "  to y:" + mScroller.getCurrY() + "----in:" + Thread.currentThread().getName());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }





}
