package com.webcon.sus.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * 监控视频控制的按钮
 * @author Vieboo
 *
 */
public class CCImageButton extends ImageButton {
	
	public CCImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public CCImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CCImageButton(Context context) {
		super(context);
	}

	
	protected boolean isTouchPointInView(float localX, float localY){
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		draw(canvas);
		int x = (int)localX;
		int y = (int)localY;
		if(x < 0 || x >= getWidth()){
			return false;
		}
		if(y < 0 || y >= getHeight()){
			return false;
		}
		int pixel = bitmap.getPixel(x, y);
		if((pixel & 0xff000000) != 0){
			return true;
		}else{
			return false;
		}
	}
}
