package test1.nh.com.demos1.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 15-12-15.
 */
public class ExpandView extends RelativeLayout {

    public static final int EXPAND_STATE=1;  // view is expand
    public static final int CONTRACT_STATE=0;// view is closed

    public int state=EXPAND_STATE;

    public ExpandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundResource(R.drawable.corner_bg);
    }


}
