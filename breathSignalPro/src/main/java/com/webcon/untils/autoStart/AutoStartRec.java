package com.webcon.untils.autoStart;

import android.app.LauncherActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.webcon.breath.freescale.ui.MainActivity;

public class AutoStartRec extends BroadcastReceiver {
    public AutoStartRec() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
            final Context mContext=context;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent i=new Intent(mContext, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   // Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag
                    i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);  // include stopped packages..
                    mContext.startActivity(i);
                }
            }).start();
        }
    }
}
