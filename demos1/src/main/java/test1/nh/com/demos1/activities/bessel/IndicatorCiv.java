package test1.nh.com.demos1.activities.bessel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

import test1.nh.com.demos1.customView.CircleImageView;
import test1.nh.com.demos1.utils.math.MathVector2D;

/**
 * Created by Administrator on 2016/10/13.
 */
public class IndicatorCiv extends CircleImageView {


    private static int currentState;
    private static final int STATE_AT_START=1;
    private static final int STATE_STRETCHING=2;
    private static final int STATE_RELEASING_TO_START=3;
    private static final int STATE_RELEASING_TO_END=4;
    private static final int STATE_AT_END=5;
    private static final int STATE_DISOLVING=6;




    public IndicatorCiv(Context context) {
        super(context);
        init();
    }

    public IndicatorCiv(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorCiv(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private Scroller toEndScroller,toStartScroller,disolveScroller;


    TextPaint textP;
    Rect textRect;
    Paint bgPaint,disolvePaint;

    float scaledDensity; //   sp/px --> sp * scaledDensity   = px

    Paint paintCircle;

    private void init(){

        for (int ii=0;ii<disolveXseed.length;ii++){
            disolveXseed[ii]= (float) Math.random()-0.5f;
            disolveYseed[ii]= (float) Math.random()-0.5f;
        }


        toEndScroller=new Scroller(getContext());
        toStartScroller=new Scroller(getContext());
        disolveScroller=new Scroller(getContext());

        currentState=STATE_AT_START;
        scaledDensity = getContext().getResources().getDisplayMetrics().scaledDensity;

        textP = new TextPaint();
        textP.setTextSize(26*scaledDensity);
        textP.setColor(Color.rgb(255,255,255));
        textP.setFlags(TextPaint.ANTI_ALIAS_FLAG);
        textP.setTextAlign(Paint.Align.CENTER);


        bgPaint=new Paint();
        bgPaint.setColor(Color.rgb(255,64,64));
        bgPaint.setAntiAlias(true);

        disolvePaint=new Paint();
        disolvePaint.setColor(Color.rgb(255,64,64));
        disolvePaint.setAntiAlias(true);


        textRect=new Rect();

        paintCircle=new Paint();
        paintCircle.setColor(Color.rgb(20,130,20));
        paintCircle.setAntiAlias(true);


        p_ctrl1=new Point();
        p_ctrl2=new Point();
    }


    int textX,textCenterX;
    int textY,textCenterY;
    int stickPointX;
    int stickPointY;

    int startPointX;
    int startPointY;


    int stickerR=15;
    private boolean withSticker =false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawStretched(canvas);
        drawHint(canvas);
        drawDisolv(canvas);

    }


    int digits=0;
    float circleRadius=0;


    private void drawDisolv(Canvas canvas){
        if (currentState == STATE_AT_END){
            for (int ii=0;ii<disolveX.length;ii++){
                canvas.drawCircle( disolveX[ii]+p_2[2].x / 2 + p_2[3].x / 2,disolveY[ii]+p_2[2].y / 2 + p_2[3].y / 2,disolveR, disolvePaint);
            }
        }
    }


    private void drawStretched(Canvas canvas){

        if (currentState==STATE_STRETCHING  ||  currentState ==STATE_RELEASING_TO_END  || currentState == STATE_RELEASING_TO_START) {

//            if (currentState==STATE_STRETCHING){
//                canvas.drawCircle(p_1[0].x / 2 + p_1[1].x / 2, p_1[0].y / 2 + p_1[1].y / 2, (int) (r1 / Math.sqrt(2)), bgPaint);
//                canvas.drawCircle(p_2[2].x / 2 + p_2[3].x / 2, p_2[2].y / 2 + p_2[3].y / 2, (int) ( r2 / Math.sqrt(2)), bgPaint);
//                canvas.drawCircle(textCenterX,textCenterY,   r2, bgPaint);
//            }

            canvas.drawCircle(p_1[0].x / 2 + p_1[1].x / 2, p_1[0].y / 2 + p_1[1].y / 2, (int) (r1 / Math.sqrt(2)), bgPaint);
            Log.i("BBB","sticker circle x:"+(p_1[0].x / 2 + p_1[1].x / 2)+"y:"+(p_1[0].y / 2 + p_1[1].y / 2)+"  r1:"+((int) (r1 / Math.sqrt(2))));
            canvas.drawCircle(textCenterX,textCenterY,   r2, bgPaint);

            canvas.drawPath(path, bgPaint);
//            canvas.drawCircle( textCenterX,textCenterY, 2, paintCircle);
        }

    }



    private void drawHint(Canvas canvas){
        if (currentState < STATE_AT_END) {
            if (currentCount > 0) {
                if (digits == 1) {
                    canvas.drawCircle(textX, textY - textRect.height() / 2, circleRadius, bgPaint);
                } else {
                    int halfWidth = (textRect.width() - textRect.width() / digits) / 2;
                    textRect.offsetTo(textX - textRect.width() / 2, textY - textRect.height());
                    canvas.drawRect(textX - halfWidth, (textRect.centerY() - circleRadius), textX + halfWidth, (textRect.centerY() + circleRadius), bgPaint);
                    canvas.drawCircle(textX - halfWidth, textRect.centerY(), circleRadius, bgPaint);
                    canvas.drawCircle(textX + halfWidth, textRect.centerY(), circleRadius, bgPaint);
                }
            }
            canvas.drawText("" + currentCount, textX, textY, textP);
        }
    }


    int currentCount=12;

    Point p_ctrl1,p_ctrl2;
    Point[] p_1=new Point[4];
    Point[] p_2=new Point[4];

    Path path;
    private void resetCtrlPoints(){
        float n=2f;
        int x1= (int) (stickPointX+(textCenterX-stickPointX)/n);
        int y1= (int) (stickPointY+(textCenterY-stickPointY)/n);
        p_ctrl1=new Point(x1,y1);

        MathVector2D.Vector v1=new MathVector2D.Vector( textCenterX-stickPointX , textCenterY-stickPointY );
        v1.scaleTo(r1/4);
        v1.addAngle(-90);
        p_ctrl1.offset(v1.dx,v1.dy);

        int x2= (int) (textCenterX-(textCenterX-stickPointX)/n);
        int y2= (int) (textCenterY-(textCenterY-stickPointY)/n);
        p_ctrl2=new Point(x2,y2);

        v1.addAngle(-180);
        p_ctrl2.offset(v1.dx,v1.dy);

    }


    int r1,r2; //  stick point radius

    private void initPath(){
        r1 = (int) (circleRadius*1.2f);
        r2 = (int) (circleRadius*1.2f);

        disolveR =r1/4f;

        textCenterX = textX;
        textCenterY = textY-textRect.height() / 2;

        resetCtrlPoints();

        path=new Path();

        p_1[0]=new Point(stickPointX,stickPointY);
        MathVector2D.Vector v1=new MathVector2D.Vector( textCenterX-stickPointX , textCenterY-stickPointY );
        v1.scaleTo(r1);
        v1.addAngle(-45);
        p_1[0].offset(v1.dx,v1.dy);
        p_1[1]=new Point(stickPointX,stickPointY);
        v1.addAngle(90);
        p_1[1].offset(v1.dx,v1.dy);
        p_1[2]=new Point(stickPointX,stickPointY);
        v1.addAngle(90);
        p_1[2].offset(v1.dx,v1.dy);
        p_1[3]=new Point(stickPointX,stickPointY);
        v1.addAngle(90);
        p_1[3].offset(v1.dx,v1.dy);

        MathVector2D.Vector v2=new MathVector2D.Vector( textCenterX-stickPointX , textCenterY-stickPointY );
        v2.scaleTo(r2);
        v2.addAngle(-45);

        p_2[0]=new Point(textCenterX ,textCenterY);
        p_2[0].offset(v2.dx,v2.dy);

        p_2[1]=new Point(textCenterX ,textCenterY);
        v2.addAngle(90);
        p_2[1].offset(v2.dx,v2.dy);
        p_2[2]=new Point(textCenterX ,textCenterY);
        v2.addAngle(90);
        p_2[2].offset(v2.dx,v2.dy);
        p_2[3]=new Point(textCenterX ,textCenterY);
        v2.addAngle(90);
        p_2[3].offset(v2.dx,v2.dy);


        path.moveTo(p_1[0].x,p_1[0].y);
//        connectingPath.cubicTo(p_ctrl1.x,p_ctrl1.y,p_ctrl2.x,p_ctrl2.y,p_2[3].x,p_2[3].y);
        path.quadTo(p_ctrl1.x,p_ctrl1.y,p_2[3].x,p_2[3].y);
        path.lineTo(p_2[2].x,p_2[2].y);

//        connectingPath.cubicTo(p_ctrl2.x,p_ctrl2.y,p_ctrl1.x,p_ctrl1.y,p_1[1].x,p_1[1].y);
        path.quadTo(p_ctrl2.x,p_ctrl2.y,p_1[1].x,p_1[1].y);
        path.close();

    }





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mesureHintText();
        resetTextPosition();
        stickPointX=textX;
        stickPointY=textY -textRect.height() / 2 ;  // -textRect.height() / 2
    }

