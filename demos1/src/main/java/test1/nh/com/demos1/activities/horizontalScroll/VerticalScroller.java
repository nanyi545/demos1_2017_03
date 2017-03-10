package test1.nh.com.demos1.activities.horizontalScroll;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Administrator on 16-9-16.
 */
public class VerticalScroller extends ViewGroup {


    private int mTouchSlop;

    private float mLastXIntercept=0;
    private float mLastYIntercept=0;


    // last position on touch screen
    private float mLastX=0;
    private float mLastY=0;

    private int topBorder;
    private int buttomBorder;

    public VerticalScroller(Context context) {
        super(context);
        init(context);
    }

    public VerticalScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerticalScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        mScroller = new Scroller(context);
    }

    private Scroller mScroller;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        float xIntercept = ev.getX();
        float yIntercept = ev.getY();
        Log.i("ccc","-----------------onInterceptTouchEvent-----------------");

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = xIntercept-mLastXIntercept;
                float deltaY = yIntercept-mLastYIntercept;
                // 当水平方向的滑动距离大于竖直方向的滑动距离，且手指拖动值大于TouchSlop值时，拦截事件  // what's the purpose???
                if (Math.abs(deltaX)>Math.abs(deltaY) && Math.abs(deltaX)>mTouchSlop) {
                    intercept=true;
                }else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            default:
                break;
        }

        mLastX = xIntercept;
        mLastY = yIntercept;
        mLastXIntercept = xIntercept;
        mLastYIntercept = yIntercept;

        Log.i("ccc","---------------------------intercept--------------------------"+intercept);
        intercept=true;
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xTouch = event.getX();
        float yTouch = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("ccc","onTouchEvent-ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = xTouch-mLastX;
                float deltaY = yTouch-mLastY;
                float scrollByStart = deltaY;
                if (getScrollY() - deltaY < topBorder) {
                    // when try to scroll beyond start
                    scrollByStart = getScrollY()-topBorder;
                } else if (getScrollY() + getHeight() - deltaY > buttomBorder) {
                    // when try to scroll beyond right-end
                    scrollByStart = buttomBorder-getHeight()-getScrollY();
                }
                scrollBy(0,(int) -scrollByStart);
                Log.i("ccc","onTouchEvent-ACTION_MOVE   xTouch:"+xTouch+"    ------  getScrollX:"+getScrollX() );
                //    event.getX()  : position of the touch event ...
                //    getScrollX()  : total scroll of the view

                break;
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                //-- full screen scroll --
//                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
//                int dx = targetIndex * getWidth() - getScrollX();

                //-- separate scroll
                int targetIndex = (getScrollY() +  itemHeight / 2) / itemHeight;
                int dY = targetIndex * itemHeight - getScrollY();


                //  ---scroll to with out animation
//                scrollTo(getScrollX()+dx,0);
                // ---use scroller, scroll with animation ---init scroller ...
                mScroller.startScroll(0, getScrollY(), 0, dY);
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


    //  invalidate() method will call this ...// which is am empty function in the superclass
    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，在其内部调用scrollTo或ScrollBy方法，完成滑动过程
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
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
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    int itemHeight;


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("ccc","changed:"+changed+"    getChildCount():"+getChildCount());
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // 在vertical方向上对子控件进行布局
                childView.layout(0, i * childView.getMeasuredHeight(), getMeasuredWidth(), (i+1) * childView.getMeasuredHeight());
//                Log.i("ccc","i th child:"+i+"    getMeasuredWidth():"+childView.getMeasuredWidth());
                itemHeight=childView.getMeasuredHeight();
            }
            // 初始化左右边界值
            topBorder = 0;
            buttomBorder = getChildCount()*itemHeight;
        }
    }
}
