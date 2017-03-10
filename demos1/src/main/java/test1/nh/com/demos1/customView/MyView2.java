package test1.nh.com.demos1.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 15-10-17.
 * costum View 1
 */
public class MyView2 extends View {

    public MyView2(Context context, AttributeSet set)
    {
        super(context, set);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int height=350;  // reset the minimum size of the view???
//        int width=300;
//        setMeasuredDimension(width, height);
    }

    // 重写该方法，进行绘图
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        // 把整张画布绘制成白色
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        // 去锯齿
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        // 绘制圆形
        canvas.drawCircle(40, 40, 30, paint);
        canvas.drawRect(40, 40, 70, 70, paint);

        // ----------path----------
        Path mPath=new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(20, 100);
        canvas.drawPath(mPath,paint);

        // ----------设置填充风格后绘制----------
        paint.setStyle(Paint.Style.FILL);
        // 为Paint设置渐变器
        Shader mShader = new LinearGradient(100, 0, 140, 100
                , new int[] {
                Color.RED, Color.YELLOW }
                , null , Shader.TileMode.CLAMP);
        paint.setShader(mShader);
        canvas.drawRect(70, 0, 170, 100, paint);
    }
}
