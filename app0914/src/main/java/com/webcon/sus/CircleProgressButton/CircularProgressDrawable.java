package com.webcon.sus.CircleProgressButton;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * 环形进度条绘图Drawable
 * <p>封装了圆弧的一些属性
 */
public class CircularProgressDrawable extends Drawable {


    private float mSweepAngle;	//扫过的角度
    private float mStartAngle;	//起点的角度
    private int mSize;			//圆环尺寸
    private int mStrokeWidth;	//边界宽度
    private int mStrokeColor;	//边界颜色

    /**
     * 构造器初始化
     * @param size 圆环直径-包含边框
     * @param strokeWidth
     * @param strokeColor
     */
    public CircularProgressDrawable(int size, int strokeWidth, int strokeColor) {
        mSize = size;
        mStrokeWidth = strokeWidth;
        mStrokeColor = strokeColor;
        mStartAngle = -90;	//初始12点钟方向
        mSweepAngle = 0;	//初始弧长为0
    }

    /* ---- getter & setter ---- */
    /**
     * 设置弧长
     * @param sweepAngle
     */
    public void setSweepAngle(float sweepAngle) {
        mSweepAngle = sweepAngle;
    }

    public int getSize() {
        return mSize;
    }

    @Override
    public void draw(Canvas canvas) {//绘制
        final Rect bounds = getBounds();

        if (mPath == null) {
            mPath = new Path();
        }
        mPath.reset();
        mPath.addArc(getRect(), mStartAngle, mSweepAngle);	//从起点开始绘制一段sweep长的圆弧
        mPath.offset(bounds.left, bounds.top);
        canvas.drawPath(mPath, createPaint());
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return 1;
    }

    private RectF mRectF;
    private Paint mPaint;
    private Path mPath;

    /**
     * 初始化绘制区域
     * @return
     */
    private RectF getRect() {
        if (mRectF == null) {
            int index = mStrokeWidth / 2;
            mRectF = new RectF(index, index, getSize() - index, getSize() - index);
        }
        return mRectF;
    }

    /**
     * 初始化画笔
     * @return
     */
    private Paint createPaint() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setColor(mStrokeColor);
        }

        return mPaint;
    }

}
