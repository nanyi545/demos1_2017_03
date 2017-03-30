package test1.nh.com.demos1.activities.vertical_scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/3/24.
 */
public class SingleScrollView extends RelativeLayout {

    public SingleScrollView(Context context) {
        this(context, null);
    }

    public SingleScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    ScrollView scrollView;

    private void initAfterInflate() {
        View first = getChildAt(0);
        if (first instanceof ScrollView) {
            type=SINGLE_SCROLL_VIEW_COMMON;
            scrollView = (ScrollView) first;  // the scrollview should be place at the first ....
        } else if(first instanceof Scrollable){
            type=SINGLE_SCROLL_VIEW_WITH_HORIZENTOAL_SUPPORT;
            scrollableChild= (Scrollable) first;
        } else {
            throw new RuntimeException("the first child of the SingleScrollView should be a ScrollView or a Scrollable ");
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initAfterInflate();
    }

    public boolean isScrollToBottom() {
        if (type==SINGLE_SCROLL_VIEW_COMMON){
            boolean svContentSmallerThanSvHeight = (scrollView.getChildAt(0).getMeasuredHeight() - scrollView.getMeasuredHeight() < 0);
            boolean svContentLargerThanSvHeight_hit_bottom = (scrollView.getChildAt(0).getMeasuredHeight() - scrollView.getMeasuredHeight() == scrollView.getScrollY());
            return (svContentSmallerThanSvHeight || svContentLargerThanSvHeight_hit_bottom);
        } else if(type==SINGLE_SCROLL_VIEW_WITH_HORIZENTOAL_SUPPORT){
            return scrollableChild.isScrollToBottom();
        }
        return false;
    }


    public boolean isScrollToTop() {
        if (type==SINGLE_SCROLL_VIEW_COMMON){
            return (scrollView.getScrollY() <= 0);
        } else if(type==SINGLE_SCROLL_VIEW_WITH_HORIZENTOAL_SUPPORT){
            return scrollableChild.isScrollToTop();
        }
        return false;
    }



    private int type=SINGLE_SCROLL_VIEW_COMMON;  // default type only vertical scrolling is needed ...


    public static final int SINGLE_SCROLL_VIEW_COMMON=21;
    public static final int SINGLE_SCROLL_VIEW_WITH_HORIZENTOAL_SUPPORT=22;



    Scrollable scrollableChild;

    public interface Scrollable{
        boolean isScrollToBottom();
        boolean isScrollToTop();
    }


}
