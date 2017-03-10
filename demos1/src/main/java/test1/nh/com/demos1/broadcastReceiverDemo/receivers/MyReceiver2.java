package test1.nh.com.demos1.broadcastReceiverDemo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver2 extends BroadcastReceiver {

    public MyReceiver2() {
        Log.i("AAA","receiver2 created in"+Thread.currentThread().getName());  // always in main...
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String contextString=context.toString();
        String message=intent.getStringExtra("msg");
        Log.i("AAA","receiver2 onReceive() in:"+Thread.currentThread().getName());
        Toast.makeText(context,"dynamic registration:"+contextString+"-"+message,Toast.LENGTH_SHORT).show();
    }
}
