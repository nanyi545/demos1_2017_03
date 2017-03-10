package test1.nh.com.demos1.entities;

import android.util.Log;

/**
 * Created by Administrator on 15-12-11.
 */
public class TestReflection {

    private static final String FINAL_STRING="final string";
    private static final int FINAL_INT=789;
    private static final boolean FINAL_B=true;

    public static void print(){
        Log.i("AAA","String:"+FINAL_STRING+"  int:"+FINAL_INT+"   boolean"+FINAL_B);
    }

    public static final String FINAL_STRING2="final string public";

}
