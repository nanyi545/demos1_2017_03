package com.nanyi545.www.materialdemo;

import android.app.Application;
import android.util.Log;


/**
 * Created by Administrator on 2017/3/2.
 */
public class MyApp extends Application {

    private static final String TAG=MyApp.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"BuildConfig.TMD:"+BuildConfig.TMD+"  BuildConfig.PF:"+BuildConfig.PF);

    }

}
