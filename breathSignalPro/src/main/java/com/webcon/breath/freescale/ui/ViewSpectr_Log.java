package com.webcon.breath.freescale.ui;

import android.util.Log;

/**
 * Created by Administrator on 16-1-14.
 */
public class ViewSpectr_Log implements IviewSpectro  {
    @Override
    public void prepareGraphData(int[] intss, int[] indicators, int timer, int flux, int signalState) {
        int[] ints=intss;
        int intsLength=ints.length;
        StringBuilder sb=new StringBuilder();
        for (int ii=0;ii<intsLength;ii++)
            sb.append(ints[ii]);
        Log.i("AAA",sb.toString());
    }
}
