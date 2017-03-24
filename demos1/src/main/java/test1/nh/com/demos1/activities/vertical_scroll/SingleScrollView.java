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
            scrollView = (ScrollView) first;  // the scrollview should be place at the first ....
        } else {
            throw new RuntimeException("the first child of the SingleScrollView should be a ScrollView ");
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initAfterInflate();
    }

    public boolean isScrollToBottom() {
        boolean svContentSmallerThanSvHeight = (scrollView.getChildAt(0).getMeasuredHeight() - scrollView.getMeasuredHeight() < 0);
        boolean svContentLargerThanSvHeight_hit_bottom = (scrollView.getChildAt(0).getMeasuredHeight() - scrollView.getMeasuredHeight() == scrollView.getScrollY());
        return (svContentSmallerThanSvHeight || svContentLargerThanSvHeight_hit_bottom);
    }

    public boolean isScrollToTop() {
        return (scrollView.getScrollY() <= 0);
    }

}
