package com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.webcon.sus.demo.R;
import com.webcon.wp.utils.NativeInterface;

public class JNItestActivity extends AppCompatActivity {

    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jnitest);

        tv1= (TextView) findViewById(R.id.tv1);

        int nret = NativeInterface.getInstance().init();
        Log.i("native","============== init > nret " + nret);
        tv1.setText(("============== init > nret " + nret));

    }


}
