package test1.nh.com.demos1.testDemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import test1.nh.com.demos1.R;
import test1.nh.com.demos1.testDemo.models.ItestData;
import test1.nh.com.demos1.utils.DMapplication;

public class UnitTestWithDIActivity extends AppCompatActivity {

    @Inject
    ItestData myTestData;  // myTestData

    public static void start(Context context) {
        final Intent intent = new Intent(context, UnitTestWithDIActivity.class);
        context.startActivity(intent);
    }

    @Bind(R.id.unit_tv1)
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_test_with_di);
        ButterKnife.bind(this);
        DMapplication.getInstance().getDataComponent().inject(this);

        tv1.setText(myTestData.obtainTestString(""));
    }
}
