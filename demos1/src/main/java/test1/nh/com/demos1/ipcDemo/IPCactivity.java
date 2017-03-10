package test1.nh.com.demos1.ipcDemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.ipcDemo.aidl.IMyAidlInterface;
import test1.nh.com.demos1.ipcDemo.aidl.IPCservice2;
import test1.nh.com.demos1.utils.OsUtils;

public class IPCactivity extends AppCompatActivity {

    public static void start(Context context){
        Intent i1=new Intent(context,IPCactivity.class);
        context.startActivity(i1);
    }


    private WorkerBinder mBinder;
    private ServiceConnection conn=new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder=(WorkerBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    private IMyAidlInterface mBinder2;
    private ServiceConnection conn2=new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder2= IMyAidlInterface.Stub.asInterface(service)  ;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipcactivity);
        bindService(new Intent(this,IPCservice.class),conn,BIND_AUTO_CREATE);
        bindService(new Intent(this,IPCservice2.class),conn2,BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        unbindService(conn2);
    }


    public void showUser(View view){
        if(mBinder!=null){
            String str=mBinder.getUser(10);
            Log.i("ipc","show user---"+str);
        }
        String processName = OsUtils.getProcessName(this.getApplicationContext(), android.os.Process.myPid());
        Log.i("ipc", "activity onclick in:" + processName + "   process ID:" + android.os.Process.myPid());
    }


    public void showUser2(View view){
        if(mBinder2!=null){
            String str= null;
            try {
                str = mBinder2.getUser(10);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.i("ipc","show user---"+str);
        }
        String processName = OsUtils.getProcessName(this.getApplicationContext(), android.os.Process.myPid());
        Log.i("ipc", "activity onclick in:" + processName + "   process ID:" + android.os.Process.myPid());
    }

}
