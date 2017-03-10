package test1.nh.com.demos1.activities.leaksTest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import test1.nh.com.demos1.R;

public class LeakActivity2 extends AppCompatActivity {

    public static ArrayList<View> StaticViews;
    public static ArrayList<String> StaticString;

    private int[] leak_int_array=new int[1024*1024*2];


    public static void start(Context c){
        Intent i=new Intent(c,LeakActivity2.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak2);

//        leakBySetStaticView();  //  this will cause leak !!!!
        leakBySetString();        //  this will not cause leak !!!

    }

    // this method will leak the activity since View will hold a reference to the context(LeakActivity2 instance)
    private void leakBySetStaticView() {
        if (StaticViews==null){
            StaticViews=new ArrayList<>();
        } else {
            StaticViews.add(findViewById(R.id.leaky_image));
        }
    }

    // this method will NOT !!!  leak the activity since String(LeakActivity2.this.toString()) will NOT hold a reference to the context(LeakActivity2 instance)
    private void leakBySetString() {
        if (StaticString==null){
            StaticString=new ArrayList<>();
        } else {
            StaticString.add(LeakActivity2.this.toString());
            Log.i("BBB",""+StaticString);
        }
    }



}
