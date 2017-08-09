package test1.nh.com.demos1.activities.pull_load;

import android.content.Context;
import android.icu.text.BreakIterator;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 2017/6/15.
 */

public class LoadList extends RelativeLayout {


    /**
     * this class manages the top/bottom loading animation
     */
    public static abstract class LoadViewHolder{

        /**
         * loading view is controlled by pull/push amount
         *   controlled solely by {@link #updateProgress(float)}
         */
        public static final int LOAD_BY_PROGRESS=1;  //

        /**
         * loading view has nothing to do with pull/push amount
         *   but is controlled by   {@link #onDragToThreshold()}  {@link #onDragBeyondThreshold()} {@link #onRelease()}
         *
         */
        public static final int LOAD_FREE=2;         //
        private final int type;

        protected LoadViewHolder(int type) {
            this.type = type;
        }
        protected LoadViewHolder(){this.type=LOAD_FREE;}

        abstract int getLayoutId();

        abstract void initViewHolder(View holderView);//  like onCreateViewHolder in Adapter,   --> to find/assign fields

        abstract void onDragToThreshold();
        abstract void onDragBeyondThreshold();
        abstract void onRelease();

        abstract void updateProgress(float progress);
    }


    public void setLoadViewHolders(LoadViewHolder topVh,LoadViewHolder bottomVh){

        topViewHolder= topVh;
        bottomViewHolder= bottomVh;

        if (topViewHolder!=null){  //  add top loading animation/indicator
            View topLoader = LayoutInflater.from(getContext()).inflate(topViewHolder.getLayoutId(), this, false);
            topViewHolder.initViewHolder(topLoader);

            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Math.abs(TOP_LIMIT)*thresholdRatio));
            params.topMargin = (int) (-Math.abs(TOP_LIMIT)*thresholdRatio);
            topLoader.setLayoutParams(params);
            addView(topLoader);  // this is the top loading view

