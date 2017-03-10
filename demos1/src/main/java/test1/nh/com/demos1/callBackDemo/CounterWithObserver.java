package test1.nh.com.demos1.callBackDemo;

import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by Administrator on 15-12-22.
 */
public class CounterWithObserver {

    private int countDownInt;
    private String counterId;
    private String TAG;

    private Observer observer;
    public void setObserver(Observer observer){
        this.observer=observer;
        observable.subscribe(observer);
    }

    private Subscriber observerSubscriber;
    private Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            observerSubscriber =subscriber;
        }
    });

    public CounterWithObserver(int i1, String name, String TAG) {
        this.countDownInt = i1;
        this.counterId = name;
        this.TAG = TAG;
    }


    public void startCountDown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (countDownInt > 0) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    countDownInt--;
                }
                Log.i(TAG, "counter:" + counterId + " finished");
                if (null!= observerSubscriber) {
                    observerSubscriber.onNext("gogogo");
                    observerSubscriber.onNext("gogogo123");
                    observerSubscriber.onCompleted();
                }
            }
        }).start();
    }

}
