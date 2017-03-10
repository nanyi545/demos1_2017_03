package com.nanyi545.www.materialdemo.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 16-9-4.
 */
public class TestMeasureView extends View {


    public TestMeasureView(Context context) {
        super(context);
    }

    public TestMeasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestMeasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    // ...  measure is a final method ...

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        int widthSize = View.resolveSize(getMinWidth(), widthMeasureSpec);
        int widthSize=measureDimension(getMinWidth(),widthMeasureSpec);

        //Get the height measurement
//        int heightSize = View.resolveSize(getMinHeight(), heightMeasureSpec);
        int heightSize=measureDimension(getMinHeight(),heightMeasureSpec);

        Log.i("eee","   MeasureView onMeasure  widthSize:"+widthSize+"   heightSize:"+heightSize);   // spec Size in px

        //MUST call this to store the measurements
        setMeasuredDimension(widthSize, heightSize);

    }

    private int getMinHeight() {
        return 150;
    }

    private int getMinWidth() {
        return 150;
    }


    public int measureDimension(int defaultSize, int measureSpec) {
        int result=0;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);


        String specModeStr="";

        switch (specMode){
            case MeasureSpec.UNSPECIFIED:
                specModeStr="UNSPECIFIED";
                result = defaultSize;
                break;
            case MeasureSpec.AT_MOST:   //  -----> wrap_content   !!!!!
                specModeStr="AT_MOST";
                result = Math.min(defaultSize, specSize);
                break;
            case MeasureSpec.EXACTLY:   // ---->  1  specifying size    2  match_parent  !!!!!!
                specModeStr="EXACTLY";
                result=specSize;   // spec Size is   in unit px  !!!
                break;
        }

//        Log.i("eee","   specMode:"+specModeStr+"   specSize:"+specSize+"   measured size:"+result);   // spec Size in px
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.RED);
        Log.i("eee","   MeasureView  ondraw");   // spec Size in px
        super.onDraw(canvas);
    }


    @Override
    public void layout(int l, int t, int r, int b) {
        Log.i("eee","   MeasureView  layout l:"+l+" t:"+t+"   r:"+r+"  b:"+b);   // spec Size in px
        super.layout(l, t, r, b);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i("eee","   MeasureView  onlayout l:"+left+" t:"+top+"   r:"+right+"  b:"+bottom+"    changed:"+changed);   // spec Size in px
        super.onLayout(changed, left, top, right, bottom);
    }


}

