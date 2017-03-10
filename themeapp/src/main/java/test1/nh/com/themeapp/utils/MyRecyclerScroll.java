package test1.nh.com.themeapp.utils;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 1511-14.
 */
public abstract class MyRecyclerScroll extends RecyclerView.OnScrollListener {

    int scrollDist = 0;
    boolean isVisible = true;
    static final float MINIMUM = 25;

//    dy,  positive value on scrolling down, and negative value scrolling up.
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (isVisible && scrollDist > MINIMUM) {  // originally visible + scroll down
            hide();
            scrollDist = 0;
            isVisible = false;
        }
        else if (!isVisible && scrollDist < -MINIMUM) {// originally invisible + scroll up
            show();
            scrollDist = 0;
            isVisible = true;
        }
        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy;
        }
    }

    public abstract void show();
    public abstract void hide();

}

