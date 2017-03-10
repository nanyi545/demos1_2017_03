package com.webcon.sus.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.webcon.sus.utils.BitmapUtil;

/**
 * 圆形头像
 * Created by Administrator on 15-6-2.
 * @author M
 */
public class RoundImageView extends ImageView{
    public static final String TAG = "RoundImageView";
    private static final int SHADOW_WIDTH = 1;
    private static final int WHITERIM_WIDTH = 4;
    private static final int Color_Shadow = 0xAADDDDDD;


    private Bitmap mBitmap;
    private Paint mPaint, shadowPaint, rimPaint;
    private int mRadius, mWidth, mHeight;
    private int cPadding = SHADOW_WIDTH + WHITERIM_WIDTH;

    //--------
    public RoundImageView(Context paramContext) {
        super(paramContext);
    }

    public RoundImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public RoundImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }


    @Override
    public void onDraw(@NonNull Canvas canvas){
        if(getDrawable() == null){
            return;
        }
//        //绘制阴影
//        canvas.drawCircle(mWidth/2, mHeight/2, mRadius, shadowPaint);
        //圆形图像
        if(mBitmap != null){
            canvas.drawBitmap(mBitmap, cPadding, cPadding, mPaint);
        }
        //绘制白边
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius - cPadding, rimPaint);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = Math.min(w, h) / 2;
        init();
        invalidate();
    }


    private void init(){
        //----------
//        shadowPaint = new Paint();
//        shadowPaint.setAntiAlias(true);
//        RadialGradient gradient = new RadialGradient(
//                mWidth/2, mHeight/2,    // 第一个,第二个参数表示渐变圆中心坐标
//                mRadius,                // 第三个参数表示半径
//                new int[]{Color.BLACK, Color.BLACK, Color_Shadow},
//                new float[]{0.f, 0.8f, 1.0f},
//                Shader.TileMode.CLAMP); // 第四个,第五个,第六个与线性渲染相同
//        shadowPaint.setShader(gradient);

        //----------
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);

        //----------
        rimPaint = new Paint();
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Paint.Style.STROKE);
        rimPaint.setStrokeWidth(WHITERIM_WIDTH);
        rimPaint.setColor(Color.WHITE);

        //-------
        BitmapDrawable bd = (BitmapDrawable) this.getDrawable();
        if(bd != null){
            int square = (mRadius - cPadding)<<1;
            mBitmap = createCircleBitmap(BitmapUtil.createFitBitmap(square, square, bd.getBitmap()));
        }
    }

    /**
     * 创建圆形图片
     * @param bitmap 压缩裁剪过后的正方形图片
     */
    private Bitmap createCircleBitmap(Bitmap bitmap){
        Log.i(TAG, "bitmap: " + bitmap.getWidth() + ", " + bitmap.getHeight());
        int squareSize = Math.max(bitmap.getWidth(), bitmap.getHeight());
        Rect rect = new Rect(0, 0, squareSize, squareSize);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setFilterBitmap(true);
        p.setDither(true);

        Bitmap bmp = Bitmap.createBitmap(squareSize, squareSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        //设成透明 --> 白色
        canvas.drawARGB(255, 255, 255, 255);
        //画一个圆形
        canvas.drawCircle(squareSize / 2, squareSize / 2, squareSize / 2, p);
        //截取
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //画上图片
        canvas.drawBitmap(bitmap, rect, rect, p);
        return bmp;
    }

}
