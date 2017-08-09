package test1.nh.com.demos1.activities.tracking.tracking_utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
                UploaderPact pact=db.restorePrevSession();
                Log.i(DMapplication.DEM0_APP,"upload pre-session:"+Thread.currentThread().getName()+"\n"+pact.toString());


                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                RequestBody body = RequestBody.create(JSON, pact.toString());

                Request request = new Request.Builder()
                        .url("http://nanyi5452.pythonanywhere.com/polls/api/log_post/")
                        .post(body)  // post type
                        .build();

                OkHttpClient client = new OkHttpClient();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String retStr = "";
                try {
                    retStr = response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(DMapplication.DEM0_APP,"upload pre-session  ret:"+retStr);
            }
        });
    }

    protected  static class UploaderPact{
        private long start_time,end_time;
        private LogSession log_json;
        private int device_type;
        private String device_info;

        public UploaderPact(long[] duration, LogSession session, int deviceType, String deviceInfo) {
            this.start_time = duration[0];
            this.end_time=duration[1];
            this.log_json = session;
            this.device_type = deviceType;
            this.device_info = deviceInfo;
        }

        @Override
        public String toString() {
            Gson gson=new Gson();
            return gson.toJson(UploaderPact.this);
        }
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
