package com.nanyi545.www.hybridtest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by Administrator on 2017/5/5.
 */

public class CustomWebView extends WebView {


    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret=super.dispatchTouchEvent(ev);
        Log.i("ccc","CustomWebView  --> dispatchTouchEvent  -->  action_type:"+ev.getActionMasked()+"   return:"+ret);
        return ret;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean ret=super.onTouchEvent(event);
        Log.i("ccc","CustomWebView  --> ontouch onTouchEvent  -->  action_type:"+event.getActionMasked()+"   return:"+ret);
        return ret;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean ret=super.onInterceptTouchEvent(ev);
        Log.i("ccc","CustomWebView  --> onInterceptTouchEvent  -->  action_type:"+ev.getActionMasked()+"   intercept="+ret);
        return ret;
    }


}
