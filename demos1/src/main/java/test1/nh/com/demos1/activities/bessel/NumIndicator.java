package test1.nh.com.demos1.activities.bessel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.utils.math.MathVector2D;

/**
 * Created by Administrator on 2017/1/24.
 */
public class NumIndicator extends View {





    public NumIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NumIndicator(Context context) {
        super(context);
        init();
    }


    private void init(){

        textP=new TextPaint();
        int pixel= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        textP.setTextSize(pixel);
        textP.setColor(Color.rgb(255,255,255));
        textP.setFlags(TextPaint.ANTI_ALIAS_FLAG);
        textP.setTextAlign(Paint.Align.CENTER);

        bgPaint=new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.RED);

        testPaint=new Paint();
        testPaint.setAntiAlias(true);
        testPaint.setColor(Color.GREEN);

    }


    int vWidth,vHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureHintText();
        int widthSize=getMinWidth();
        int heightSize=getMinHeight();
        if (currentCount<10){
            vWidth=Math.max(widthSize,heightSize);
            vHeight=Math.max(widthSize,heightSize);
        } else {
            vWidth=widthSize;
            vHeight=heightSize;
        }
        setMeasuredDimension(vWidth, vHeight);
    }

    private int getMinHeight() {
        if (textRect!=null) return textRect.height()*2;
        else return 0;
    }

    private int getMinWidth() {
        if (textRect!=null) return textRect.width()*2;
        else return 0;
    }

    private void measureHintText(){
        textRect=new Rect();
        textP.getTextBounds(""+currentCount,0,(""+currentCount).length(),textRect);
    }

    int currentCount=10;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTextAtXY(canvas,vWidth/2,vHeight/2);
    }


    private void drawTextAtXY(Canvas canvas,float x,float y){
        int radius= (int) (textRect.height()*0.75f);
        if (currentCount>0){
            if ((currentCount+"").length()==1){
                canvas.drawCircle(x,y,radius,bgPaint);
            } else if ((currentCount+"").length()>1){
                float textWidth=textRect.width()/(currentCount+"").length()*((currentCount+"").length()-1f)/2;
                canvas.drawCircle(x-textWidth,y,radius,bgPaint);
                canvas.drawCircle(x+textWidth,y,radius,bgPaint);
                canvas.drawRect(new RectF(x-textWidth,y-radius,x+textWidth,y+radius),bgPaint);
            }
            canvas.drawText(""+currentCount,x,y+textRect.height()/2,textP);
        }
    }
    TextPaint textP;
    Rect textRect;
    Paint bgPaint,testPaint;


    /**
     *   setCount(0)  will hide the indicator, no need to call set visibility
     * @param count
     */
    public void setCount(int count){
        currentCount=count;
        requestLayout();
    }

    public int getCount(){
        return currentCount;
    }



}
