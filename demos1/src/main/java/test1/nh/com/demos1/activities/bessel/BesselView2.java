package test1.nh.com.demos1.activities.bessel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import test1.nh.com.demos1.utils.math.MathVector2D;

/**
 * Created by Administrator on 2016/10/11.
 */
public class BesselView2 extends View {

    private static int currentState;
    private static final int STATE_AT_START=1;
    private static final int STATE_STRETCHING=2;
    private static final int STATE_RELEASING_TO_START=3;
    private static final int STATE_RELEASING_TO_END=4;
    private static final int STATE_AT_END=5;
    private static final int STATE_DISOLVING=6;



    public BesselView2(Context context) {
        super(context);
        init(context);
        initPath();

    }

    public BesselView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initPath();

    }

    public BesselView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initPath();

    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        //Get the width measurement
        int widthSize = View.resolveSize(getDesiredWidth(), widthMeasureSpec);

        //Get the height measurement
        int heightSize = View.resolveSize(getDesiredHeight(), heightMeasureSpec);

        //MUST call this to store the measurements
        setMeasuredDimension(widthSize, heightSize);

    }

    private int getDesiredHeight() {
        return 300;
    }

    private int getDesiredWidth() {
        return 300;
    }

    Path path;

    private Scroller toEndScroller,toStartScroller,disolveScroller;


    private float[] disolveXseed=new float[15];
    private float[] disolveYseed=new float[15];
    private float[] disolveX=new float[15];
    private float[] disolveY=new float[15];

    private float disolveR=0;

    private void init(Context context){
        currentState=STATE_AT_START;
        Log.i("BBB","STATE_AT_START");


        for (int ii=0;ii<disolveXseed.length;ii++){
            disolveXseed[ii]= (float) Math.random()-0.5f;
            disolveYseed[ii]= (float) Math.random()-0.5f;
        }
        disolveR=r2/5;


        toEndScroller=new Scroller(context);
        toStartScroller=new Scroller(context);
        disolveScroller=new Scroller(context);


        // ---init paint
        paint=new Paint();
        paint.setColor(Color.rgb(120,30,20));
        paint.setAntiAlias(true);

        paintCircle=new Paint();
        paintCircle.setColor(Color.rgb(20,130,20));
        paintCircle.setAntiAlias(true);

        disolvePaint=new Paint();
        disolvePaint.setColor(Color.rgb(120,30,20));
        disolvePaint.setAntiAlias(true);

    }


    private int currentDisolveStage=0;
    private static final int MAX_DISOLVE_STAGE=255;
    private void dissovleAtEnd(){
        disolveScroller.startScroll(0,0,0,MAX_DISOLVE_STAGE,600);
        invalidate();
    }


    @Override
    public void computeScroll() {
        if (toEndScroller.computeScrollOffset()) {
            p_center1.x=toEndScroller.getCurrX();
            p_center1.y=toEndScroller.getCurrY();
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
            p_center2.x=toStartScroller.getCurrX();
            p_center2.y=toStartScroller.getCurrY();
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



    boolean withInStart=false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();

        switch (action){
            case MotionEvent.ACTION_DOWN:

                int touchX_= (int) event.getX();
                int touchY_= (int) event.getY();

                MathVector2D.Vector temp=new MathVector2D.Vector(touchX_-p_center1.x,touchY_-p_center1.y);

                if (temp.getLength()>(2*r1)){
                    withInStart=false;
                    return true;
                }  else {
                    withInStart=true;
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if(withInStart) {
                    currentState = STATE_STRETCHING;

                    int touchX = (int) event.getX();
                    int touchY = (int) event.getY();

                    p_center2.x = touchX;
                    p_center2.y = touchY;
                    initPath();
                    invalidate();

                }

                break;

            case MotionEvent.ACTION_UP:
                if (withInStart) {
                    int dy = p_center1.x - p_center2.x;
                    int dx = p_center1.y - p_center2.y;
                    MathVector2D.Vector v = new MathVector2D.Vector(dx, dy);
                    if (v.getLength() < 150) {
                        dx = p_center1.x - p_center2.x;
                        dy = p_center1.y - p_center2.y;
                        toStartScroller.startScroll(p_center2.x, p_center2.y, dx, dy);
                        currentState = STATE_RELEASING_TO_START;
                        Log.i("BBB", "STATE_RELEASING_TO_START");
                    } else {
                        dx = p_center2.x - p_center1.x;
                        dy = p_center2.y - p_center1.y;
                        toEndScroller.startScroll(p_center1.x, p_center1.y, dx, dy);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (currentState<STATE_AT_END) {
            canvas.drawCircle(p_1[0].x / 2 + p_1[1].x / 2, p_1[0].y / 2 + p_1[1].y / 2, (int) (r1 / Math.sqrt(2)), paint);
            canvas.drawCircle(p_2[2].x / 2 + p_2[3].x / 2, p_2[2].y / 2 + p_2[3].y / 2, (int) (r2 / Math.sqrt(2)), paint);
            canvas.drawPath(path, paint);
        } else {

            for (int ii=0;ii<disolveX.length;ii++){
                canvas.drawCircle( disolveX[ii]+p_2[2].x / 2 + p_2[3].x / 2,disolveY[ii]+p_2[2].y / 2 + p_2[3].y / 2,disolveR, disolvePaint);
            }

        }


//        canvas.drawCircle( p_center1.x,p_center1.y,r1, paint);
//        canvas.drawCircle( p_center2.x,p_center2.y,r2, paint);


//        canvas.drawCircle( p_1[0].x,p_1[0].y,1, paintCircle);
//        canvas.drawCircle( p_1[1].x,p_1[1].y,1, paintCircle);
//        canvas.drawCircle( p_2[3].x,p_2[3].y,1, paintCircle);
//        canvas.drawCircle( p_2[2].x,p_2[2].y,1, paintCircle);
//        canvas.drawCircle( p_ctrl1.x,p_ctrl1.y,1, paintCircle);
//        canvas.drawCircle( p_ctrl2.x,p_ctrl2.y,1, paintCircle);
    }

    Paint paint,paintCircle,disolvePaint;

    Point p_center1=new Point(50,50);
    Point p_center2=new Point(50,50);

    Point p_ctrl1,p_ctrl2;

    private void resetCtrlPoints(){
        float n=2f;
        int x1= (int) (p_center1.x+(p_center2.x-p_center1.x)/n);
        int y1= (int) (p_center1.y+(p_center2.y-p_center1.y)/n);
        p_ctrl1=new Point(x1,y1);

        MathVector2D.Vector v1=new MathVector2D.Vector( p_center2.x-p_center1.x , p_center2.y-p_center1.y );
        v1.scaleTo(8);
        v1.addAngle(-90);
        p_ctrl1.offset(v1.dx,v1.dy);

        int x2= (int) (p_center2.x-(p_center2.x-p_center1.x)/n);
        int y2= (int) (p_center2.y-(p_center2.y-p_center1.y)/n);
        p_ctrl2=new Point(x2,y2);

        v1.addAngle(-180);
        p_ctrl2.offset(v1.dx,v1.dy);

    }

    int r1=10,r2=20;

    Point[] p_1=new Point[4];
    Point[] p_2=new Point[4];


    private void initPath(){
        resetCtrlPoints();

        path=new Path();

        p_1[0]=new Point(p_center1.x,p_center1.y);
        MathVector2D.Vector v1=new MathVector2D.Vector( p_center2.x-p_center1.x , p_center2.y-p_center1.y );
        v1.scaleTo(r1);
        v1.addAngle(-45);
        p_1[0].offset(v1.dx,v1.dy);
        p_1[1]=new Point(p_center1.x,p_center1.y);
        v1.addAngle(90);
        p_1[1].offset(v1.dx,v1.dy);
        p_1[2]=new Point(p_center1.x,p_center1.y);
        v1.addAngle(90);
        p_1[2].offset(v1.dx,v1.dy);
        p_1[3]=new Point(p_center1.x,p_center1.y);
        v1.addAngle(90);
        p_1[3].offset(v1.dx,v1.dy);

        MathVector2D.Vector v2=new MathVector2D.Vector( p_center2.x-p_center1.x , p_center2.y-p_center1.y );
        v2.scaleTo(r2);
        v2.addAngle(-45);

        p_2[0]=new Point(p_center2.x,p_center2.y);
        p_2[0].offset(v2.dx,v2.dy);

        p_2[1]=new Point(p_center2.x,p_center2.y);
        v2.addAngle(90);
        p_2[1].offset(v2.dx,v2.dy);
        p_2[2]=new Point(p_center2.x,p_center2.y);
        v2.addAngle(90);
        p_2[2].offset(v2.dx,v2.dy);
        p_2[3]=new Point(p_center2.x,p_center2.y);
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





}