            View topBlankArea=new View(getContext());
            topBlankArea.setBackground(topLoader.getBackground());
            RelativeLayout.LayoutParams blankPramsTop=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(Math.abs(TOP_LIMIT)));
            blankPramsTop.topMargin = -Math.abs(TOP_LIMIT);
            topBlankArea.setLayoutParams(blankPramsTop);
            addView(topBlankArea); // this is the decoy view to make top area have homogeneous background
            topLoader.bringToFront();
        }

        if (bottomViewHolder!=null){
            View bottom = LayoutInflater.from(getContext()).inflate(bottomViewHolder.getLayoutId(), this, false);
            bottomViewHolder.initViewHolder(bottom);

            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Math.abs(BOTTOM_LIMIT)*thresholdRatio));
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.bottomMargin = (int) (-Math.abs(BOTTOM_LIMIT)*thresholdRatio);
            bottom.setLayoutParams(params);
            addView(bottom);  // this is the bottom loading view

            View bottomBlankArea=new View(getContext());
            bottomBlankArea.setBackground(bottom.getBackground());
            RelativeLayout.LayoutParams blankPramsBottom=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(Math.abs(BOTTOM_LIMIT)));
            blankPramsBottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            blankPramsBottom.bottomMargin = -Math.abs(BOTTOM_LIMIT);
            bottomBlankArea.setLayoutParams(blankPramsBottom);
            addView(bottomBlankArea); // this is the decoy view to make bottom area have homogeneous background
            bottom.bringToFront();
        }

    }


    LoadViewHolder topViewHolder;
    LoadViewHolder bottomViewHolder;



    private static final String TAG="LoadList";

    private RecyclerView recyclerView;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public LoadList(Context context) {
        this(context,null);
    }

    public LoadList(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadList(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }




    private void init(Context context){
        recyclerView=new RecyclerView(context);
        recyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(recyclerView);
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        returnScroller=new Scroller(context,new DecelerateInterpolator());
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if ((topViewHolder==null)||(bottomViewHolder==null)){
                    throw new RuntimeException("setLoadViewHolders()  is not called");
                }
                if(getViewTreeObserver().isAlive()){
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);    //
                    }
                }
            }
        });
    }






    Scroller returnScroller;


    //  thresholdRatio

    private void smoothReturn() {
        final int startY=getScrollY();
        float ratio=0;
        if (startY>0){  //  getScrollY > 0 , pull up to load more
            ratio=startY/(BOTTOM_LIMIT+0f);

        }  else { //  getScrollY < 0 , pull down to refresh
            ratio=startY/(TOP_LIMIT+0f);

        }

        int dy1=0;   //  stage1  scroll to threshold level
        int dy2=0;   //  stage2  scroll to origin
        int startY2=0;
        if (ratio<thresholdRatio){   //  action not triggered ...  scroll back to origin

            int targetY=0;
            int dy=targetY-startY;
            returnScroller.startScroll(0,startY,0,dy);
            invalidate();

        } else {   //  action triggered ... scroll back in 2 stages

            if (startY>0){  //  getScrollY > 0 , pull up to load more
                int targetY1= (int) (BOTTOM_LIMIT*thresholdRatio);   //  stage1  scroll to threshold level
                int targetY2=0;                                      //  stage2  scroll to origin
                dy1=targetY1-startY;
                dy2=targetY2-targetY1;
                startY2 = targetY1;
            }  else { //  getScrollY < 0 , pull down to refresh
                int targetY1= (int) (TOP_LIMIT*thresholdRatio);   //  stage1  scroll to threshold level
                int targetY2=0;                                   //  stage2  scroll to origin
                dy1=targetY1-startY;
                dy2=targetY2-targetY1;
                startY2 = targetY1;
            }

            returnScroller.startScroll(0,startY,0,dy1,return_stage1_duration);
            invalidate();
            final int finalDy = dy2,finalStart=startY2;
            postDelayed(new Runnable(){
                @Override
                public void run() {
                    returnScroller.startScroll(0,finalStart,0, finalDy);
                    invalidate();
                }
            },return_stage1_duration+stage1_wait_duration);

        }
    }


    @Override
    public void computeScroll() {
        if (returnScroller.computeScrollOffset()){
            int currentY=returnScroller.getCurrY();
            scrollTo(0,currentY);
            updateProgress();
            invalidate();
        } else {
          if (getScrollY()==0){
              resetState(STATE_FREE);
          }
        }
    }




    private static final int STATE_DRAG=11;   // being dragged ..
    private static final int STATE_RELEASE=12;// being released
    private static final int STATE_FREE=13;
    private String getStateStr(){
        switch(state){
            case STATE_DRAG:
                return "STATE_DRAG";
            case STATE_RELEASE:
                return "STATE_RELEASE";
            case STATE_FREE:
                return "STATE_FREE";
        }
        return "err";
    }
    private int state=STATE_FREE;
    private void resetState(int state){
        this.state=state;
        Log.i(TAG,"new state:"+getStateStr());
    }

    /**
     *  update the top/bottom loading view by progress....
     *  @return   progress, 0 if at original position , 1 if at {@link #BOTTOM_LIMIT}  or {@link #TOP_LIMIT}
     */
    private void updateProgress(){
        int currentY=getScrollY();
        if ( currentY < 0){
            if (topViewHolder!=null){
                float progress=(0f+currentY)/TOP_LIMIT;
                switch(state){
                    case STATE_DRAG:
                        if (progress<thresholdRatio){
                            topViewHolder.onDragToThreshold();
                        } else{
                            topViewHolder.onDragBeyondThreshold();
                        }
                        break;
                    case STATE_RELEASE:
                        topViewHolder.onRelease();
                        break;
                }
                topViewHolder.updateProgress(progress);
            }
        } else {
            if (bottomViewHolder!=null){
                float progress=(0f+currentY)/BOTTOM_LIMIT;
                switch(state){
                    case STATE_DRAG:
                        if (progress<thresholdRatio){
                            bottomViewHolder.onDragToThreshold();
                        } else{
                            bottomViewHolder.onDragBeyondThreshold();
                        }
                        break;
                    case STATE_RELEASE:
                        bottomViewHolder.onRelease();
                        break;
                }
                bottomViewHolder.updateProgress(progress);
            }

        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        viewHeight=getMeasuredHeight();
    }

    float lastInterceptX,lastInterceptY,lastX,lastY;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean intercept=false;
        if (state!=STATE_FREE) return intercept; //  next drag is only allowed after previous drag is finished
        int action = e.getActionMasked();
        float touchY=e.getY();
        float touchX=e.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY=touchY-lastInterceptY;
                float deltaX=touchX-lastInterceptX;
                float distanceY=-deltaY;
                boolean atTop=!recyclerView.canScrollVertically(-1);
                boolean atBottom=!recyclerView.canScrollVertically(1);
                if (Math.abs(deltaY)>Math.abs(deltaX)){  // this is a vertical scroll ...
                    if ( distanceY>0 ) {   // scroll down --  finger move up
                        if (atBottom){
                            lastInterceptY=touchY;
                            intercept=true;
                        }
                    }
                    if ( distanceY<0 ) {  // scroll up   --finger move down
                        if (atTop){
                            lastInterceptY=touchY;
                            intercept=true;
                        }
                    }
                } else {
                    intercept=false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        lastInterceptY=touchY;
        lastY=touchY;  //  ....   !!!!!
        lastInterceptX=touchX;
        lastX=touchX;  //  ....    ???
        return intercept;
    }

    private static final int TOP_LIMIT=-200;  // top over scroll region = 200px ,  if no  over scroll set to 0
    private static final int BOTTOM_LIMIT=200;  // bottom over scroll region = 200px ,  if no  over scroll set to 0
    float thresholdRatio=0.6f;
    private int return_stage1_duration=250,stage1_wait_duration=500;


    private int viewHeight;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        resetState(STATE_DRAG);
        updateProgress();
        float touchY=e.getY();
        float touchX=e.getX();
        int action = e.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY=touchY-lastY;
                float distanceY=-deltaY;
                int currentScroll=getScrollY();
                if ( distanceY<0 ) {   // reaching top limit
                    if ((currentScroll+distanceY) < TOP_LIMIT){
                        scrollTo(0,TOP_LIMIT);
                        return true;
                    }
                }  else {  // reaching bottom limit
                    if ((currentScroll+distanceY) > BOTTOM_LIMIT){
                        scrollTo(0,BOTTOM_LIMIT);
                        return true;
                    }
                }
                scrollBy(0, (int) distanceY);
                break;
            case MotionEvent.ACTION_UP:
                resetState(STATE_RELEASE);
                smoothReturn();     //   getScrollY > 0 , pull up to load more   ||||   getScrollY < 0 , pull down to refresh
                break;
        }
        lastY=touchY;
        lastX=touchX;  //  ....    ???
        return true;
    }







}
