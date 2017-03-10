package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 2017/2/28.
 *
 * this Class supports pull down  and up  ....   --> better understanding of collapsing toolbar layout is needed ....
 *
 */
public class CoordinatorPullToRefresh2 extends CoordinatorPullable {


    public CoordinatorPullToRefresh2(Context context) {
        super(context);
    }

    public CoordinatorPullToRefresh2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoordinatorPullToRefresh2(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public boolean onTouchEvent(MotionEvent event) {
        float xTouch = event.getX();
        float yTouch = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("ccc","--------action down-----------");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("ccc","--------action move-----------");

                float deltaX = xTouch-mLastX;
                float deltaY = yTouch-mLastY;
                float scrollByStart = deltaY;


                if (( getScrollY() <= 0)&& ( scrollByStart >= 0)){    //   when scroll down( scrollByStart>= 0 ) is allowed  !!!
                    if(((-getScrollY()+(scrollByStart)) < getRevealHeight())   ){     //  when scroll down within  revealHeight ( scrollByStart < revealHeight )
                        scrollBy(0, (int) -scrollByStart);
                    } else {
                        scrollTo(0, -getRevealHeight());
                    }
                }


                if ( scrollByStart < 0){    //   scroll up is allowed ....
                    if( (getScrollY()-scrollByStart)<0 ){   //  only when the total scroll is <0  (scroll up is allowed after scrolling down .... )
                        scrollBy(0, (int) -scrollByStart);
                    } else {
                        scrollTo(0, 0);
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



}
