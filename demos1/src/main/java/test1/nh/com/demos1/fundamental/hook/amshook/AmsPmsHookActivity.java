package test1.nh.com.demos1.fundamental.hook.amshook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.LoadingTestActivity;

public class AmsPmsHookActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ams_pms_hook);
    }

    // 这个方法比onCreate调用早; 在这里Hook比较好.
    @Override
    protected void attachBaseContext(Context newBase) {
        HookHelper.hookActivityManager();
        HookHelper.hookPackageManager(newBase);
        super.attachBaseContext(newBase);
    }


    public void click(View v){
        int id=v.getId();
        switch(id){
            case R.id.ams_hook:
                LoadingTestActivity.start(AmsPmsHookActivity.this);
                break;
            case R.id.pms_hook:
                getPackageManager().getInstalledApplications(0);
                break;
        }
    }

}
