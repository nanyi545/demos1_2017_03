package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.nanyi545.www.materialdemo.R;
import com.nanyi545.www.materialdemo.collapse_layout.CollapsHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 2017/2/28.
 *
 * this Class supports pull down  and up
 *
 */
public class CoordinatorPullToRefresh2 extends CoordinatorPullable implements GestureDetector.OnGestureListener {


    public CoordinatorPullToRefresh2(Context context) {
        this(context,null);
    }

    public CoordinatorPullToRefresh2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CoordinatorPullToRefresh2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }



    StoppableScrollView sv;

    public void setViews(StoppableScrollView sv) {
        this.sv = sv;
        manager=CollapsHolder.CollapsHolderManager.getInstance(this, R.id.collapse_holder2, R.id.collapse_holder1);

    }

    CollapsHolder.CollapsHolderManager manager;



    GestureDetectorCompat detector;

    private Scroller mScroller;

    // last position on touch screen
    private float mLastX=0;
    private float mLastY=0;

    private void initView(Context context){
        mScroller=new Scroller(context);
        detector=new GestureDetectorCompat(context,this);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
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




                if (scrollByStart >= 0) {   //    scroll down( scrollByStart>= 0 )

                    boolean svNotAtTop =  sv.computeVerticalScrollOffset() > 0;
                    if ( svNotAtTop ){   //   scroll view   not yet at top

                        sv.scrollBy(0, (int) -scrollByStart);  // scroll view gets the scroll   ....

                    } else {   // scroll view at top

                        boolean appBarFullyExpanded=manager.isFullyExpanded();
                        if (!appBarFullyExpanded){   // if app bar is not fully expanded

                            manager.collapse((int) -scrollByStart);  // app bar gets the scroll


                        } else {    //app bar is  fully expanded


                            if ( getScrollY() <= 0){    //   pullToRefresh gets the scroll -->    when scroll down( scrollByStart>= 0 ) is allowed  !!!
                                if(((-getScrollY()+(scrollByStart)) < getRevealHeight())   ){     //  when scroll down within  revealHeight ( scrollByStart < revealHeight )
                                    scrollBy(0, (int) -scrollByStart);
                                } else {
                                    scrollTo(0, -getRevealHeight());
                                }
                            }

                        }
                    }

                }





                if ( scrollByStart < 0){    //   scroll up


                    if( (getScrollY()-scrollByStart)<0 ){   //  pullToRefresh area is not yet fully collapsed ...
                        scrollBy(0, (int) -scrollByStart);  //  pullToRefresh area  gets the scroll
                    } else {
                        scrollTo(0, 0);   //  pullToRefresh area is fully collapsed ...


                        boolean appBarFullyCollapsed=manager.isFullyCollapsed();

                        if (!appBarFullyCollapsed){   // app bar is not yet fully collapsed ...
                            manager.collapse((int) -scrollByStart);  // app bar gets the scroll
                        } else {// app bar is  fully collapsed ...

                            sv.scrollBy(0, (int) -scrollByStart);   // scroll view gets the scroll

                        }


                    }
                }


                float progress= (-getScrollY()+0f) / getRevealHeight();
                getRevealContent().setProgress(progress);

                break;
            case MotionEvent.ACTION_UP:
                Log.i("ccc","--------action up-----------");



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
//        Log.i("ccc","super ontouchRet:"+ontouchRet);
        return true;

    }



    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }


    private int totalDrag=0;


    public void dragDown(int dy){
        if (mScroller.isFinished()){
            totalDrag=totalDrag+dy;
            if (totalDrag<0){  // totalDrag should be < 0
                if ((-totalDrag)<getRevealHeight()){
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
    }


    public void releaseDrag(){
        int dy =  - getScrollY();
        if(dy>0){
            mScroller.startScroll(0, getScrollY(), 0, dy,400);
            totalDrag=0;
            invalidate();
        }
    }

    public boolean shouldIntercept(){
        return getScrollY()<0;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i("ccc","fling:"+velocityY);  //  vY > 0,finger fling down       vY < 0, finger fling up


        boolean flingUp=velocityY<0;
        boolean flingDown=velocityY>0;
        boolean svAtTop=(sv.computeVerticalScrollOffset()<=20);

        if (flingUp){  // close app bar
            manager.smoothCollapseAll(true);
        }
        if (flingDown && svAtTop) {  // expand  app bar
            manager.smoothCollapseAll(false);
        }

        sv.fling((int) -velocityY);



        return true;
    }
}
