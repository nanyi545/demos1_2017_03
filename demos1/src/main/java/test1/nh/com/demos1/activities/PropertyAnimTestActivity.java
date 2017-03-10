package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import test1.nh.com.demos1.R;

public class PropertyAnimTestActivity extends AppCompatActivity {

    public static void start(Context context) {
        final Intent intent = new Intent(context, PropertyAnimTestActivity.class);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_anim_test);
    }



}
