package test1.nh.com.demos1.activities.hybrid_test;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
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
    public void showToast(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    @android.webkit.JavascriptInterface
    public void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    @android.webkit.JavascriptInterface
    public String LoadPreferences(String key){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sharedPreferences.getString(key, "");
    }

    @android.webkit.JavascriptInterface
    public void Vibrate(long milliseconds){
        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(milliseconds);
    }

    @android.webkit.JavascriptInterface
    public void SendSMS(String phoneNumber, String message){
        Toast.makeText(mContext, "to:"+phoneNumber+"  msg:"+message, Toast.LENGTH_LONG).show();
    }


}
