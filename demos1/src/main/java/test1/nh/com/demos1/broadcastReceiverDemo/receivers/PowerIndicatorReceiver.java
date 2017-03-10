package test1.nh.com.demos1.broadcastReceiverDemo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import test1.nh.com.demos1.ipcDemo.aidl.IPCservice2;

public class PowerIndicatorReceiver extends BroadcastReceiver {


    public PowerIndicatorReceiver() {
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            Intent i1 = new Intent(context, IPCservice2.class);
            context.startService(i1);
        }
        if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            Intent i1 = new Intent(context, IPCservice2.class);
            context.stopService(i1);
        }
    }
}
