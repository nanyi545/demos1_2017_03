package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.nanyi545.www.materialdemo.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 2017/2/27.
 */
public class PullToRefresh extends RelativeLayout {


    /**
     * RevealContent class holds the View that is shown during pull-down-to-refresh process
     * **/
    public static abstract class RevealContent extends RelativeLayout {

        public RevealContent(Context context) {
            super(context);
        }

        public RevealContent(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public RevealContent(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        abstract void setProgress(float progress);
        abstract int getContentId(); /** get layout id to inflate **/
        abstract RevealContent init(Context context,PullToRefresh parent);

    }



    /**
     *  this is a default implementation of RevealContent that contains only a text view
     */
    public static class RevealContentImp extends RevealContent{

        TextView indicator;

        public RevealContentImp(Context context) {
            super(context);
        }

        public RevealContentImp(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public RevealContentImp(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        RevealContent init( Context context, PullToRefresh parent) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            View revealContent  =  mInflater.inflate(getContentId(), null);
            int viewHeight= (int) context.getResources().getDimension(R.dimen.reveal_height_imp1);
            Log.i("bbb","viewHeight:"+viewHeight);
            parent.setRevealHeight(viewHeight);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, viewHeight);
            layoutParams.setMargins(0, -viewHeight ,0, 0);
            revealContent.setLayoutParams(layoutParams);
            parent.addView(revealContent);
            parent.setRevelContent(RevealContentImp.this);
            indicator= (TextView) revealContent.findViewById(R.id.reveal_txt);
            return null;
        }

        @Override
        void setProgress(float progress) {
            indicator.setText(""+progress);
        }

        @Override
        int getContentId() {
            return R.layout.reveal_content_lo;
        }

    }





    public PullToRefresh(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public PullToRefresh(Context context) {
        this(context,null);
    }

    public PullToRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void setRevealContent(Class<? extends RevealContent> cls){
        try {
            Constructor c=cls.getConstructor(Context.class);
            cls.cast(c.newInstance(getContext())).init(getContext(),this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private int revealHeight =100;
    public void setRevealHeight(int revealHeight) {
        this.revealHeight = revealHeight;
    }


    private RevealContent revealContent;
    private void setRevelContent(RevealContent revealContent){
        this.revealContent=revealContent;
    }
    private RevealContent getRevealContent() {
        return revealContent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xTouch = event.getX();
        float yTouch = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("ccc","onTouchEvent-ACTION_DOWN");
                if (getScrollY()> 0) return true;  // only scroll down( getScrollY()<=0 ) is allowed  !!!
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = xTouch-mLastX;
                float deltaY = yTouch-mLastY;
                float scrollByStart = deltaY;

                if(((-getScrollY()+(scrollByStart)) < revealHeight) && ( getScrollY() <= 0)&& ( scrollByStart >= 0) ){  // only scroll down( scrollByStart>= 0 ) is allowed  !!!     only scroll down within  revealHeight ( scrollByStart < revealHeight )  is allowed
                    scrollBy(0, (int) -scrollByStart);
                }
                //    event.getX()  : position of the touch event ...
                //    getScrollX()  : total scroll of the view     left-->plus,  right-->minus
                //    deltaX  :  scroll left --> minus      scroll right --> plus

                float progress= (-getScrollY()+(scrollByStart)) / revealHeight;
                getRevealContent().setProgress(progress);

                break;
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                //-- full screen scroll --
//                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
//                int dx = targetIndex * getWidth() - getScrollX();

                int dy =  - getScrollY();

                //  ---scroll to with out animation
//                scrollTo(getScrollX()+dx,0);
                // ---use scroller, scroll with animation ---init scroller ...
                mScroller.startScroll(0, getScrollY(), 0, dy,400);
                invalidate();

                Log.i("ccc","onTouchEvent-ACTION_UP");
                Log.i("bbb","scrollY:"+getScrollY()+"  revealHeight:"+revealHeight);
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
        Log.i("ccc","--on computeScroll---");
        // 第三步，重写computeScroll()方法，在其内部调用scrollTo或ScrollBy方法，完成滑动过程
        if (mScroller.computeScrollOffset()) {
            Log.i("ccc","to x:"+mScroller.getCurrX()+"  to y:"+mScroller.getCurrY()+"----in:"+Thread.currentThread().getName());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }




}
