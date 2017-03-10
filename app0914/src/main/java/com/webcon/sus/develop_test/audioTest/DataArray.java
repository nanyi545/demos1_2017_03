package com.webcon.sus.develop_test.audioTest;

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
        this.int300=new short[totalLength];
    }
    // ---default constructor --> length 300----
    public DataArray() {
        this.int300=new short[totalLength];
    }


    private short[] int300;

    public synchronized short[] getWaveArray(){ //---lock on dataArray object---
        return int300;
    }

    //---add data to the array----
    @Override
    public void pushData(short[] ints) {
        synchronized (this) {  //---lock on dataArray object---
//            Log.i("AAA","pushing in"+Thread.currentThread().getName());
            int inputLength = ints.length;
            System.arraycopy(int300, 0, int300, inputLength, totalLength - inputLength);  // shift the array to right by inputLength
            System.arraycopy(ints, 0, int300, 0, inputLength);  //  add the first inputLength sub-array
        }
    }


}
