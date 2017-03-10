package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.customView.LoadView;

public class CustomViewActivity2 extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,CustomViewActivity2.class);
        c.startActivity(i);
    }


    DisplayMetrics dm=new DisplayMetrics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view2);
    }

    public void startLoad(View v){
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        Log.i("ccc",""+dm.density+"    "+(75*dm.density));
        LoadView.startLoading2(this);
    }
}
