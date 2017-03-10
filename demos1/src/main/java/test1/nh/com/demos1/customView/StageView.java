package test1.nh.com.demos1.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import test1.nh.com.demos1.R;


/**
 * Created by Administrator on 2016/11/26.
 */

public class StageView extends RelativeLayout {


    public StageView(Context context) {
        super(context);
        init(context,null);
    }

    public StageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public StageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }


    LayoutInflater mInflater;



    private void init(Context context,AttributeSet attrs){
        mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.stage_view_lo, this, true);

        if (attrs!=null){
            TypedArray a = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.StageView
            );
            upperText = a.getString(R.styleable.StageView_upperText);
            lowerText = a.getString(R.styleable.StageView_lowerText);
            selected= a.getBoolean(R.styleable.StageView_selected,false);
            a.recycle();
        }

        upper= (TextView) v.findViewById(R.id.top_text);
        upper.setText(upperText);

        lower= (TextView) v.findViewById(R.id.bottom_text);
        if (lowerText!=null){
            lower.setText(lowerText);
        } else{
            lower.setVisibility(View.INVISIBLE);
        }

        centerDot= (ImageView) v.findViewById(R.id.center_dot);
        left=(ImageView) v.findViewById(R.id.left_line);
        right=(ImageView) v.findViewById(R.id.right_line);
        indicator= (TriangleView) v.findViewById(R.id.triangle);

        setSelected(selected);
    }


    private TextView upper,lower;
    private String upperText="",lowerText;
    private boolean selected=false;

    private ImageView centerDot,left,right;
    private TriangleView indicator;

    public void hideLeft(){
        left.setVisibility(View.INVISIBLE);
    }
    public void hideRight(){
        right.setVisibility(View.INVISIBLE);
    }

    public void changeLowerText(String text){
        lower.setText(text);
    }
    public void changeLowerText(int textRes){
        lower.setText(textRes);
    }

    public void changeUpperText(String text){
        upper.setText(text);
    }

    public void changeUpperText(int textRes){
        upper.setText(textRes);
    }

    public void setSelected(boolean selected){
        this.selected=selected;
        if(selected){
            centerDot.setImageResource(R.drawable.circle_img);
            upper.setBackgroundResource(R.drawable.corner_drawable_orange);
            upper.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
            indicator.setVisibility(View.VISIBLE);
        } else{
            centerDot.setImageResource(R.drawable.circle_gray);
            upper.setBackgroundResource(R.color.color_white);
            upper.setTextColor(ContextCompat.getColor(getContext(), R.color.Gray300));
            indicator.setVisibility(View.INVISIBLE);
        }
    }



}
