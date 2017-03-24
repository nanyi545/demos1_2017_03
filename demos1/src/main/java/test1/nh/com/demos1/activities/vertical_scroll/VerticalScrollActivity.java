package test1.nh.com.demos1.activities.vertical_scroll;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import test1.nh.com.demos1.R;

public class VerticalScrollActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,VerticalScrollActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_scroll);
    }


}
