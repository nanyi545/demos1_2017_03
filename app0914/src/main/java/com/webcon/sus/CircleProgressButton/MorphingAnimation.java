package com.webcon.sus.CircleProgressButton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

/**
 * 按钮变形动画
 * <p>方形<-->圆形互换，以及边界的变形
 */
public class MorphingAnimation {

    public static final int DURATION_NORMAL = 400;
    public static final int DURATION_INSTANT = 1;

    private OnAnimationEndListener mListener;
    
    /* 延续时间 */
    private int mDuration;
    
    /* 宽度变化 */
    private int mFromWidth;
    private int mToWidth;

    /* 颜色变化 */
    private int mFromColor;
    private int mToColor;

    /* 边界笔画变化 */
    private int mFromStrokeColor;
    private int mToStrokeColor;

    /* 边角角度变化 */
    private float mFromCornerRadius;
    private float mToCornerRadius;

    private float mPadding;

    private TextView mView;
    private StrokeGradientDrawable mDrawable;

    /**
     * 构造器初始化
     * @param viewGroup	textView
     * @param drawable	边界渐变绘图
     */
    public MorphingAnimation(TextView viewGroup, StrokeGradientDrawable drawable) {
        mView = viewGroup;
        mDrawable = drawable;
    }
    
    /* ---- setter & getter ---- */
    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setListener(OnAnimationEndListener listener) {
        mListener = listener;
    }

    public void setFromWidth(int fromWidth) {
        mFromWidth = fromWidth;
    }

    public void setToWidth(int toWidth) {
        mToWidth = toWidth;
    }

    public void setFromColor(int fromColor) {
        mFromColor = fromColor;
    }

    public void setToColor(int toColor) {
        mToColor = toColor;
    }

    public void setFromStrokeColor(int fromStrokeColor) {
        mFromStrokeColor = fromStrokeColor;
    }

    public void setToStrokeColor(int toStrokeColor) {
        mToStrokeColor = toStrokeColor;
    }

    public void setFromCornerRadius(float fromCornerRadius) {
        mFromCornerRadius = fromCornerRadius;
    }

    public void setToCornerRadius(float toCornerRadius) {
        mToCornerRadius = toCornerRadius;
    }

    public void setPadding(float padding) {
        mPadding = padding;
    }

    /**
     * 开始动画
     */
    public void start() {
    	//①用于计算属性值-宽度值的变化
        ValueAnimator widthAnimation = ValueAnimator.ofInt(mFromWidth, mToWidth);
        //获取边界渐变绘图对象
        final GradientDrawable gradientDrawable = mDrawable.getGradientDrawable();
        //②根据属性值执行相应的动作-改变对象的宽度
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();	//得到当前的属性值
                int leftOffset;
                int rightOffset;
                int padding;

                if (mFromWidth > mToWidth) {
                    leftOffset = (mFromWidth - value) / 2;
                    rightOffset = mFromWidth - leftOffset;
                  //getAnimatedFraction()返回当前动画完成的百分比
                    padding = (int) (mPadding * animation.getAnimatedFraction());
                } else {
                    leftOffset = (mToWidth - value) / 2;
                    rightOffset = mToWidth - leftOffset;
                    padding = (int) (mPadding - mPadding * animation.getAnimatedFraction());
                }
                
                //设置绘制的矩形区域
                gradientDrawable
                        .setBounds(leftOffset + padding, padding, rightOffset - padding, mView.getHeight() - padding);
            }
        });
        
        //继承自ValueAnimator，指定一个对象及该对象的一个属性，当属性值计算完成时自动设置为该对象的相应属性 
        //背景色渐变动画
        ObjectAnimator bgColorAnimation = ObjectAnimator.ofInt(gradientDrawable, "color", mFromColor, mToColor);
        //设置求值器
        bgColorAnimation.setEvaluator(new ArgbEvaluator());
        
        //边界颜色渐变动画
        ObjectAnimator strokeColorAnimation =
                ObjectAnimator.ofInt(mDrawable, "strokeColor", mFromStrokeColor, mToStrokeColor);
        strokeColorAnimation.setEvaluator(new ArgbEvaluator());
        
        //角度变化动画
        ObjectAnimator cornerAnimation =
                ObjectAnimator.ofFloat(gradientDrawable, "cornerRadius", mFromCornerRadius, mToCornerRadius);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mDuration);
        animatorSet.playTogether(widthAnimation, bgColorAnimation, strokeColorAnimation, cornerAnimation);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }
}
