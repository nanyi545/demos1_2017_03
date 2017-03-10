package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * RevealContent class holds the View that is shown during pull-down-to-refresh process
 * **/
public abstract class RevealContent extends RelativeLayout {

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
    abstract int getViewHeight();
    abstract void setUpInternal(View holder);


    public RevealContent init( Context context, CoordinatorPullable parent) {

        LayoutInflater mInflater = LayoutInflater.from(context);
        View revealContent  =  mInflater.inflate(getContentId(), null);
        int viewHeight= getViewHeight();
        Log.i("bbb","viewHeight:"+viewHeight);
        parent.setRevealHeight(viewHeight);
        LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, viewHeight);
        layoutParams.setMargins(0, -viewHeight ,0, 0);
        revealContent.setLayoutParams(layoutParams);
        parent.addView(revealContent);
        parent.setRevelContent(RevealContent.this);

        setUpInternal(revealContent);
        return null;
    }

}