    private void resetTextPosition() {
        textX=getWidth()/4*3;
        textY= (int) (getHeight()*(1/2f-0.43f))+textRect.height();
    }


    private void mesureHintText() {
        textP.getTextBounds(""+currentCount,0,(""+currentCount).length(),textRect);
        digits=(""+currentCount).length();
        circleRadius= (float) Math.sqrt(textRect.width()*textRect.width()/digits/digits+textRect.height()*textRect.height())/2;
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();

        switch (action){
            case MotionEvent.ACTION_DOWN:

                int touchX_= (int) event.getX();
                int touchY_= (int) event.getY();

                MathVector2D.Vector temp=new MathVector2D.Vector(touchX_-stickPointX,touchY_-stickPointY);

                if (temp.getLength()>(2*stickerR)){
                    withSticker =false;
                    return true;
                }  else {
                    withSticker =true;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if(withSticker) {
                    currentState = STATE_STRETCHING;

                    int touchX = (int) event.getX();
                    int touchY = (int) event.getY();

                    textX = touchX;
                    textY = touchY  + textRect.height() / 2;


                    initPath();
                    invalidate();

                }

                break;

            case MotionEvent.ACTION_UP:
                if (withSticker) {
                    int dx = textX - stickPointX;
                    int dy = textY - stickPointY;
                    MathVector2D.Vector v = new MathVector2D.Vector(dx, dy);
                    if (v.getLength() < 100) {
                        dx = stickPointX - textX;
                        dy = stickPointY + textRect.height()/2 - textY;
                        toStartScroller.startScroll(textX, textY, dx, dy);
                        currentState = STATE_RELEASING_TO_START;
                        Log.i("BBB", "STATE_RELEASING_TO_START");
                    } else {
                        dx = textX - stickPointX;
                        dy = textY - stickPointY;
                        toEndScroller.startScroll(stickPointX, stickPointY, dx, dy);
                        currentState = STATE_RELEASING_TO_END;
                        Log.i("BBB", "STATE_RELEASING_TO_END");
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
            stickPointX=toEndScroller.getCurrX();
            stickPointY=toEndScroller.getCurrY();
            initPath();
            invalidate();
        } else {
            if (currentState==STATE_RELEASING_TO_END){
                currentState=STATE_AT_END;
                Log.i("BBB","STATE_AT_END");
                dissovleAtEnd();
            }
        }
        if (toStartScroller.computeScrollOffset()) {
            textX=toStartScroller.getCurrX();
            textY=toStartScroller.getCurrY();
            initPath();
            invalidate();
        }  else {
            if (currentState==STATE_RELEASING_TO_START){
                currentState=STATE_AT_START;
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




}
