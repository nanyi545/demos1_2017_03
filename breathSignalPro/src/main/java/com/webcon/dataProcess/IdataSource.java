package com.webcon.dataProcess;

import rx.Observer;

/**
 * Created by Administrator on 16-1-13.
 *
 * specify a data source
 */
public interface IdataSource {

    // set the observer for the data
    //   the observer is responsible to data analysis
    void setDataObserver(Observer<int[]> observer);

    // to be called by the real data source to feed data in
    // ---returns whether the dataSource has set a valid data observer
    int streamIn(int[] ints);

}
