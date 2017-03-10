package test1.nh.com.demos1.utils;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Administrator on 15-11-23.
 */
public class TestObject implements Serializable{

    public TestObject(int length){
        this.length=length;
    }

    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void talk(){
        Log.i("POLY","----obj0");
    }
}
