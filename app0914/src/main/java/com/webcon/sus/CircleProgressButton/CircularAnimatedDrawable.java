package com.webcon.sus.CircleProgressButton;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * 环形动画绘图Drawable
 * <p>封装了圆弧的两个动画
 */
@SuppressLint("NewApi")
public class CircularAnimatedDrawable extends Drawable implements Animatable {
	private static final String TAG = "CircularAnimated";
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    private static final int ANGLE_ANIMATOR_DURATION = 2000;
    private static final int SWEEP_ANIMATOR_DURATION = 600;
    public static final int MIN_SWEEP_ANGLE = 30;	//最小弧度
    
    private final RectF fBounds = new RectF();		//绘制区域
    
    private ObjectAnimator mObjectAnimatorSweep;	//动画-弧度-头部
    private ObjectAnimator mObjectAnimatorAngle;	//动画-角度-尾端
    
    private boolean mModeAppearing;		//成员变量，默认值为false //true:进度增长;false:进度缩短
    private Paint mPaint;
    
    private float mCurrentGlobalAngleOffset;		//当前角度偏移
    private float mCurrentGlobalAngle;				//*当前角度-起点
    private float mCurrentSweepAngle;				//*当前弧度
    private float mBorderWidth;						//边界宽度
    private boolean mRunning;						//是否正在动画

    /**
     * 构造器初始化
     * @param color	进度条画笔颜色
     * @param borderWidth 边界宽度
     */
    public CircularAnimatedDrawable(int color, float borderWidth) {
        mBorderWidth = borderWidth;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(color);

        setupAnimations();
    }

    @Override
    public void draw(Canvas canvas) {
        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
        float sweepAngle = mCurrentSweepAngle;
//        Log.w(TAG, "-----------");
//        Log.i(TAG, "mCurrentGlobalAngle:" + mCurrentGlobalAngle);
//        Log.i(TAG, "mCurrentGlobalAngleOffset" + mCurrentGlobalAngleOffset);
        if (!mModeAppearing) {
        	//尾追
            startAngle = startAngle + sweepAngle;
            sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
//        	Log.i(TAG, "!mode appearing");
//        	Log.i(TAG, "startAngle:"+startAngle);
//        	Log.i(TAG, "sweepAngle:"+sweepAngle);
        } else {
        	//首进
            sweepAngle += MIN_SWEEP_ANGLE;
//            Log.i(TAG, "mode appearing");
//        	Log.i(TAG, "startAngle:"+startAngle);
//        	Log.i(TAG, "sweepAngle:"+sweepAngle);
        }
        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {//设置透明度
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {//滤镜
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {//不透明度
        return PixelFormat.TRANSPARENT;
    }
    
    /**
     * 切换显示模式-首进/尾退
     */
    private void toggleAppearingMode() {
        mModeAppearing = !mModeAppearing;
        if (mModeAppearing) {
        	//进度增长	首部加速
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        fBounds.left = bounds.left + mBorderWidth / 2f + .5f;
        fBounds.right = bounds.right - mBorderWidth / 2f - .5f;
        fBounds.top = bounds.top + mBorderWidth / 2f + .5f;
        fBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;
    }

    //对象属性-角度
    private Property<CircularAnimatedDrawable, Float> mAngleProperty  =
            new Property<CircularAnimatedDrawable, Float>(Float.class, "angle") {
    	//动画过程中调用
        @Override
        public Float get(CircularAnimatedDrawable object) {
            return object.getCurrentGlobalAngle();
        }

        @Override
        public void set(CircularAnimatedDrawable object, Float value) {
            object.setCurrentGlobalAngle(value);
        }
    };
    
    //对象属性-弧度
    private Property<CircularAnimatedDrawable, Float> mSweepProperty
            = new Property<CircularAnimatedDrawable, Float>(Float.class, "arc") {
    	//动画过程中调用
        @Override
        public Float get(CircularAnimatedDrawable object) {
            return object.getCurrentSweepAngle();
        }

        @Override
        public void set(CircularAnimatedDrawable object, Float value) {
            object.setCurrentSweepAngle(value);
        }
    };

    private void setupAnimations() {
    	//角度动画	//传递对象属性后，其中的set/get方法会被调用，从而触发两个角度的变化设置
        mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty, 360f);
        mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        mObjectAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
        mObjectAnimatorAngle.setRepeatMode(ValueAnimator.RESTART);	//重复模式-重启
        mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);//无限重复
        
        //弧度动画
        mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, mSweepProperty, 360f - MIN_SWEEP_ANGLE * 2);
        mObjectAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        mObjectAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
        mObjectAnimatorSweep.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimatorSweep.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                toggleAppearingMode();
            }
        });
    }

    @Override
    public void start() {//impl->Animatable
        if (isRunning()) {
            return;
        }
        mRunning = true;
        mObjectAnimatorAngle.start();
        mObjectAnimatorSweep.start();
        invalidateSelf();	//####←_←####大概是这个触发draw()进行绘制
    }

    @Override
    public void stop() {//impl->Animatable
        if (!isRunning()) {
            return;
        }
        mRunning = false;
        mObjectAnimatorAngle.cancel();
        mObjectAnimatorSweep.cancel();
        invalidateSelf();
    }

    @Override
    public boolean isRunning() {//impl->Animatable
        return mRunning;
    }

    /* ---- setter & getter ---- 
     * 在Animator计算过程中调用，触发在属性对象Property中
     * */
    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        mCurrentGlobalAngle = currentGlobalAngle;
        invalidateSelf();
    }

    public float getCurrentGlobalAngle() {
        return mCurrentGlobalAngle;
    }

    public void setCurrentSweepAngle(float currentSweepAngle) {
        mCurrentSweepAngle = currentSweepAngle;
        invalidateSelf();
    }

    public float getCurrentSweepAngle() {
        return mCurrentSweepAngle;
    }

}
