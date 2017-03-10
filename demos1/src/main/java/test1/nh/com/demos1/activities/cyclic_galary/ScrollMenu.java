package test1.nh.com.demos1.activities.cyclic_galary;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

/**
 * Created by Administrator on 2016/11/8.
 */
public class ScrollMenu extends ViewGroup {







    int widthPixels,itemWidth,heightPixels;

    public ScrollMenu(Context context) {
        super(context);
        init();
    }

    public ScrollMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    float mDensity;

    private void init(){
        ScrollMenu.this.setVisibility(View.INVISIBLE);
        mScroller = new Scroller(getContext());
        initScroller = new Scroller(getContext(),new OvershootInterpolator());
        Activity host= (Activity) getContext();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        host.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        widthPixels = displaymetrics.widthPixels;
        itemWidth= widthPixels/5;
        heightPixels=itemWidth;
        mDensity=displaymetrics.density;

        Log.i("ddd","widthPixels:"+widthPixels+"   heightPixels:"+heightPixels+"  "+displaymetrics.density);

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

    }




    // last position on touch screen
    private float mLastX=0;
    private float mLastY=0;


    private Scroller mScroller;
    private Scroller initScroller;


    private int mTouchSlop;




    private int initOffsetInDp=300;

    public void scrollIn(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ScrollMenu.this.setVisibility(View.VISIBLE);
                initScroller.startScroll(-(int) (initOffsetInDp*mDensity), 0, (int) (initOffsetInDp*mDensity), 0,1200);
                invalidate();
            }
        },700);
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xTouch = event.getX();
        float yTouch = event.getY();
        Log.i("ddd","onTouchEvent  action: "+event.getAction());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                Log.i("ddd","onTouchEvent-ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i("ddd","MotionEvent.ACTION_MOVE    targetIndex:");

                float deltaX = xTouch-mLastX;
                float deltaY = yTouch-mLastY;

                if (Math.abs(deltaY)>0 ) {
                    getParent().requestDisallowInterceptTouchEvent(true);   // this makes sure the up/down scroll in ScrollMenu does not affect the scrollview
                }else {

                }


                float scrollByStart = deltaX;
                if (getScrollX() - deltaX < leftBorder) {
                    // when try to scroll beyond start
//                    scrollByStart = getScrollX()-leftBorder;
                    scrollByStart = deltaX/3;

                } else if (getScrollX() + getWidth() - deltaX > rightBorder) {
                    // when try to scroll beyond right-end
//                    scrollByStart = rightBorder-getWidth()-getScrollX();
                    scrollByStart = deltaX/3;

                }
                scrollBy((int) -scrollByStart, 0);
//                Log.i("ddd","onTouchEvent-ACTION_MOVE   xTouch:"+xTouch+"    ------  getScrollX:"+getScrollX() +"----- deltaX:"+deltaX );
                //    event.getX()  : position of the touch event ...
                //    getScrollX()  : total scroll of the view     left-->plus,  right-->minus
                //    deltaX  :  scroll left --> minus      scroll right --> plus

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                //-- full screen scroll --
//                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
//                int dx = targetIndex * getWidth() - getScrollX();

                getParent().requestDisallowInterceptTouchEvent(false);
                Log.i("BBB","-------");


                //-- separate scroll
                int targetIndex = (getScrollX() +  itemWidth / 2) / itemWidth;
                //如果超过右边界，则回弹到最后一个View
                if (targetIndex>getChildCount()-5){
                    targetIndex = getChildCount()-5;
                    //如果超过左边界，则回弹到第一个View
                }else if (targetIndex<0){
                    targetIndex =0;
                }

//                Log.i("ddd","MotionEvent.ACTION_UP    targetIndex:"+targetIndex);
                int dx = targetIndex * itemWidth - getScrollX();


                //  ---scroll to with out animation
//                scrollTo(getScrollX()+dx,0);
                // ---use scroller, scroll with animation ---init scroller ...
                mScroller.startScroll(getScrollX(), 0, dx, 0,400);
                invalidate();

                Log.i("ccc","onTouchEvent-ACTION_UP");
                break;
            default:
                break;
        }

        mLastX = xTouch;  // this gets updated in MotionEvent.ACTION_DOWN
        mLastY = yTouch;
        Log.i("ccc","---------mLastX:"+mLastX+"    mLastY:"+mLastY+"------------");

//        return super.onTouchEvent(event);
        return true;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.i("ccc","to x:"+mScroller.getCurrX()+"  to y:"+mScroller.getCurrY()+"----in:"+Thread.currentThread().getName());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
        if (initScroller.computeScrollOffset()) {
            scrollTo(initScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 测量每一个子控件的大小
             measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            if (childView instanceof  ScrollMenuItem ) {
                ((ScrollMenuItem) childView).resize(itemWidth,itemWidth);
            }

        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //Get the width measurement
        int widthSize = View.resolveSize(widthPixels, widthMeasureSpec);
        //Get the height measurement
        int heightSize = View.resolveSize(heightPixels, heightMeasureSpec);

        //MUST call this to store the measurements
        setMeasuredDimension(widthSize, heightSize);

    }



    int leftBorder,rightBorder;


    @Override
    protected void onLayout(boolean changed, int ii1, int i1, int i2, int i3) {

        Log.i("ccc","changed:"+changed+"    getChildCount():"+getChildCount());
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                childView.layout(i * itemWidth, 0, (i+1) * itemWidth, heightPixels);
            }
            // 初始化左右边界值
            leftBorder = 0;
            rightBorder = getChildCount()*itemWidth;

        }



    }



}
