package com.nanyi545.www.hybridtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/25.
 */
public class JSInterface {

    Context mContext;
    JSInterface(Context c){
        mContext = c;
    }

    @android.webkit.JavascriptInterface
    public void log(String message){
        Log.i("JS_LOG",message);
    }



}
