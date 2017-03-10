package test1.nh.com.demos1.activities.time_picker;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.widget.TextView;

/**
 * Created by Administrator on 16-7-17.
 */
public class TextViewUtil {

    public static void appendWithSqure (TextView tv){
        String str=tv.getText().toString();
        SpannableString msp = new SpannableString(str+"2");
        final int textLength=str.length();
        msp.setSpan(new SuperscriptSpan(), textLength, textLength+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new RelativeSizeSpan(0.6f), textLength, textLength+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(msp);
    }


}
