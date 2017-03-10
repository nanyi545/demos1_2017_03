package test1.nh.com.demos1.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 16-9-3.
 */
public class CustomView extends View {

    private float mBorderWidth;
    private int mBorderColor;

    private Paint mPaint;

    private RectF mBounds;
    private float width;
    private float height;
    float radius;
    float smallLength;
    float largeLength;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("ccc","constructor ");

        TypedArray typedArray = context.getTheme()
                .obtainStyledAttributes(
                        attrs,
                        R.styleable.CustomView
                        , 0, 0);

        try{
            mBorderColor = typedArray.getColor(R.styleable.CustomView_border_color1,0xff000000);
            mBorderWidth = typedArray.getDimension(R.styleable.CustomView_border_width1,2);
        }finally {
            typedArray.recycle();
        }

        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("ccc","onsize changed");

        mBounds = new RectF(getLeft(),getTop(),getRight(),getBottom());

        width = mBounds.right - mBounds.left;
        height = mBounds.bottom - mBounds.top;
//        Log.i("ccc","mBounds.right:"+mBounds.right+"   mBounds.left:"+mBounds.left+"  mBounds.bottom:"+mBounds.bottom+"   mBounds.top:"+mBounds.top);

        if(width<height){
            radius = width/2.3f;
        }else{
            radius = height/2.3f;
        }

        smallLength =10;
        largeLength=20;
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i("ccc","on draw..start.. ");

        canvas.drawColor(0x66771111);
        mPaint.setColor(0x66111177);
        canvas.drawRoundRect(new RectF(0, 0, width, height), 30, 30, mPaint);



        mPaint.setColor(mBorderColor);
        float centerX=width/2;
        float centerY=height/2;
        canvas.drawCircle(centerX,centerY,radius,mPaint);
        float start_x,start_y;
        float end_x,end_y;
        for(int i=0;i<60;++i){
            start_x= radius *(float)Math.cos(Math.PI/180 * i * 6);
            start_y= radius *(float)Math.sin(Math.PI/180 * i * 6);
            if(i%5==0){
                end_x = start_x+largeLength*(float)Math.cos(Math.PI / 180 * i * 6);
                end_y = start_y+largeLength*(float)Math.sin(Math.PI/180 * i * 6);
            }else{
                end_x = start_x+smallLength*(float)Math.cos(Math.PI/180 * i * 6);
                end_y = start_y+smallLength*(float)Math.sin(Math.PI/180 * i * 6);
            }
            start_x+=centerX;
            end_x+=centerX;
            start_y+=centerY;
            end_y+=centerY;
            canvas.drawLine(start_x, start_y, end_x, end_y, mPaint);
        }
        canvas.drawCircle(centerX,centerY,20,mPaint);
        canvas.rotate(60,centerX,centerY);
        canvas.drawLine(centerX,centerY,centerX,centerY-radius,mPaint);
        Log.i("ccc","on draw..finish.. ");
    }
}
