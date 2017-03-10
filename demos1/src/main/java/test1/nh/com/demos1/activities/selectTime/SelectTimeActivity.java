package test1.nh.com.demos1.activities.selectTime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;

import test1.nh.com.demos1.R;

public class SelectTimeActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,SelectTimeActivity.class);
        c.startActivity(i);
    }


    TimeSelector selector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
        selector=new TimeSelector(this, findViewById(R.id.container));


        NumberPicker mNumberPicker = (NumberPicker) findViewById(R.id.show_num_picker);
        mNumberPicker.setMaxValue(23);
        mNumberPicker.setMinValue(0);
        mNumberPicker.setValue(10);



    }


    public void selectTime(View v){
        selector.show();
    }

}
