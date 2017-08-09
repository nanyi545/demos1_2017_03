package test1.nh.com.demos1.activities.tracking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.tracking.tracking_utils.LogItem;
import test1.nh.com.demos1.activities.tracking.tracking_utils.LogUserAction;
import test1.nh.com.demos1.activities.tracking.tracking_utils.LogView;
import test1.nh.com.demos1.activities.tracking.tracking_utils.TrackingManager;

public abstract class TrackingActivity extends AppCompatActivity {



    abstract String getActivityName();


    TrackingManager manager;
    LogItem activityLog;

    public TrackingManager getManager() {
        return manager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        manager=TrackingManager.getInstance(this);
        activityLog = new LogView(getActivityName());
    }


    @Override
    protected void onPause() {
        super.onPause();
        activityLog.setEndTime();
        manager.addTrackingLog(activityLog);
    }





}
