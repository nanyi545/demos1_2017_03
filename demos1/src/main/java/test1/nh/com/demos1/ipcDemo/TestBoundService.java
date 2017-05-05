package test1.nh.com.demos1.ipcDemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class TestBoundService extends Service {


    public TestBoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new TestBinder(this);
    }

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.i("ipc","non-ipc msg:"+msg.what+"   in thread:"+Thread.currentThread().getName());
        }

    }


    @Override
    public void onCreate() {
        super.onCreate();

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }


    public static class TestBinder extends Binder {

        private ServiceHandler mServiceHandler;

        private TestBinder (TestBoundService service){
            mServiceHandler=service.mServiceHandler;
        }

        public void performAction(Runnable runnable){
            mServiceHandler.post(runnable);
        }

        public void sendMsg(int what){
            mServiceHandler.sendEmptyMessage(what);
        }


        public <R,I> void executeTask(Task<R,I> task,I i){
            final Task<R,I> mTask=task;
            final I input=i;
            final Handler mainHandler=new Handler();
            mServiceHandler.post(new Runnable() {
                @Override
                public void run() {
                    final R ret=mTask.executeTask(input);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTask.onCompleted(ret);
                        }
                    });
                }
            });
        }

        public interface Task<R,I> {
            R executeTask(I i);
            void onCompleted(R r);
        }

    }



}
