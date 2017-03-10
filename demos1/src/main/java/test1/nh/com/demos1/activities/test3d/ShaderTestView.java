package test1.nh.com.demos1.activities.test3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/9/28.
 */
public class ShaderTestView extends View {



    /**
     * 背景颜色
     */
    private int mBackgroundColor = Color.rgb(255, 0, 0);
    /**
     * 默认背景颜色
     */
    private static final int DEFAULT_BACKGROUND_COLOR = Color.rgb(255, 255, 255);


    public ShaderTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShaderTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShaderTestView(Context context) {
        super(context);
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
        initShader();
    }

    private int getDesiredHeight() {
        return 200;
    }
    private int getDesiredWidth() {
        return 350;
    }


    private Paint mShaderPaint= new Paint();



    private void initShader(){
        Shader shader = new LinearGradient(0, 0, getMeasuredWidth(), getMeasuredHeight(), new int[] {
                mBackgroundColor & 0xDFFFFFFF,
                mBackgroundColor & 0xCFFFFFFF,
                mBackgroundColor & 0x00FFFFFF },
                null, Shader.TileMode.CLAMP);
        mShaderPaint.setShader(shader);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(mBackgroundColor);
        canvas.drawRect(0, 0, getMeasuredWidth(), getDesiredHeight(), mShaderPaint);  //  draw rec with linear gradient color ...
    }


}
