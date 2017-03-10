package test1.nh.com.demos1.launchMode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import test1.nh.com.demos1.R;

public class DefaultLaunchActivity extends AppCompatActivity {

    public static void start(Context context){
        Intent i1=new Intent(context,DefaultLaunchActivity.class);
        context.startActivity(i1);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_launch);
    }
}
