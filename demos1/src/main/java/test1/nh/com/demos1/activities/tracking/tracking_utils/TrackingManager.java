package test1.nh.com.demos1.activities.tracking.tracking_utils;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import test1.nh.com.demos1.utils.DMapplication;

/**
 * Created by Administrator on 2017/5/21.
 */

public class TrackingManager {

    private TrackingDB db;
    private Executor executor;

    private static TrackingManager instance;


    public static TrackingManager getInstance(Context c,Executor executor){
        if (instance==null){
            instance = new TrackingManager(c,executor);
            return instance;
        } else {
            return instance;
        }
    }

    public static TrackingManager getInstance(Context c){
        return getInstance(c,Executors.newSingleThreadExecutor());
    }





    /**
     * if you do not use app-wide executor in your app, create an executor for TrackingManager
     * @param c
     */
    private TrackingManager(Context c) {
        db=TrackingDB.getInstance(c);
        executor= Executors.newSingleThreadExecutor();
    }

    /**
     * if you do use app-wide executor in your app, pass this executor in to do background for TrackingManager
     * @param c
     */
    private TrackingManager(Context c,Executor executor) {
        db=TrackingDB.getInstance(c);
        this.executor = executor;
    }



    public void uploadPreviousSession(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                String json=db.restorePrevSession();
                Log.i(DMapplication.DEM0_APP,"upload pre-session:"+Thread.currentThread().getName()+"\n"+json);
            }
        });
    }



    public void addTrackingLog(final LogItem log){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.insertContact(log);
            }
        });
    }





}
