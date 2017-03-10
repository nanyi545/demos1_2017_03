package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test1.nh.com.demos1.R;

public class ButterKnifeActivity extends AppCompatActivity {

    public static Intent createQuery(Context context, String query, String value) {
        // Reuse MainActivity for simplification
        Intent i = new Intent(context, ButterKnifeActivity.class);
        i.putExtra("QUERY", query);
        i.putExtra("VALUE", value);
        return i;
    }


    public static void start(Context context) {
        final Intent intent = new Intent(context, ButterKnifeActivity.class);
        context.startActivity(intent);
    }

    @Bind(R.id.bk_iv1) ImageView iv1;
    @OnClick(R.id.bk_iv1)
    void cangeAlpha(){
        if (iv1.getAlpha()>0.5f) iv1.setAlpha(0.1f);
        else iv1.setAlpha(0.9f);
    }

    @BindString(R.string.text1_activity_butterKnife)
    String butterKnifeText1;
    @Bind(R.id.bk_tv1) TextView tv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife);
        ButterKnife.bind(this);
        iv1.setImageResource(R.drawable.bknife);
        tv1.setText(butterKnifeText1);
    }
}
