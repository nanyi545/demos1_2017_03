package test1.nh.com.demos1.activities.bessel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/10/11.
 */
public class BesselView1 extends View {


    public BesselView1(Context context) {
        super(context);
        initData();
        initPath();

    }

    public BesselView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
        initPath();

    }

    public BesselView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
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
        return 150;
    }

    private int getDesiredWidth() {
        return 150;
    }

    Path path;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(path,paint);
        canvas.drawLine(p_start.x,p_start.y,p_end.x,p_end.y,paint);


//        for (int ii=0;ii<pointsOnPath.length;ii++){
//            canvas.drawCircle( pointsOnPath[ii].x,pointsOnPath[ii].y,10, paintCircle);
//        }

    }


    Paint paint,paintCircle;

    private float ratio=0;

    Point p_start=new Point(1,1);
    Point p_end=new Point(120,120);
    Point p_control=new Point(89,20);

    Point[] pointsOnPath=new Point[30];

    Point computePointOnLine(Point start,Point end,float ratio){
        int middleX=start.x+(int)((end.x-start.x)*ratio);
        int middleY=start.y+(int)((end.y-start.y)*ratio);
        return new Point(middleX,middleY);
    }


    Point computePointOnCurve(Point start,Point end,Point control,float ratio){
        Point temp1=computePointOnLine(start,control,ratio);
        Point temp2=computePointOnLine(control,end,ratio);
        return computePointOnLine(temp1,temp2,ratio);
    }


    private void initPath(){
        path=new Path();
        path.moveTo(p_start.x,p_start.y);
//        for (int ii=0;ii<pointsOnPath.length-2;ii++){
//            connectingPath.cubicTo(pointsOnPath[ii].x,pointsOnPath[ii].y,pointsOnPath[ii+1].x,pointsOnPath[ii+1].y,pointsOnPath[ii+2].x,pointsOnPath[ii+2].y);
//        }
        path.quadTo(p_control.x,p_control.y,p_end.x,p_end.y);

        paint=new Paint();
        paint.setColor(Color.rgb(120,30,20));
        paint.setAntiAlias(true);


        paintCircle=new Paint();
        paintCircle.setColor(Color.rgb(20,130,20));
        paintCircle.setAntiAlias(true);


    }




    private void initData(){
        for (int ii=0;ii<pointsOnPath.length;ii++){
            ratio=1.0f/pointsOnPath.length*ii;
            pointsOnPath[ii]=computePointOnCurve(p_start,p_end,p_control,ratio);
        }
    }



}
