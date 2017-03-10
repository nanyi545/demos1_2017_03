package test1.nh.com.demos1.broadcastReceiverDemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.broadcastReceiverDemo.receivers.MyReceiver2;

public class BCRActivity extends AppCompatActivity {

    public static void start(Context context){
        Intent i1=new Intent(context,BCRActivity.class);
        context.startActivity(i1);
    }


    public void button1(View view){
        Intent intent = new Intent();
        intent.setAction("com.demo.receiver.intent1");
        intent.putExtra("msg", "receiver.intent1");
        sendBroadcast(intent);
    }

    public void button2(View view){
        Intent intent = new Intent();
        intent.setAction("com.demo.receiver.intent2");
        intent.putExtra("msg", "receiver.intent2");
        sendBroadcast(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bcr);


        // dynamic registering
        IntentFilter filter=new IntentFilter("com.demo.receiver.intent2");
        BroadcastReceiver receiver=new MyReceiver2();
        registerReceiver(receiver,filter);


    }
}
