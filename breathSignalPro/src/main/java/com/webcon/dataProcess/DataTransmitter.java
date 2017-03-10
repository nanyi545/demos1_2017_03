package com.webcon.dataProcess;

import com.webcon.untils.BreathConstants;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 16-1-13.
 */
public class DataTransmitter implements IdataSource {


    @Override
    public int streamIn(int[] ints1) {
        if (null==observerSubscriber) return BreathConstants.OBSERVER_NULL;
        else{
            observerSubscriber.onNext(ints1);
            return BreathConstants.DATA_SENT_OBSERVER;
        }
    }


    private Observer<int[]> dataObserver; // dummy data observer,


    // advantage--> specifying the working thread of the observer: preferably non-UI thread
    @Override
    public void setDataObserver(Observer<int[]> observer) {
        this.dataObserver = observer;
        dataObservable.subscribeOn(Schedulers.computation())  // data generation on .... worker thread
                .observeOn(Schedulers.computation())   // data handling on .... (main thread.  AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private Subscriber observerSubscriber;

    // dataObservable's responsibility is to obtain the subscriber which the dataObservable is subscribed to
    private Observable dataObservable = Observable.create(new Observable.OnSubscribe<int[]>() {
        @Override
        public void call(Subscriber<? super int[]> subscriber) {
            observerSubscriber = subscriber;
        }
    });



}
