package test1.nh.com.demos1.activities.tracking.tracking_utils;

import android.content.Context;
import android.util.Log;

import de.greenrobot.event.EventBus;
import test1.nh.com.demos1.utils.DMapplication;

/**
 * Created by Administrator on 2017/5/25.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler crashHandler;

    private CrashHandler() {}

    public static CrashHandler getInstance(Context context) {
        if (crashHandler == null) {
            synchronized (CrashHandler.class) {
                if (crashHandler == null) {
                    crashHandler = new CrashHandler();
                    crashHandler.init(context);
                }
            }
        }
        return crashHandler;
    }






    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    /**
     * 初始化
     */
    private void init(Context context) {
        mContext = context;
        // 获取系统默认的 UncaughtException 处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该 CrashHandler 为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }




    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (handleException(e)){

                } else {
                    mDefaultHandler.uncaughtException(t,e);
                }
            }
        }).start();
    }


    /**
     * @param ex
     * @return true:handled , false : pass to system handler..
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        TrackingManager manager=TrackingManager.getInstance(mContext);
        String preReplace=Log.getStackTraceString(ex);
        Log.e(DMapplication.DEM0_APP, "preReplace\n"+preReplace);
        Log.e(DMapplication.DEM0_APP, "afterReplace\n"+preReplace.replace("\'","|"));
        manager.addTrackingLog(new LogError(Log.getStackTraceString(ex).replace("\'","|")));
        try {  // wait for local IO
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }






}
