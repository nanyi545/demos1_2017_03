package test1.nh.com.demos1.activities.tracking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.tracking.tracking_utils.LogUserAction;

public class TrackingHomeActivity extends TrackingActivity {

    public static void start(Context c){
        Intent i=new Intent(c,TrackingHomeActivity.class);
        c.startActivity(i);
    }


    @Override
    String getActivityName() {
        return "home";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_home);


    }


    public void click(View v){
        int id=v.getId();
        switch(id){
            case R.id.trak_btn1 :
                manager.addTrackingLog(new LogUserAction(getActivityName()+"click btn1"));
                break;
            case R.id.trak_btn2 :
                manager.addTrackingLog(new LogUserAction(getActivityName()+"click btn1"));
                Track1Activity.start(TrackingHomeActivity.this);
                break;


        }
    }



}
