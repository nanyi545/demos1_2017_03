package test1.nh.com.demos1.activities.bessel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

import test1.nh.com.demos1.customView.CircleImageView;
import test1.nh.com.demos1.utils.math.MathVector2D;

/**
 * Created by Administrator on 2016/10/14.
 */
public class BesselView3 extends CircleImageView {

    private static int currentState ;
    private static final int STATE_AT_START=1;
    private static final int STATE_STRETCHING=2;

    private static final int STATE_RELEASING_TO_START=4;
    private static final int STATE_RELEASING_TO_END=5;
    private static final int STATE_AT_END=6;
    private static final int STATE_DISOLVING=7;



    public BesselView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BesselView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BesselView3(Context context) {
        super(context);
        init();
    }

    private Scroller toEndScroller,toStartScroller,disolveScroller;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureHintText();
        resetTextPosition();

    }


    private void measureHintText() {
        textP.getTextBounds(""+currentCount,0,(""+currentCount).length(),textRect);
        digits=(""+currentCount).length();
        r2= (float) Math.sqrt(textRect.width()*textRect.width()/digits/digits+textRect.height()*textRect.height())/2;
        r1=r2*initialRatio;
        distanceThreshold=r2*10;
        disolveR=r2/5;

    }

    private void resetTextPosition() {
        stickPoint.x=getWidth()*0.80f;
        stickPoint.y= getHeight()*0.2f;

        startPoint.x=getWidth()*0.80f;
        startPoint.y= getHeight()*0.2f;

        endPoint.x =getWidth()*0.8f;
        endPoint.y= getHeight()*0.2f;
    }


    /**
     * texts
     */
    int currentCount=7,digits;
    TextPaint textP;
    Rect textRect;
    float scaledDensity; //   sp/px --> sp * scaledDensity   = px




    private PointF stickPoint=new PointF(0f,0f);  //  this point should not be reset after init after onMeasure()
    private PointF endPoint=new PointF(80f,120f);
    private PointF ctrlP;
    private PointF startPoint=new PointF(0f,0f);

    PointF startPoint_1, startPoint_2,endPoint_1,endPoint_2;

    float r1,initialRatio=1f,r2,distanceThreshold;

    Path path;
    Paint bgPaint , circlePaint ;


    /**
     *  for disolving...
     */
    private float[] disolveXseed=new float[15];
    private float[] disolveYseed=new float[15];
    private float[] disolveX=new float[15];
    private float[] disolveY=new float[15];

    private float disolveR=0;

    private int currentDisolveStage=0;
    private static final int MAX_DISOLVE_STAGE=255;
    private void dissovleAtEnd(){
        disolveScroller.startScroll(0,0,0,MAX_DISOLVE_STAGE,600);
        invalidate();
    }

    Paint disolvePaint;


    private void init(){

        for (int ii=0;ii<disolveXseed.length;ii++){
            disolveXseed[ii]= (float) Math.random()-0.5f;
            disolveYseed[ii]= (float) Math.random()-0.5f;
        }



        currentState= STATE_AT_START;
        toEndScroller=new Scroller(getContext());
        toStartScroller=new Scroller(getContext(),new OvershootInterpolator(2));
        disolveScroller=new Scroller(getContext());

        bgPaint=new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.RED);

        circlePaint =new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.GREEN);

        scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;
        textP =new TextPaint();
        textP.setTextSize(18*scaledDensity);
        textP.setColor(Color.rgb(255,255,255));
        textP.setFlags(TextPaint.ANTI_ALIAS_FLAG);
        textP.setTextAlign(Paint.Align.CENTER);

        disolvePaint=new Paint();
        disolvePaint.setColor(Color.rgb(120,30,20));
        disolvePaint.setAntiAlias(true);


        textRect=new Rect();
        initPath();
    }



    private void initPath(){


        float distance= (float) Math.sqrt((stickPoint.x-endPoint.x)*(stickPoint.x-endPoint.x)+(stickPoint.y-endPoint.y)*(stickPoint.y-endPoint.y)) / (distanceThreshold);

        float ratio=initialRatio/(distance+1f);

        r1=r2*ratio;

        path=new Path();

        startPoint_1 =new PointF(stickPoint.x,stickPoint.y);
        startPoint_2 =new PointF(stickPoint.x,stickPoint.y);
        endPoint_1=new PointF(endPoint.x,endPoint.y);
        endPoint_2=new PointF(endPoint.x,endPoint.y);


        MathVector2D.VectorF v1=new MathVector2D.VectorF( endPoint.x-stickPoint.x , endPoint.y-stickPoint.y );

        v1.scaleTo(r1);
        v1.addAngle(-90);
        startPoint_1.offset(v1.dx,v1.dy);

        v1.addAngle(180);
        startPoint_2.offset(v1.dx,v1.dy);


        MathVector2D.VectorF v2=new MathVector2D.VectorF( endPoint.x-stickPoint.x , endPoint.y-stickPoint.y );
        v2.scaleTo(r2);
        v2.addAngle(-90);
        endPoint_1.offset(v2.dx,v2.dy);

        v2.addAngle(180);
        endPoint_2.offset(v2.dx,v2.dy);

        ctrlP=new PointF( stickPoint.x/2 + endPoint.x/2 , stickPoint.y/2 + endPoint.y/2  );


        path.moveTo(startPoint_1.x, startPoint_1.y);
        path.quadTo(ctrlP.x,ctrlP.y,endPoint_1.x,endPoint_1.y);
        path.lineTo(endPoint_2.x,endPoint_2.y);
        path.quadTo(ctrlP.x,ctrlP.y, startPoint_2.x, startPoint_2.y);
        path.lineTo(startPoint_1.x, startPoint_1.y);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (currentState==STATE_STRETCHING||currentState==STATE_RELEASING_TO_START || currentState==STATE_RELEASING_TO_END){
            if (!hasMoveOutside) {
                canvas.drawPath(path, bgPaint);
                canvas.drawCircle(stickPoint.x, stickPoint.y, r1, bgPaint);
                canvas.drawCircle(endPoint.x, endPoint.y, r2, bgPaint);
            }
        }

        drawHint(canvas);
        drawDisolv(canvas);
    }


    private void drawDisolv(Canvas canvas){
        if (currentState == STATE_DISOLVING  &&  toEndScroller.isFinished() ){
            for (int ii=0;ii<disolveX.length;ii++){
                canvas.drawCircle( disolveX[ii]+endPoint.x,disolveY[ii]+endPoint.y,disolveR, disolvePaint);
            }
        }
    }



    private void drawHint(Canvas canvas){
        if (currentState <= STATE_AT_END) {
            if (currentCount > 0) {
                if (digits == 1) {
                    canvas.drawCircle(endPoint.x, endPoint.y , r2, bgPaint);
                } else {
                    int halfWidth = (textRect.width() - textRect.width() / digits) / 2;
                    canvas.drawRect(endPoint.x - halfWidth, (endPoint.y  - r2), endPoint.x + halfWidth, (endPoint.y  + r2), bgPaint);
                    canvas.drawCircle(endPoint.x - halfWidth, endPoint.y, r2, bgPaint);
                    canvas.drawCircle(endPoint.x + halfWidth, endPoint.y, r2, bgPaint);
                }
            }
            canvas.drawText("" + currentCount, endPoint.x, endPoint.y  + textRect.height() / 2, textP);
        }
    }



    private boolean withInStart=false;

    private boolean hasMoveOutside=false;  // weather or not the finger has moved out side of the "back-zone"

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();

        switch (action){
            case MotionEvent.ACTION_DOWN:

                float touchX_= event.getX();
                float touchY_= event.getY();

                MathVector2D.VectorF temp=new MathVector2D.VectorF(touchX_-stickPoint.x,touchY_-stickPoint.y);

                if (temp.getLength()>(2*r2)){
                    withInStart=false;
                    return true;
                }  else {
                    withInStart=true;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if(withInStart) {



                    float touchX =  event.getX();
                    float touchY =  event.getY();

                    endPoint.x = touchX;
                    endPoint.y = touchY;


                    float dy = stickPoint.x - endPoint.x;
                    float dx = stickPoint.y - endPoint.y;
                    MathVector2D.VectorF v = new MathVector2D.VectorF(dx, dy);
                    if (v.getLength() < distanceThreshold ) {
                        currentState = STATE_STRETCHING;

                    } else {
                        hasMoveOutside=true;

                        dx = endPoint.x - startPoint.x;
                        dy = endPoint.y - startPoint.y;

                        boolean notToStart=( Math.sqrt(dx*dx+dy*dy)>0.1);
                        if (currentState==STATE_STRETCHING   || notToStart && toEndScroller.isFinished()) {
                            toEndScroller.startScroll((int) startPoint.x, (int) startPoint.y, (int) dx, (int) dy,10);
                            currentState = STATE_RELEASING_TO_END;
                            Log.i("BBB", "STATE_RELEASING_TO_END");
                        }
                    }


                    initPath();
                    invalidate();

                }

                break;

            case MotionEvent.ACTION_UP:

                if (withInStart) {
                    float dy = stickPoint.x - endPoint.x;
                    float dx = stickPoint.y - endPoint.y;
                    MathVector2D.VectorF v = new MathVector2D.VectorF(dx, dy);

                    if (v.getLength() < distanceThreshold ) {
                        dx = stickPoint.x - endPoint.x;
                        dy = stickPoint.y - endPoint.y;
                        toStartScroller.startScroll((int)endPoint.x, (int)endPoint.y, (int)dx, (int)dy);
                        currentState = STATE_RELEASING_TO_START;
                        Log.i("BBB", "STATE_RELEASING_TO_START");
                    } else {
//                        dx = endPoint.x - stickPoint.x;
//                        dy = endPoint.y - stickPoint.y;
//                        toEndScroller.startScroll((int)stickPoint.x, (int)stickPoint.y, (int)dx, (int)dy);
//                        currentState = STATE_RELEASING_TO_END;
//                        Log.i("BBB", "STATE_RELEASING_TO_END");

                        Log.i("BBB", "withInStart:"+withInStart+ "     separated:"+(v.getLength() < distanceThreshold)  +"      currentState==STATE_AT_END:"+(currentState==STATE_AT_END) );

                        if(currentState==STATE_AT_END  || currentState== STATE_RELEASING_TO_END){
                            toEndScroller.forceFinished(true);
                            currentState=STATE_DISOLVING;
                            Log.i("BBB","STATE_DISOLVING");
                            dissovleAtEnd();
                        }

                    }

                    invalidate();
                }
                break;

        }


        return true;
    }




    @Override
    public void computeScroll() {
        if (toEndScroller.computeScrollOffset()) {
            startPoint.x=toEndScroller.getCurrX();
            startPoint.y=toEndScroller.getCurrY();
            initPath();
            invalidate();
        } else {
            if (currentState==STATE_RELEASING_TO_END){
                currentState=STATE_AT_END;
                Log.i("BBB","STATE_AT_END");
//                dissovleAtEnd();
            }
        }
        if (toStartScroller.computeScrollOffset()) {
            endPoint.x=toStartScroller.getCurrX();
            endPoint.y=toStartScroller.getCurrY();
            initPath();
            invalidate();
        }  else {
            if (currentState==STATE_RELEASING_TO_START){
                currentState=STATE_AT_START;
                hasMoveOutside=false;

                startPoint.x=stickPoint.x;
                startPoint.y=stickPoint.y;
                endPoint.x=stickPoint.x;
                endPoint.y=stickPoint.y;

                Log.i("BBB","STATE_AT_START");
            }
        }


        if (disolveScroller.computeScrollOffset()){

            int alpha=MAX_DISOLVE_STAGE-disolveScroller.getCurrY();
            int progress=disolveScroller.getCurrY();
            disolvePaint.setAlpha(alpha);

            for (int ii=0;ii<disolveX.length;ii++){
                disolveX[ii]= disolveXseed[ii]*progress/MAX_DISOLVE_STAGE*2*(2*r2);
                disolveY[ii]= disolveYseed[ii]*progress/MAX_DISOLVE_STAGE*2*(2*r2);
            }

            invalidate();
        }



    }




}
