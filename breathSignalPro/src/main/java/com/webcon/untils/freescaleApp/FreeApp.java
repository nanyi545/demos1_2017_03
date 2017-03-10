package com.webcon.untils.freescaleApp;

import android.app.Application;
import android.util.Log;

import com.webcon.untils.BreathConstants;

import java.io.IOException;
import java.io.InputStream;

import serial_port.utils.SerialPort;
import serial_port.utils.SerialPortFinder;

/**
 * Created by Administrator on 16-1-13.
 */
public class FreeApp extends Application {

    private String TAG=BreathConstants.APP_LOG;


    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;
    private InputStream mInputStream;

    private void dump(byte[] bytearray){
        StringBuilder sb=new StringBuilder();
        int length=bytearray.length;
        for (int ii=0;ii<length;ii++){
            sb.append("-"+bytearray[ii]);
        }
        Log.i(TAG,sb.toString());
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while(true) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) {return;}
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        dump(buffer);
                    } else {
                        Log.i(TAG,"no data yet");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"application launched");

        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
        int length=entries.length;
        Log.i(TAG, "length:"+length);

        for (int ii=0;ii<length;ii++) {
            Log.i(TAG, "ith:"+ii);
            Log.i(TAG, "entries:"+entries[ii]);        //  ttymxc3 (IMX-uart)
            Log.i(TAG, "entryValues:"+entryValues[ii]);//  /dev/ttymxc3
        }

//        File file=new File("/dev/ttymxc3");// exist true
//        File file=new File("/ttymxc3");  // exist false
//        try {
//            mSerialPort=new SerialPort(file,115200,1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        mInputStream=mSerialPort.getInputStream();
//        new ReadThread().start();

    }
}
