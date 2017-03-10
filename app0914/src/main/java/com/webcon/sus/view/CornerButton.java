package com.webcon.sus.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.webcon.sus.demo.R;


/**
 * 圆角Button
 * @author Vieboo
 *
 */
public class CornerButton extends Button {

	public CornerButton(Context context) {
		super(context);
	}
	
	
	public CornerButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CornerButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		if(focused){
			this.setTextColor(Color.parseColor("#ffffff"));
			this.setBackgroundResource(R.drawable.bg_xml_menubtn_b);
		}else{
			this.setTextColor(Color.parseColor("#333333"));
			this.setBackgroundResource(R.drawable.bg_xml_menubtn_a);
		}
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_MOVE:
			this.setTextColor(Color.parseColor("#ffffff"));
			this.setBackgroundResource(R.drawable.bg_xml_menubtn_b);
			break;
		case MotionEvent.ACTION_UP:
			this.setTextColor(Color.parseColor("#333333"));
			this.setBackgroundResource(R.drawable.bg_xml_menubtn_a);
			break;
		case MotionEvent.ACTION_DOWN:
			this.setTextColor(Color.parseColor("#ffffff"));
			this.setBackgroundResource(R.drawable.bg_xml_menubtn_b);
			break;
		default:
			this.setTextColor(Color.parseColor("#333333"));
			this.setBackgroundResource(R.drawable.bg_xml_menubtn_a);
			break;
		}
		return super.onTouchEvent(event);
	}
}
