package test1.nh.com.demos1.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 15-12-12.
 */
public class MyView3 extends RelativeLayout {

    private TextView tv1;
    private ImageView iv1;

    private String tvText;
    private int textBgColor;
    private int imageRes;

    private LayoutParams tvpos;
    private LayoutParams ivpos;

    public MyView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        // ----------what is the difference between the 2 methods below?--------
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.MyView3
        );
//        TypedArray a = context.getTheme().obtainStyledAttributes(
//                attrs,
//                R.styleable.MyView3,
//                0, 0
//        );
        try {
            tvText = a.getString(R.styleable.MyView3_textContent);
            textBgColor = a.getColor(R.styleable.MyView3_textBackground, 0xffffffff);
            imageRes = a.getResourceId(R.styleable.MyView3_imageSource, R.drawable.ic_account_balance_black_24dp);
        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }

        tv1=new TextView(context);
        tv1.setText(tvText);
        tv1.setBackgroundColor(textBgColor);
        tvpos=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        tvpos.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(tv1,tvpos);

        iv1=new ImageView(context);
        iv1.setBackgroundResource(imageRes);
        ivpos=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        ivpos.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(iv1,ivpos);
    }

    public MyView3(Context context) {
        super(context);
    }


}
