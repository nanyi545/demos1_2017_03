package test1.nh.com.demos1.activities.tracking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.tracking.tracking_utils.LogUserAction;

public class Track1Activity extends TrackingActivity {


    public static void start(Context c){
        Intent i=new Intent(c,Track1Activity.class);
        c.startActivity(i);
    }



    @Override
    String getActivityName() {
        return "页面1";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track1);
    }

    TextView nullTv;


    public void click(View v){
        int id=v.getId();
        switch(id){
            case R.id.track1_btn1 :
                manager.addTrackingLog(new LogUserAction(getActivityName()+"click return"));
                Track1Activity.this.finish();
                break;
            case R.id.track1_btn2 :
                manager.addTrackingLog(new LogUserAction(getActivityName()+"click NPE"));
                nullTv.setText("here is an error");
                break;

        }
    }



}
