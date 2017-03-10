package test1.nh.com.demos1.activities.range_seek;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import test1.nh.com.demos1.R;

public class RangeSeekbarMainActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,RangeSeekbarMainActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_seekbar_main);

        RangeSeekBar seekbar= (RangeSeekBar) findViewById(R.id.seekbar);
        seekbar.setRangeValues(3000,9000);
        seekbar.setSelectedMinValue(3000);
        seekbar.setSelectedMaxValue(9000);
        seekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                int min=(Integer)minValue;
                int max=(Integer)maxValue;
                Log.i("CCC",""+min+"---"+max);
            }
        });

    }
}
