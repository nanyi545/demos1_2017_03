package test1.nh.com.demos1.activities.cyclic_galary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 2016/11/8.
 */

public class ScrollMenuItem extends RelativeLayout {

    public ScrollMenuItem(Context context) {
        super(context);
        init(context,null);
    }

    public ScrollMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ScrollMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    ImageView iv;
    TextView tv;

    LayoutInflater mInflater;

    public void resize(int width,int height){
        getLayoutParams().height= height;
        getLayoutParams().width= width;
        invalidate();
    }


    int imageRes;
    String title;

    private void init(Context c,AttributeSet attrs){
        if (attrs!=null){
            TypedArray a = c.obtainStyledAttributes(
                    attrs,
                    R.styleable.ScrollMenuItem
            );
            imageRes = a.getResourceId(R.styleable.ScrollMenuItem_imgId, R.drawable.ic_account_balance_black_24dp);
            title = a.getString(R.styleable.ScrollMenuItem_title);
        }

        mInflater = LayoutInflater.from(c);
        View v = mInflater.inflate(R.layout.menu_item_lo, this, true);
        iv= (ImageView) v.findViewById(R.id.item_icon);
        iv.setImageResource(imageRes);
        tv= (TextView) v.findViewById(R.id.item_text);
        tv.setText(title);

    }



}
