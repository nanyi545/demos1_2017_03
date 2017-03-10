package test1.nh.com.demos1.activityManagerActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import test1.nh.com.demos1.R;

public class ActManagerActivity extends Activity {

    public static void start(Context context){
        Intent intent=new Intent(context,ActManagerActivity.class);
        context.startActivity(intent);
    }

    @Bind(R.id.tv1_manager) TextView tv1;


    //  !!!   System services not available to Activities before onCreate()
    ActivityManager manager ;


    int currentCount;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);// this is needed to make sure views' states are preserved !!
        Log.i("AAA","--on--save state---");
        currentCount++;
        outState.putInt("test",currentCount);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);// this is needed to make sure views' states are preserved !!
        Log.i("AAA","--on--restore---");
        int whatUget=savedInstanceState.getInt("test");
        currentCount=whatUget;
        Log.i("AAA","integer From Bundle:"+whatUget);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_manager);
        ButterKnife.bind(this);
        Log.i("AAA","--create--");

        showRunningApps();

    }

    // only 1 running app????? why ????
    private void showRunningApps() {
        manager= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppList=manager.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> iteraor=runningAppList.iterator();
        StringBuilder sb_names=new StringBuilder();
        while(iteraor.hasNext()){
            ActivityManager.RunningAppProcessInfo tempProcess=iteraor.next();
            Log.i("CCC",""+tempProcess.processName);
            sb_names.append(tempProcess.processName+"\n");
        }
        tv1.setText(sb_names.toString());

        tv1.setSaveEnabled(false);//**
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("AAA","--onstart--");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("AAA","--restart--");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("AAA","--resume--");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AAA","--pause--");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("AAA","--stop--");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("AAA","--destroy--");
    }
}
