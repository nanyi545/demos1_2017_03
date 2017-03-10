package test1.nh.com.demos1.activities.horizontalScroll;

import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import org.mockito.asm.tree.analysis.Frame;

/**
 * Created by Administrator on 16-9-16.
 */
public class HorizontalScroller extends ViewGroup {

    private int mTouchSlop;

    private float mLastXIntercept=0;
    private float mLastYIntercept=0;


    // last position on touch screen
    private float mLastX=0;
    private float mLastY=0;

    private int leftBorder;
    private int rightBorder;

    public HorizontalScroller(Context context) {
        super(context);
        init(context);
    }

    public HorizontalScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizontalScroller(Context context, AttributeSet attrs, int defStyleAttr) {
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
                float scrollByStart = deltaX;
                if (getScrollX() - deltaX < leftBorder) {
                    // when try to scroll beyond start
//                    scrollByStart = getScrollX()-leftBorder;
                    scrollByStart = deltaX/3;

                } else if (getScrollX() + getWidth() - deltaX > rightBorder) {
                    // when try to scroll beyond right-end
                    scrollByStart = rightBorder-getWidth()-getScrollX();
//                    scrollByStart = deltaX/3;

                }
                scrollBy((int) -scrollByStart, 0);
                Log.i("ccc","onTouchEvent-ACTION_MOVE   xTouch:"+xTouch+"    ------  getScrollX:"+getScrollX() +"----- deltaX:"+deltaX );
                //    event.getX()  : position of the touch event ...
                //    getScrollX()  : total scroll of the view     left-->plus,  right-->minus
                //    deltaX  :  scroll left --> minus      scroll right --> plus

                break;
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                //-- full screen scroll --
//                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
//                int dx = targetIndex * getWidth() - getScrollX();

                //-- separate scroll
                int targetIndex = (getScrollX() +  itemWidth / 2) / itemWidth;
                //如果超过右边界，则回弹到最后一个View
                if (targetIndex>getChildCount()-2){
                    targetIndex = getChildCount()-2;
                    //如果超过左边界，则回弹到第一个View
                }else if (targetIndex<0){
                    targetIndex =0;
                }

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


    //  invalidate() method will call this ...// which is am empty function in the superclass
    @Override
    public void computeScroll() {
        Log.i("ccc","--on computeScroll---");
        // 第三步，重写computeScroll()方法，在其内部调用scrollTo或ScrollBy方法，完成滑动过程
        if (mScroller.computeScrollOffset()) {
            Log.i("ccc","to x:"+mScroller.getCurrX()+"  to y:"+mScroller.getCurrY()+"----in:"+Thread.currentThread().getName());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }


    public int measureDimension(int defaultSize, int measureSpec,String type) {
        int result=0;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);


        String specModeStr="";

        switch (specMode){
            case MeasureSpec.UNSPECIFIED:
                specModeStr="UNSPECIFIED";
                result = defaultSize;
                break;
            case MeasureSpec.AT_MOST:   //  -----> wrap_content   !!!!!
                specModeStr="AT_MOST";
                result = Math.min(defaultSize, specSize);
                break;
            case MeasureSpec.EXACTLY:   // ---->  1  specifying size    2  match_parent  !!!!!!
                specModeStr="EXACTLY";
                result=specSize;   // spec Size is   in unit px  !!!
                break;
        }

        Log.i("eee",type+"   specMode:"+specModeStr+"   specSize:"+specSize+"   measured size:"+result);   // spec Size in px
        return result;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 测量每一个子控件的大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);   //         protected void measureChild(View child, int parentWidthMeasureSpec,int parentHeightMeasureSpec)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureDimension(100,widthMeasureSpec,"scroller-width");
        measureDimension(100,heightMeasureSpec,"scroller-height");

    }


    int itemWidth;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("ccc","changed:"+changed+"    getChildCount():"+getChildCount());
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                itemWidth=childView.getMeasuredWidth();

                // 在水平方向上对子控件进行布局
//                childView.layout(i * getMeasuredWidth(), 0, i * getMeasuredWidth()+childView.getMeasuredWidth()+getPaddingLeft(), childView.getMeasuredHeight());
                //-------
                childView.layout(i * childView.getMeasuredWidth(), 0, (i+1) * childView.getMeasuredWidth(), childView.getMeasuredHeight());

//                * @param l Left position, relative to parent
//                * @param t Top position, relative to parent
//                * @param r Right position, relative to parent
//                * @param b Bottom position, relative to parent

            }
            // 初始化左右边界值
            leftBorder = 0;
//            rightBorder = getChildCount()*getMeasuredWidth();
            rightBorder = getChildCount()*itemWidth;
        }
    }
}
