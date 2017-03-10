package test1.nh.com.demos1.customView;

import android.content.Context;
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


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = View.resolveSize(getMinWidth(), widthMeasureSpec);
//        int widthSize=measureDimension(getMinWidth(),widthMeasureSpec,"width  ");

        //Get the height measurement
        int heightSize = View.resolveSize(getMinHeight(), heightMeasureSpec);

        //MUST call this to store the measurements
        setMeasuredDimension(widthSize, heightSize);

    }

    private int getMinHeight() {
        return 150;
    }

    private int getMinWidth() {
        return 150;
    }


    public int measureDimension(int defaultSize, int measureSpec,String type) {
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

