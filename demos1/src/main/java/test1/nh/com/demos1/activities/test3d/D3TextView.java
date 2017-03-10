package test1.nh.com.demos1.activities.test3d;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/9/27.
 */
public class D3TextView extends View {

    public D3TextView(Context context) {
        super(context);
        init();
    }

    public D3TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public D3TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextPaint mTextPaintNormal;

    private void init(){
        mTextPaintNormal = new TextPaint();
        mTextPaintNormal.setTextSize(32);
        mTextPaintNormal.setColor(Color.GREEN);
        mTextPaintNormal.setFlags(TextPaint.ANTI_ALIAS_FLAG);
        mTextPaintNormal.setTextAlign(Paint.Align.CENTER);

        matrix = new Matrix();
        camera = new Camera();
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
        return 200;
    }
    private int getDesiredWidth() {
        return 350;
    }


    Camera camera;
    Matrix matrix;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);





        canvas.save();
        camera.save();
        camera.translate(0, 0, 0);
        camera.rotate(0, 0, 45);
        camera.getMatrix(matrix);
        //绕(curX,curY)变换
//        matrix.preTranslate(-50, -50);
//        matrix.postTranslate(50,50);
        camera.restore();
        canvas.concat(matrix);
        mTextPaintNormal.setColor(Color.RED);
        canvas.drawText("center", 50, 50, mTextPaintNormal);
        canvas.restore();


        canvas.save();
        camera.save();
        camera.translate(0, 0, 0);
        camera.rotate(0, 0, 45);
        camera.getMatrix(matrix);
        //绕(curX,curY)变换
        matrix.preTranslate(-50, -50);  // so that the rotation happens arround the point 50,50
        matrix.postTranslate(50,50);
        camera.restore();
        canvas.concat(matrix);
        mTextPaintNormal.setColor(Color.BLUE);
        canvas.drawText("center", 50, 50, mTextPaintNormal);
        canvas.restore();



        mTextPaintNormal.setColor(Color.GREEN);
//        canvas.drawText("center", 50, 50, mTextPaintNormal);
        canvas.drawText("bottom", 50, 100, mTextPaintNormal);
        canvas.drawText("bottom2", 50, 150, mTextPaintNormal);

    }
}
