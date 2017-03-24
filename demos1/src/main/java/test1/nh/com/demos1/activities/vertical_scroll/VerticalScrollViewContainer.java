package test1.nh.com.demos1.activities.vertical_scroll;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/24.
 */

public class VerticalScrollViewContainer extends RelativeLayout implements GestureDetector.OnGestureListener {


    public VerticalScrollViewContainer(Context context) {
        this(context,null);
    }

    public VerticalScrollViewContainer(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VerticalScrollViewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context c){
        gestureDetectorCompat=new GestureDetectorCompat(c,this);
    }



    ArrayList<SingleScrollView> scrollViews;
    private void findScrollViews(){
        int count=getChildCount();
        scrollViews=new ArrayList();
        for (int i=0;i<count;i++){
            View v=getChildAt(i);
            if (v instanceof SingleScrollView){
                SingleScrollView cast=(SingleScrollView)v;
                scrollViews.add(cast);
            }
        }
    }

    private void layoutScrollViews(){
        if (scrollViews==null||scrollViews.size()==0) return;
        int count=scrollViews.size();
        int width=getMeasuredWidth();
        int height=getMeasuredHeight();
        for (int i=0;i<count;i++){
            scrollViews.get(i).layout(0,height*i,width,height*(i+1));
        }
    }

    private boolean scrollViewsFound=false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!scrollViewsFound){
            findScrollViews();
            scrollViewsFound=true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(scrollViewsFound){
            layoutScrollViews();
        }
    }


    int currentIndex=0;

    Scroller transitionScroller;




    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }






    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("ccc","--onTouchEvent--");
        boolean consumed=gestureDetectorCompat.onTouchEvent(event);
        return (super.onTouchEvent(event)||consumed);
    }




    GestureDetectorCompat gestureDetectorCompat;

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
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
        Log.i("ccc","-------onScroll---distanceY:"+distanceY);
        scrollBy(0, (int) distanceY);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


}
