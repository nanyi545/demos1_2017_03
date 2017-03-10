package test1.nh.com.demos1.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.ButterKnifeActivity;

/**
 * Created by Administrator on 16-1-10.
 */
public class ButterKnifeActivitTest1 extends ActivityInstrumentationTestCase2<ButterKnifeActivity>{

    private ButterKnifeActivity mButterKnifeActivity;
    private TextView mTestText;

    public ButterKnifeActivitTest1() {
        super(ButterKnifeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mButterKnifeActivity = getActivity();
        mTestText = (TextView) mButterKnifeActivity.findViewById(R.id.bk_tv1);
    }


    public void testMyFirstTestTextView_labelText() {
        final String expected = mButterKnifeActivity.getString(R.string.text1_activity_butterKnife);
        final String actual = mTestText.getText().toString();
        assertEquals( expected, actual);
    }


    public void testMyFirstTestTextView2_labelText() {
        final String expected = mButterKnifeActivity.getString(R.string.text1_activity_butterKnife);
        final String actual = "what the fuck";
        assertEquals( expected, actual);
    }



}
