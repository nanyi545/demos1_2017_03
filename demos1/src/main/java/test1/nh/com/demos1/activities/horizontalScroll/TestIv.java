package test1.nh.com.demos1.activities.horizontalScroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/12/27.
 */
public class TestIv extends ImageView {


    public TestIv(Context context) {
        super(context);
    }

    public TestIv(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestIv(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        measureDimension(100,widthMeasureSpec,"width");
        measureDimension(100,heightMeasureSpec,"height");

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }



    public int measureDimension(int defaultSize, int measureSpec, String type) {
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

        Log.i("eee",type+"   specMode:"+specModeStr+"   specSize:"+specSize+"   measured size:"+result);   // spec Size in px
        return result;
    }



}

