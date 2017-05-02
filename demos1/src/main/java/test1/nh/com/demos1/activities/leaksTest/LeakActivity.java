package test1.nh.com.demos1.activities.leaksTest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import test1.nh.com.demos1.R;

public class LeakActivity extends Activity {



    private int[] leak_int_array=new int[1024*1024*2];

//    private final Handler mLeakyHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Log.i("LEAK","receive message in leak activity");
//        }
//    };


    //---- non-leak Handler -----
    private static class MyHandler extends Handler {
        private final WeakReference<LeakActivity> mwActivity;
        public MyHandler(LeakActivity activity) {
            mwActivity = new WeakReference<LeakActivity>(activity);  //  WeakReference. get() will return null when referenced object was gc-ed.
        }
        @Override
        public void handleMessage(Message msg) {
//            LeakActivity activity = mActivity.get();
//            if (activity != null) {
//                activity.tv.setText("non-leak-handler");
//            }
        }
    }

    private MyHandler mHandler = new MyHandler(this);



    // non--leak runnable------method 1: if the runnable needs to access the activity ------
    private static class StaticRunnable implements Runnable {
        private final WeakReference<LeakActivity> mwActivity;
        public StaticRunnable(LeakActivity activity){
            mwActivity = new WeakReference<LeakActivity>(activity);  //  WeakReference. get() will return null when referenced object was gc-ed.
        }
        @Override
        public void run() {
            LeakActivity activity = mwActivity.get();
            if (activity != null) {
                activity.tv.setText("non-leak-handler + static runnable");
            }else {
                Log.i("LEAK","WEAK-reference get null return, GC occurred");
            }

        }
    }

    // non--leak runnable------method 2: if the runnable does not need to access the activity ------
    private static final Runnable sRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i("LEAK","static runnable");
        }
    };



    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leak);

        tv= (TextView) findViewById(R.id.tv_leak);


//        mHandler.sendEmptyMessage(11);
//        mHandler.postDelayed(nsRunnable, 1000 * 60*1);//  go to OOM
////------no leak
//        mHandler.postDelayed(sRunnable, 1000 * 60*1);
        mHandler.postDelayed(new StaticRunnable(this), 1000 * 60*1);


//        mLeakyHandler.sendEmptyMessage(1);
////------cause memory leak within 1 minute--->beyond 1 minute GC can restore the memory
//        mLeakyHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("LEAK", "send message");
//            }
//        }, 1000 * 60*1);

        // Go back to the previous Activity.
//        finish();
    }


    @Override
    protected void onPause() {
//        leak_int_array=null;
        super.onPause();
    }


}
