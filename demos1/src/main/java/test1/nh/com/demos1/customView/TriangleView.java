package test1.nh.com.demos1.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import test1.nh.com.demos1.R;


/**
 * Created by ww on 2016/7/1.
 */
public class TriangleView extends View{


    int viewWidth;
    int viewHeight;



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth=w;
        viewHeight=h;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewWidth=measureDimension(getMinWidth(),widthMeasureSpec);
        viewHeight=measureDimension(getMinHeight(),heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);
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
        switch (specMode){
            case MeasureSpec.UNSPECIFIED:
                result = defaultSize;
                break;
            case MeasureSpec.AT_MOST:   //  -----> wrap_content   !!!!!
                result = Math.min(defaultSize, specSize);
                break;
            case MeasureSpec.EXACTLY:   // ---->  1  specifying size    2  match_parent  !!!!!!
                result=specSize;   // spec Size is   in unit px  !!!
                break;
        }
        return result;
    }





    public TriangleView(Context context) {
        super(context);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrs(attrs);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttrs(attrs);
    }


    private void readAttrs(AttributeSet attrs){
        if (attrs!=null){
            TypedArray a = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.TriangleView
            );
            upsideDown = a.getBoolean(R.styleable.TriangleView_upsideDown,false);
            color=a.getColor(R.styleable.TriangleView_triangleColor,0xFFFAFAFA);
            a.recycle();
        }
    }


    private boolean upsideDown;
    private int color=0xFFFAFAFA;


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        Paint paint = new Paint();
        // 去锯齿
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(0xFFEEEEEE);
        paint.setColor(color);

        Path path1 = new Path();
        if (upsideDown){
            path1.moveTo(0, 0);
            path1.lineTo(viewWidth, 0);
            path1.lineTo(viewWidth/2, viewHeight);
        } else {
            path1.moveTo(0, viewHeight);
            path1.lineTo(viewWidth/2, 0);
            path1.lineTo(viewWidth, viewHeight);
        }

        path1.close();

        canvas.drawPath(path1, paint);

    }
}
