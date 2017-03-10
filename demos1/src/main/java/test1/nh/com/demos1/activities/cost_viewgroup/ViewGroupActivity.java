package test1.nh.com.demos1.activities.cost_viewgroup;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import test1.nh.com.demos1.R;

public class ViewGroupActivity extends AppCompatActivity {



    public static void start(Context c){
        Intent i =new Intent(c,ViewGroupActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);
    }
}
