package test1.nh.com.demos1.fundamental.hook.fox_ams;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class RealActivity extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"this is the real activity---not declared",Toast.LENGTH_SHORT).show();
    }








}
