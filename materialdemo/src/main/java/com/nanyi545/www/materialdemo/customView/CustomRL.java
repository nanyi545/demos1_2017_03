package com.nanyi545.www.materialdemo.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/3/6.
 */

public class CustomRL extends RelativeLayout {


    public CustomRL(Context context) {
        super(context);
    }

    public CustomRL(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRL(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i("eee","   RelativeLayout  onMeasure  widthSize:"+getMeasuredWidth()+"   heightSize:"+getMeasuredHeight());   // spec Size in px

    }



    // layout method is final ...

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.i("eee","   RelativeLayout  onLayout l:"+l+" t:"+t+"   r:"+r+"  b:"+b);   // spec Size in px

    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }



}
