package com.webcon.dataProcess;

import android.util.Log;

import com.webcon.untils.BreathConstants;

/**
 * Created by Administrator on 16-1-13.
 *
 * class to manage breath signal wave form --> sampling frequency at 5hz : 300 int for 1 minute
 */
public class DataArray implements IpushData{

    // ---the default array length is 300---> corresponding to 1-minute breath data
    private int totalLength=300;

    public DataArray(int totalLength) {
        this.totalLength = totalLength;
        this.int300=new int[totalLength];
    }
    // ---default constructor --> length 300----
    public DataArray() {
        this.int300=new int[totalLength];
    }


    private int[] int300;

    public synchronized int[] getBreathArray(){ //---lock on dataArray object---
        return int300;
    }

    //---add data to the array----
    @Override
    public void pushData(int[] ints) {
        synchronized (this) {  //---lock on dataArray object---
            Log.i(BreathConstants.THREAD_INDICATOR,"pushing in"+Thread.currentThread().getName());
            int inputLength = ints.length;
            System.arraycopy(int300, 0, int300, inputLength, totalLength - inputLength);  // shift the array to right by inputLength
            System.arraycopy(ints, 0, int300, 0, inputLength);  //  add the first inputLength sub-array
        }
    }


}
