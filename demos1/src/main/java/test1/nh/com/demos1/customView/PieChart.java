package test1.nh.com.demos1.customView;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.util.IllegalFormatWidthException;

/**
 * Created by Administrator on 2017/4/11.
 */

public class PieChart extends View {


    public PieChart(Context context) {
        this(context,null);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private static Paint getPaint(int color,int strokeWidth){
        Paint p1 = new Paint();
        p1.setAntiAlias(true);
        p1.setStyle(Paint.Style.STROKE);
        p1.setStrokeCap(Paint.Cap.BUTT);
        p1.setStrokeJoin(Paint.Join.MITER);
        p1.setStrokeWidth(strokeWidth);
        p1.setColor(color);
        return p1;
    }


    private void init(){

        scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        itemTextSize=DEFAULT_ITEM_TEXT_SIZE*scaledDensity;

        animateScroller=new Scroller(getContext(),new AccelerateDecelerateInterpolator());

        txtPaint1=new TextPaint();
        txtPaint1 = new TextPaint();
        txtPaint1.setTextSize(itemTextSize);
        txtPaint1.setColor(Color.rgb(64,64,64));
        txtPaint1.setFlags(TextPaint.ANTI_ALIAS_FLAG);
        txtPaint1.setTextAlign(Paint.Align.CENTER);

        txtPaint2=new TextPaint();
        txtPaint2 = new TextPaint();
        txtPaint2.setTextSize(itemTextSize);
        txtPaint2.setColor(Color.rgb(64,64,64));
        txtPaint2.setFlags(TextPaint.ANTI_ALIAS_FLAG);
        txtPaint2.setTextAlign(Paint.Align.CENTER);



//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPieChart(true);
////                test();
//            }
//        });

    }


    private void test(){
        currentProgress+=1;
        invalidate();
    }


    public void setData(int[] newPercentages,int[] newColors,float newTotal){
        dataHasBeenSet=true;
        percentages=newPercentages;
        int colors=newColors.length;
        paints=new Paint[colors];
        for (int i=0;i<colors;i++){
            paints[i]=getPaint(newColors[i],arcWidth);
        }
        totalAmount=newTotal;
    }


    public void showPieChart(boolean animated) {
        if (dataHasBeenSet) {
            if (animated) {
                animateScroller.startScroll(0, 0, 100, 0, 5000);
            } else {
                currentProgress = 100;
            }
            invalidate();
        }
    }

    public void setDataAndshowPieChart(int[] newPercentages,int[] newColors,float newTotal,boolean animated){
        setData(newPercentages,newColors,newTotal);
        showPieChart(animated);
    }


    private float itemTextSize; // in sp
    private static final float DEFAULT_ITEM_TEXT_SIZE=16; // sp
    float scaledDensity; //   sp/px --> sp * scaledDensity   = px

    private Paint[] paints;
    private int[] percentages;
    private boolean dataHasBeenSet;

    private void resetPaintsWidth(int arcWidth){
        if (paints!=null){
            for (Paint temp:paints){
                if (temp!=null){
                    temp.setStrokeWidth(arcWidth);
                }
            }
        }
    }




    int viewWidth,viewHeight;
    int centerX,centerY,radius,arcWidth;
    RectF arcHolder;

    /**
     *  modify this to change width/height ratio
     */
    private float heightToWidthRatio=0.5f;


    TextPaint txtPaint1,txtPaint2;




    private void computeSize(){
        centerX=viewWidth/2;
        centerY=viewHeight/2;
        radius= (int) (Math.min(viewWidth,viewHeight)/2*1.0f);
        arcWidth= (int) (radius/2.5f);
        resetPaintsWidth(arcWidth);
        int diff=radius-arcWidth; // so that the full arcWidth is in the radius
        arcHolder=new RectF(centerX-diff,centerY-diff,centerX+diff,centerY+diff);
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSpecMode==MeasureSpec.EXACTLY){
            viewWidth=widthSpecSize;
        } else {
            throw new IllegalArgumentException("only use MATCH_PARENT/DIP to specify width for PieChart !!");
        }
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.i("aaa","  widthSpecMode:"+widthSpecMode+"   heightSpecMode:"+heightSpecMode);

        if (heightSpecMode==MeasureSpec.EXACTLY){
            viewHeight= (int) (viewWidth*heightToWidthRatio);
        } else {
            throw new IllegalArgumentException("only use MATCH_PARENT/DIP to specify height for PieChart !!");
        }
        setMeasuredDimension(viewWidth,viewHeight);
        computeSize();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.i("aaa", "onDraw  --->  currentProgress:" + currentProgress);
        if(dataHasBeenSet) {
            drawArc(canvas);
        }

    }


    private static final float AMOUNT_TO_OVERLAP=1.3f; // other wise there will be gaps ...

    private void drawArc(Canvas canvas){

        for (int i=0;i<percentages.length;i++){
            int startAngle;
            int endAngle;
            if(i==0){
                startAngle=0;
            } else {
                startAngle= (int) (percentages[i-1]/100f*360);
                if ( percentages[i-1] > currentProgress ) break;
            }
            if ( percentages[i] <  currentProgress ){
                endAngle = (int) (percentages[i]/100f*360);
            } else {
                endAngle =  (int) (currentProgress/100f*360);
            }
            Log.i("aaa","i:"+i+"    currentProgress:"+currentProgress+"  startAngle:"+startAngle+"   endAngle:"+endAngle);
            if (endAngle>startAngle) {
                canvas.drawArc(arcHolder, startAngle-AMOUNT_TO_OVERLAP, endAngle - startAngle+AMOUNT_TO_OVERLAP, false, paints[i]);
            }
        }
        canvas.drawText( "当前总资产",centerX, (float) (centerY*0.85),txtPaint2);
        canvas.drawText( String.format("%.2f", totalAmount*currentProgress/100)+"元",centerX, (float) (centerY*1.15),txtPaint1);
    }


    Scroller animateScroller;




    int currentProgress=0;


    @Override
    public void computeScroll() {
        if (animateScroller.computeScrollOffset()){
            currentProgress=animateScroller.getCurrX();
            Log.i("aaa","currentProgress:"+currentProgress);
            postInvalidate();
        }
    }


    private float totalAmount;



}
