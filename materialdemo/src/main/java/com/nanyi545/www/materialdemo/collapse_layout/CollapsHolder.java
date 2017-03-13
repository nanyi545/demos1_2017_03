package com.nanyi545.www.materialdemo.collapse_layout;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */
public class CollapsHolder extends RelativeLayout implements GestureDetector.OnGestureListener {


    public static final String TAG = CollapsHolder.class.getName();


    public CollapsHolder(Context context) {
        this(context, null);
    }

    public CollapsHolder(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public int getAppearantHeight(){
        return initialHeight+currentShift;
    }

    private void init() {

        Log.i("ccc","collapseHolder--constructor");
        detector = new GestureDetectorCompat(getContext(), this);

        ViewTreeObserver viewTreeObserver = CollapsHolder.this.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (initialHeight==0){
                        initialHeight = getMeasuredHeight();  // get initial height (  it is the first time the view is instanciated...  onRestoreInstanceState not called ... )
                    } else {
                        //   do nothing ...  the initialHeight is obtained from onRestoreInstanceState ...
                    }


                    if (CollapsHolder.this.getViewTreeObserver().isAlive()) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            CollapsHolder.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            CollapsHolder.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }

                }
            });
        }

        collapseController = new Scroller(getContext());

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }



    /**
     * currentShift<=0
     * currentShift<0    , collapsed  ( currentShift  = (-initialHeight)  , 100% collapsed )
     * currentShift=0    , not changed ...
     * <p>
     * currentShift should be no smaller than -initialHeight
     */
    private int currentShift = 0;
    private static final String CURRENT_SHIFT_KEY = "CURRENT_SHIFT_KEY";

    int initialHeight;
    private static final String INIT_HEIGHT_KEY = "INIT_HEIGHT_KEY";

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.i("ccc","onSaveInstanceState  ---  currentShift:"+this.currentShift+"    initialHeight:"+initialHeight );
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt(CURRENT_SHIFT_KEY, this.currentShift); // ... save stuff
        bundle.putInt(INIT_HEIGHT_KEY, this.initialHeight); // ... save stuff

        return bundle;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) // implicit null check
        {
            Bundle bundle = (Bundle) state;

            this.initialHeight = bundle.getInt(INIT_HEIGHT_KEY);
            int previousShift = bundle.getInt(CURRENT_SHIFT_KEY); // ... load stuff
            collapse(Math.abs(previousShift));

            Log.i("ccc","onRestoreInstanceState  ---  currentShift:"+this.currentShift+"    initialHeight:"+initialHeight );

            Parcelable superState = bundle.getParcelable("superState");
            super.onRestoreInstanceState(superState);
        }

    }

    /**
     * scroll up   --> to collapse  deltaY>0
     * scroll down --> to expand    deltaY<0
     *
     * @param deltaY
     * @return 0 if the input deltaY is fully converted to view expansion/collapsing without touching either the expansion/collapsing limit.
     * >0  if the input deltaY cause current view to reach collapse limit, the overflow amount ( positive deltaY )  is returned
     * <0  if the input deltaY cause current view to reach expand limit, the overflow amount ( negative deltaY )  is returned
     */
    public int collapse(float deltaY) {
        Log.i("ccc"," currentShift:"+currentShift+"   initialHeight:"+initialHeight);
        if (deltaY > 0) {   // scroll up --> to collapse
            if ((currentShift - deltaY) <= -initialHeight) {   // make sure the total CollapsHolder height is positive, which is the limit in collapsing
                int deltaY_new = initialHeight + currentShift;
                doCollapse(deltaY_new);
                return (int) (deltaY - deltaY_new);
            }
        }
        if (deltaY < 0) {  // scroll down --> to expand
            if ((currentShift - deltaY) >= 0) {  //  make sure height will not expand beyond the initial height, which is the limit in expanding
                int deltaY_new = currentShift;
                doCollapse(deltaY_new);
                return (int) (deltaY - deltaY_new);
            }
        }
        doCollapse(deltaY);
        return 0;
    }

    public void collapseAll() {
        int deltaY = initialHeight + currentShift;
        collapse(deltaY);
    }


    Scroller collapseController;

    /**
     * @param collapse true --> collapse         false --> expand
     */
    public void collapseAllSmooth(boolean collapse) {
        int startShift = currentShift;
        int endShiftFullCollapse = -initialHeight - currentShift;
        int endShiftFullExpand = -currentShift;
        collapseController.forceFinished(true);
        if (collapse) {
            collapseController.startScroll(0, startShift, 0, endShiftFullCollapse, 300);  // collapse all
        } else {
            collapseController.startScroll(0, startShift, 0, endShiftFullExpand, 300);  // expand all
        }

        smoothCollapseHandler.sendEmptyMessage(COLLAPSE_ALL);
    }


    private static int COLLAPSE_ALL = 11;

    private Handler smoothCollapseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == COLLAPSE_ALL) {

                if (collapseController.computeScrollOffset()) {
                    int deltaY = currentShift - collapseController.getCurrY();
                    collapse(deltaY);
                    smoothCollapseHandler.sendEmptyMessage(COLLAPSE_ALL);
                }


            }


        }
    };


    private void doCollapse(float deltaY) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                params.topMargin -= deltaY;
                child.setLayoutParams(params);
            }
        }
        currentShift -= deltaY;
        setAlpha(getExpandRatio());
    }


    /**
     * @return get collapsable height in px
     */
    public int getScrollRegion() {
        return initialHeight + currentShift;
    }


    /**
     * @return 100% : fully expanded ,   0% : fully collapsed
     */
    public float getExpandRatio() {
        float ratio = 1 - ((currentShift + 0f) / -initialHeight);
        return ratio;
    }

    public boolean isFullyExpanded() {
        return currentShift == 0;
    }

    public boolean isFullyCollapsed() {
        return -currentShift == initialHeight;
    }


    /**
     * test methods ...
     */
    public void expand() {
        collapse(-1);
    }

    public void collapse() {
        collapse(1);
    }


    GestureDetectorCompat detector;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
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
        collapse(distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    public static class CollapsHolderManager {

        private CollapsHolderManager() {
        }

        private int currentActiveHolderIndex;

        /**
         * update the currentActiveHolderIndex based on the return of of {@link CollapsHolder#collapse(float deltaY)}
         *
         * @param overFlowAmount see return of {@link CollapsHolder#collapse(float deltaY)}
         */
        private void switchCurrentActiveHolder(int overFlowAmount) {
            if (overFlowAmount == 0) return;
            if (overFlowAmount > 0) {
                if ((currentActiveHolderIndex + 1) <= (getHolders().size() - 1)) {
                    currentActiveHolderIndex += 1;
                }
                return;
            }
            if (overFlowAmount < 0) {
                if (currentActiveHolderIndex - 1 >= 0) {
                    currentActiveHolderIndex -= 1;
                }
                return;
            }

        }


        List<CollapsHolder> holders;

        private List<CollapsHolder> getHolders() {
            return holders;
        }

        private void setHolders(List<CollapsHolder> holders) {
            this.holders = holders;
        }


        /**
         * @param parent
         * @param ids    make sure the ids are added from the last (  lowest in Y position on screen )
         * @return
         */
        public static CollapsHolderManager getInstance(View parent, int... ids) {
            CollapsHolderManager manager = new CollapsHolderManager();
            List<CollapsHolder> holders = new ArrayList();
            for (int id : ids) {
                holders.add((CollapsHolder) parent.findViewById(id));
            }
            manager.setHolders(holders);
            manager.currentActiveHolderIndex = 0;  // by default, the first in the list  corresponds to the lowest in Y position on screen
            return manager;
        }


        /**
         * to collapse  the list of CollapsHolders
         * <p>
         * scroll up   --> to collapse  deltaY>0
         * scroll down --> to expand    deltaY<0
         */
        public void collapse(int deltaY) {
            int overFlow = getHolders().get(currentActiveHolderIndex).collapse(deltaY);
            int loopCount = 0;
            while ((overFlow != 0) && (loopCount <= 3)) {     //  assume  loopCount  > 3 means reaching the collaps/expansion limit for this group of  CollapsHolders ....
                loopCount += 1;
                switchCurrentActiveHolder(overFlow);
                overFlow = getHolders().get(currentActiveHolderIndex).collapse(overFlow);
            }
        }


        /**
         * @param collapse true --> collapse         false --> expand
         */
        public void smoothCollapseAll(boolean collapse) {
            for (CollapsHolder holder : getHolders()) {
                holder.collapseAllSmooth(collapse);
            }
            if (collapse) {
                currentActiveHolderIndex = getHolders().size() - 1;
            } else {
                currentActiveHolderIndex = 0;
            }

        }


        public boolean isFullyExpanded() {
            boolean currentActivatedAtBottom = (currentActiveHolderIndex == 0);
            if (currentActivatedAtBottom) {
                return getHolders().get(currentActiveHolderIndex).isFullyExpanded();
            } else {
                return false;
            }

        }


        public boolean isFullyCollapsed() {
            boolean currentActivatedAtTop = (currentActiveHolderIndex == getHolders().size() - 1);
            if (currentActivatedAtTop) {
                return getHolders().get(currentActiveHolderIndex).isFullyCollapsed();
            } else {
                return false;
            }
        }


    }


}
