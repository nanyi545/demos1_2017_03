package test1.nh.com.demos1.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import test1.nh.com.demos1.activities.MainActivity_from;

/**
 * Created by Administrator on 15-12-7.
 */
public class AutoStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AAA","onReceive");
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
            Log.i("AAA", "onReceive boot");

            final Context mContext=context;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent i=new Intent(mContext, MainActivity_from.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   // Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag
                    i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);  // include stopped packages..
                    mContext.startActivity(i);
                }
            }).start();
        }
    }
}
