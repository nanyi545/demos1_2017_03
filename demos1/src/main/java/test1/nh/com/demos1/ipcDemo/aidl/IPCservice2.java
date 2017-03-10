package test1.nh.com.demos1.ipcDemo.aidl;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.MainActivity_from;
import test1.nh.com.demos1.utils.OsUtils;

/**
 * Created by Administrator on 16-4-10.
 */
public class IPCservice2 extends Service {

    String processInfo;
    int FORE_NOTI_ID=9857;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("ipc","ipc service created!");
        String processName = OsUtils.getProcessName(this.getApplicationContext(), android.os.Process.myPid());
        Log.i("ipc", "service on create:" + processName + "   process ID:" + android.os.Process.myPid());
        processInfo="service on create:" + processName + "   process ID:" + android.os.Process.myPid();

        startForeground(FORE_NOTI_ID, createNotice());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new WorkerBinder2(processInfo);
    }




    //-----------Notification Test------------------
    private Notification createNotice() {

        //点通知跳转，用pendingIntent--->
        Intent intent = new Intent(this, MainActivity_from.class);
        PendingIntent pintent = PendingIntent.getActivity(this, 0, intent, 0);

        // 从资源文件转到bitmap
        Resources res=getResources();
        Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.icon1);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentIntent(pintent)
                        .setSmallIcon(R.drawable.icon_shield_green)
                        .setLargeIcon(bmp)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("this service runs in a separate process")
                        .setContentText("Hello IPC!");
        return mBuilder.build();
    }

}
