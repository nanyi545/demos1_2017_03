package test1.nh.com.demos1.broadcastReceiverDemo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver1 extends BroadcastReceiver {

    public MyReceiver1() {
        Log.i("AAA","receiver1 created in"+Thread.currentThread().getName());
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String contextString=context.toString();
        String message=intent.getStringExtra("msg");
        Log.i("AAA","receiver1 onReceive() in:"+Thread.currentThread().getName());
        Toast.makeText(context,"static registration:"+contextString+"-"+message,Toast.LENGTH_SHORT).show();
    }
}
