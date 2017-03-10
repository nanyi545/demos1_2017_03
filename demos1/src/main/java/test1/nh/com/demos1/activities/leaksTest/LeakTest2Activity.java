package test1.nh.com.demos1.activities.leaksTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import test1.nh.com.demos1.R;

public class LeakTest2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak_test2);
        Button b= (Button) findViewById(R.id.start_leaky);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeakActivity2.start(LeakTest2Activity.this);
            }
        });
    }

}
