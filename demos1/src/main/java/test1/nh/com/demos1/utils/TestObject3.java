package test1.nh.com.demos1.utils;

import android.util.Log;

/**
 * Created by Administrator on 15-11-24.
 */
public class TestObject3 extends TestObject {

    public TestObject3(int length){
        super(length);
    }

    public void talk(){
        Log.i("POLY", "----obj3");
    }

    public void talk2(){
        Log.i("POLY", "----obj3---talk2");
    }


}
