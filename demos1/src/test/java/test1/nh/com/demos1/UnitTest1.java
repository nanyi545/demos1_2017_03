package test1.nh.com.demos1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

import test1.nh.com.demos1.utils.TestObject;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 15-12-30.
 */
public class UnitTest1 {

    private void dumpArray(int[] ints){
        int intLength=ints.length;
        System.out.println("length:"+intLength);
        for (int ii=0;ii<intLength;ii++){
            System.out.print(" "+ints[ii]);
        }
        System.out.println("end of array");
    }


    private TestObject testObj1;

    @Before
    public void before(){
        testObj1=new TestObject(10);
    }

    @After
    public void after(){
        testObj1=null;
    }


    @Test
    public void test1(){
//        testObj1.talk();
        assertEquals(8,testObj1.getLength());
    }

    @Test
    public void test2(){
//        testObj1.talk();
        assertEquals(10,testObj1.getLength());
    }

    @Test
    public void testString(){
        String str1="abcdefg";
        System.out.println(str1);
        str1="efg";
        System.out.println(str1);


        String aa="http://jlu.edu.cn";
        System.out.println(aa);
        System.out.println(aa.replace(".","_"));

    }

    @Test
    public void testString2(){
        String str1="abcdefg";
        System.out.println(str1.substring(2,4));
        System.out.println(Integer.parseInt("02"));
    }

    @Test
    public void testArray1(){
        int[] a1={1,2,3};
        dumpArray(a1);
        a1=new int[]{1,2,3,4,5};
        dumpArray(a1);
    }


    @Test
    public void testChar(){
        short a1= '\0';
        System.out.println(a1);
    }

//// ArrayMap needs android API
//    @Test
//    public void testMap(){
//        ArrayMap<String, Integer> mDeviceReflect=new ArrayMap<>();
//        mDeviceReflect.put("333",1);
//        mDeviceReflect.put("11",2);
//        boolean a0=mDeviceReflect.containsKey("");
//        System.out.println(a0);
//        int a1=mDeviceReflect.get("11");
//        System.out.println(a1);
//    }



    @Test
    public void testNull(){
        Object o=null;
        System.out.println((o!=null));
    }




    @Test
    public void testTimer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println("count"+i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Timer timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("task1");
            }
        }, 1L);
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("task3");
            }
        }, 10L);

    }

    @Test
    public void testHttp(){

        

    }









}
