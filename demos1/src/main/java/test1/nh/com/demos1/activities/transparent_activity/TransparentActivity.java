package test1.nh.com.demos1.activities.transparent_activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import test1.nh.com.demos1.R;

public class TransparentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
    }

    public void goBack(View v){
        this.finish();
    }

}
