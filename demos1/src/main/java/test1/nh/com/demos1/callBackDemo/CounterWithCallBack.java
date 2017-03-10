package test1.nh.com.demos1.callBackDemo;

import android.util.Log;

/**
 * Created by Administrator on 15-12-22.
 */
public class CounterWithCallBack {


    // call back interface
    public static interface FinishCallBack{
        void callUponFinish();
    }


    private FinishCallBack mFinishCallBack;

    public void registerFinishCallback(FinishCallBack callback){
        this.mFinishCallBack=callback;
    }


    private int countDownInt;
    private String counterId;
    private String TAG;
    public CounterWithCallBack(int i1,String name,String TAG){
        this.countDownInt=i1;
        this.counterId=name;
        this.TAG=TAG;
    }

    public void startCountDown(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                while (countDownInt>0){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    countDownInt--;
                }
                Log.i(TAG,"counter:"+counterId+" finished");

                if (null!=mFinishCallBack)
                mFinishCallBack.callUponFinish();  // call the finish callback
            }
        }).start();
    }




}
