package test1.nh.com.demos1.ipcDemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import test1.nh.com.demos1.utils.OsUtils;

/**
 * Created by Administrator on 16-4-10.
 */
public class IPCservice extends Service {




    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ipc","ipc service created!");
        String processName = OsUtils.getProcessName(this.getApplicationContext(), android.os.Process.myPid());
        Log.i("ipc", "service on create:" + processName + "   process ID:" + android.os.Process.myPid());
    }





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new WorkerBinder();
    }




}
