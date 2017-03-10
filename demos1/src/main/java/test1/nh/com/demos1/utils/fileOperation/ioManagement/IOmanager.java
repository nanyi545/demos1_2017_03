package test1.nh.com.demos1.utils.fileOperation.ioManagement;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 16-2-1.
 */
public class IOmanager {

    private IntArrayIO intArrayIO=new IntArrayIO();


    private Subscriber observerSubscriber;

    private <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                            observerSubscriber =subscriber;
                        } catch (Exception ex) {
                            Log.e("AAA", "Error database operation", ex);
                        }
                    }
                });
    }



    public Observable getWriteObservable(final Context c, final int[] aaa,final Calendar timeOfWrite){
        return makeObservable(intArrayIO.getWriteCallable(c,aaa,timeOfWrite)).subscribeOn(Schedulers.io());   //  Schedulers.computation()  or   Schedulers.io();
    }


}
