package test1.nh.com.demos1.activities.vertical_scroll;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/24.
 */

public class VerticalScrollViewContainer extends RelativeLayout  {


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
        mVelocityTracker = VelocityTracker.obtain();
        transitionScroller=new Scroller(c,new DecelerateInterpolator());
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
        Log.i("ccc","container-----onMeasure");

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(scrollViewsFound){
            viewHeight=getMeasuredHeight();
            layoutScrollViews();
        }
        Log.i("ccc","container-----onLayout");
    }


    private int currentIndex=0;
    private int viewHeight;

    Scroller transitionScroller;

    /**
     * called at  ACTION_UP  and  fling velocity is smaller than the threshold  --> "smooth scroll back"
     */
    private void smoothScrollToFixedPositionsWhenNoFling(){
        Log.i("ccc","-----call smoothScrollToFixedPositionsWhenNoFling---currentIndex:"+currentIndex+"    viewHeight:"+viewHeight);
        int targetIndex=(getScrollY()+viewHeight/2)/viewHeight;
        if (targetIndex<0){
            targetIndex=0;
        }
        if (targetIndex>scrollViews.size()-1){
            targetIndex=scrollViews.size()-1;
        }
        currentIndex=targetIndex;
        int targetY=targetIndex*viewHeight;
        int startY=getScrollY();
        int dy=targetY-startY;

        Log.i("ccc","-----call smoothScrollToFixedPositionsWhenNoFling---currentIndex:"+currentIndex+"     startY:"+startY+"     targetY:"+targetY+"   dy:"+dy);
        transitionScroller.startScroll(0,startY,0,dy);
        invalidate();
    }

    /**
     * called at  ACTION_UP  and  fling velocity is larger than the threshold  --> "smooth  scroll to the targeted index"
     * @param changeIndex  this is change in {@link this.currentIndex}, normally -1 or +1
     */
    private void smoothScrollToTargetIndex_UponFling(int changeIndex){
        Log.i("ccc","-----call smoothScrollToTargetIndex_UponFling---changeIndex:"+changeIndex+"   currentIndex:"+currentIndex+"    viewHeight:"+viewHeight);
        int targetIndex=currentIndex+changeIndex;
        if (targetIndex<0){
            targetIndex=0;
        }
        if (targetIndex>scrollViews.size()-1){
            targetIndex=scrollViews.size()-1;
        }
        currentIndex=targetIndex;
        int targetY=targetIndex*viewHeight;
        int startY=getScrollY();
        int dy=targetY-startY;
        Log.i("ccc","-----call smoothScrollToTargetIndex_UponFling---changeIndex:"+changeIndex+"   currentIndex:"+currentIndex+"    targetY:"+targetY+"    startY:"+startY+"   dy:"+dy);
        transitionScroller.startScroll(0,startY,0,dy);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (transitionScroller.computeScrollOffset()){
            int currentY=transitionScroller.getCurrY();
            Log.i("ccc","currentY:"+currentY);
            scrollTo(0,currentY);
            postInvalidate();
        }
    }




    private static final int TOP_LIMIT=-200;  // top over scroll region = 200px ,  if no  over scroll set to 0
    private static final int BOTTOM_LIMIT=200;  // bottom over scroll region = 200px ,  if no  over scroll set to 0

    float lastInterceptY;
    float lastInterceptX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        SingleScrollView currentScrollView=scrollViews.get(currentIndex);
        float touchY=event.getY();
        float touchX=event.getX();

        int action = event.getActionMasked();
        boolean intercept=false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i("ccc","onInterceptTouchEvent:ACTION_DOWN: currentScrollView.isScrollToTop():"+currentScrollView.isScrollToTop()+"    currentScrollView.isScrollToBottom():"+currentScrollView.isScrollToBottom()+"   intercept:"+intercept);
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY=touchY-lastInterceptY;
                float distanceY=-deltaY;
                Log.i("ccc","onInterceptTouchEvent:ACTION_MOVE distanceY:"+distanceY+" currentScrollView.isScrollToTop():"+currentScrollView.isScrollToTop()+"    currentScrollView.isScrollToBottom():"+currentScrollView.isScrollToBottom()+"   intercept:"+intercept);

                float deltaX=touchX-lastInterceptX;

                if (Math.abs(deltaY)>Math.abs(deltaX)){  // this is a vertical scroll ...

                    if ( distanceY<0 ) {   // scroll down --  finger down
                        if (currentScrollView.isScrollToTop()){
                            lastInterceptY=touchY;
                            intercept=true;
                        }
                    }

                    if ( distanceY>0 ) {  // scroll up   --finger up
                        if (currentScrollView.isScrollToBottom()){
                            lastInterceptY=touchY;
                            intercept=true;
                        }
                    }

                } else {
                    intercept=false;
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.i("ccc","onInterceptTouchEvent:ACTION_UP: currentScrollView.isScrollToTop():"+currentScrollView.isScrollToTop()+"    currentScrollView.isScrollToBottom():"+currentScrollView.isScrollToBottom()+"   intercept:"+intercept);
                intercept = false;
                break;
        }

        lastInterceptY=touchY;
        lastY=touchY;  //  ....   !!!!!

        lastInterceptX=touchX;
        lastX=touchX;  //  ....    ???

        return intercept;
    }



    private VelocityTracker mVelocityTracker;



    float lastY;
    float lastX;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("ccc","onTouchEvent:  currentIndex:"+currentIndex+"   type:"+event.getActionMasked());

        float touchY=event.getY();
        float touchX=event.getX();

        mVelocityTracker.addMovement(event);

        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY=touchY-lastY;

                float distanceY=-deltaY;

                int currentScroll=getScrollY();
                if ( distanceY<0 ) {   // reaching top limit
                    if ((currentScroll+distanceY)<TOP_LIMIT){
                        scrollTo(0,TOP_LIMIT);
                        return true;
                    }
                }  else {  // reaching bottom limit
                    int viewBottom=BOTTOM_LIMIT+viewHeight*(scrollViews.size()-1);
                    if ((currentScroll+distanceY)> viewBottom){
                        scrollTo(0,viewBottom);
                        return true;
                    }
                }
                scrollBy(0, (int) distanceY);
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityUponRelease=  mVelocityTracker.getYVelocity();
                if (Math.abs(velocityUponRelease)<500){
                    smoothScrollToFixedPositionsWhenNoFling();
                } else {
                    if (velocityUponRelease<0){  // fling up
                        smoothScrollToTargetIndex_UponFling(1);
                    } else {
                        smoothScrollToTargetIndex_UponFling(-1);
                    }
                }
                mVelocityTracker.clear();
                break;
        }

        lastY=touchY;
        lastX=touchX;  //  ....    ???

        return true;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVelocityTracker.recycle();
    }
}
