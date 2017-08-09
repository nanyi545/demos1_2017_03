package test1.nh.com.demos1.fundamental.hook.fox_ams;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class StubActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"this is the stub activity-- declared in manifest",Toast.LENGTH_SHORT).show();
    }


}
