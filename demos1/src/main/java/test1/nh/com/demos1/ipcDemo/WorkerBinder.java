package test1.nh.com.demos1.ipcDemo;

import android.os.Binder;
import android.util.Log;

/**
 * Created by Administrator on 16-4-10.
 */
public class WorkerBinder extends Binder implements IipcWork{

    @Override
    public void storeUser(int id, String user) {
        Log.i("ipc","storing successfully");
    }

    @Override
    public String getUser(int id) {
        return "test User name";
    }
}
