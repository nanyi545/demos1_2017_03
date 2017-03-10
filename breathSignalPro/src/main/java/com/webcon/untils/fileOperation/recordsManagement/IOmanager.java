package com.webcon.untils.fileOperation.recordsManagement;

import android.util.Log;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 16-2-1.
 */
public class IOmanager {

    private IntArrayIO intArrayIO;

    public IOmanager(IntArrayIO intArrayIO) {
        this.intArrayIO = intArrayIO;
    }


    private Observer writeObserver;
    private Observer<Callable<int[]>> readObserver;

    private Subscriber writeObserverSubscriber;
    private Subscriber<? super Callable<int[]>> readObserverSubscriber;

    public void setWriteObserver(Observer observer) {
        this.writeObserver = observer;
        makeObservable().timeout(5, TimeUnit.SECONDS).observeOn(Schedulers.io()).subscribe(writeObserver);  // XX seconds is considered as timeout!
        while (writeObserverSubscriber==null){}  // end this method only when readObserverSubscriber is not null!!!

    }

    public void setReadObserver(Observer<Callable<int[]>> observer) {
        this.readObserver = observer;
        makeObservable1().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(readObserver);
        while (readObserverSubscriber==null){}  // end this method only when readObserverSubscriber is not null!!!
    }





    //--- "execute once" Observable pattern ----- for those Callables are executed once
    private <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        writeObserverSubscriber = subscriber;
//                        try {
//                            subscriber.onNext(func.call());
//                        } catch (Exception ex) {
//                            Log.e("AAA", "Error database operation", ex);
//                        }
                    }
                });
    }

    //--- "repeated execute" Observable pattern ----- for those Callables are executed repeatedly
    private <T> Observable<T> makeObservable() {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        writeObserverSubscriber = subscriber;
                    }
                });
    }
    private Observable<Callable<int[]>> makeObservable1() {
        return Observable.create(
                new Observable.OnSubscribe<Callable<int[]>>() {
                    @Override
                    public void call(Subscriber<? super Callable<int[]>> subscriber) {
                        readObserverSubscriber = subscriber;
                    }
                });
    }


    public void sendArray(int[] aaa) {
        writeObserverSubscriber.onNext(intArrayIO.getWriteCallable(aaa));
    }

    public void readArrayFromFile(File file) {
        Log.i("CCC","subscriber-null??"+(readObserverSubscriber==null));
        readObserverSubscriber.onNext(intArrayIO.getReadCallable(file));
    }


    public Observable getWriteObservable(final int[] aaa) {
        return makeObservable(intArrayIO.getWriteCallable(aaa)).subscribeOn(Schedulers.io());   //  Schedulers.computation()  or   Schedulers.io();
    }


}
