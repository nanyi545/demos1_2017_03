package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanyi545.www.materialdemo.R;

/**
 *  this is a default implementation of RevealContent that contains only a text view
 */
public class RevealContentImp extends RevealContent{

    TextView indicator;

    public RevealContentImp(Context context) {
        super(context);
    }

    public RevealContentImp(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RevealContentImp(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    void setProgress(float progress) {
        if (indicator!=null)
        indicator.setText(""+progress);
        Log.i("ddd",""+progress);
    }

    @Override
    int getContentId() {
        return R.layout.reveal_content_lo;
    }

    @Override
    int getViewHeight() {
        return  (int) getContext().getResources().getDimension(R.dimen.reveal_height_imp1);
    }

    @Override
    void setUpInternal(View v) {
        indicator= (TextView) v.findViewById(R.id.reveal_txt);
    }

}
