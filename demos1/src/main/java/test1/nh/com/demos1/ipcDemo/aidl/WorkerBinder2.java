package test1.nh.com.demos1.ipcDemo.aidl;

import android.os.RemoteException;
import android.util.Log;

/**
 * Created by Administrator on 16-4-10.
 */
public class WorkerBinder2 extends IMyAidlInterface.Stub{


    String processInfo;
    public WorkerBinder2(String processInfo){
        this.processInfo=processInfo;
    }

    @Override
    public void storeUser(int id, String user) throws RemoteException {
        Log.i("ipc","storing successfully");
    }

    @Override
    public String getUser(int id) throws RemoteException {
        Log.i("ipc","get user in WorkerBinder2: "+processInfo);
        return "a user";
    }
}
