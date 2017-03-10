package com.webcon.sus.CircleProgressButton;

import android.graphics.drawable.GradientDrawable;

/**
 * 边界渐变绘图
 * <p>针对GradientDrawable对象的简单的包装类
 */
public class StrokeGradientDrawable {

    private int mStrokeWidth;	//边界线宽
    private int mStrokeColor;	//边界颜色

    private GradientDrawable mGradientDrawable;	//GradientDrawable对象
    
    /**
     * 获取GradientDrawable对象
     * @param drawable GradientDrawable对象
     */
    public StrokeGradientDrawable(GradientDrawable drawable) {
        mGradientDrawable = drawable;
    }
    
    /* ----getter & setter---- */
    /**
     * 获取边界宽
     * @return
     */
    public int getStrokeWidth() {
        return mStrokeWidth;
    }
    
    /**
     * 设置边界宽
     * <p>同时设置GradientDrawable对象的边界
     * @param strokeWidth
     */
    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        mGradientDrawable.setStroke(strokeWidth, getStrokeColor());
    }
    
    /**
     * 获取颜色
     * @return
     */
    public int getStrokeColor() {
        return mStrokeColor;
    }
    
    /**
     * 设置颜色
     * <p>同时设置GradientDrawable对象的颜色
     * @param strokeColor
     */
    public void setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        mGradientDrawable.setStroke(getStrokeWidth(), strokeColor);
    }
    
    /**
     * 返回GradientDrawable对象
     * @return
     */
    public GradientDrawable getGradientDrawable() {
        return mGradientDrawable;
    }
}
