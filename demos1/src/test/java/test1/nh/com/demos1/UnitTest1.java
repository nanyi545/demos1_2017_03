package test1.nh.com.demos1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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

        int a=2147483647;
        System.out.println("a:"+a+"   Integer.MAX_VALUE:"+Integer.MAX_VALUE);
        System.out.println(a<Integer.MAX_VALUE);
        System.out.println(a>Integer.MIN_VALUE);


    }





    @Test
    public void testFindPeak(){
        int[] arr=new int[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,16,15};
        System.out.println(findPeakElement(arr));

    }



    public int findPeakElement(int[] nums) {
        if(nums.length==1) return nums[0];
        if(nums[0]>nums[1]) return nums[0];
        if(nums[nums.length-1]>nums[nums.length-2]) return nums[nums.length-1];
        return findP(nums,0,nums.length-1);
    }


    public int findP(int[] nums, int lo, int hi){

        int length=hi-lo+1;
        if( length>=3 &&  length <=5 ) {
            for (int i=lo+1;i<=hi-1;i++){
                if(nums[i]>nums[i-1] && nums[i]>nums[i+1]) {
                    return i;
                }
            }
            return -1;
        }  else {

            int l1=length/2+length%2+lo;
            int l2=l1-1;

            System.out.println("lo:"+lo+"  hi:"+hi+"  l1:"+l1+"  l2:"+l2);
            int ret1=findP (nums,lo,l1);
            if (ret1>0) {
                return ret1;
            }  else {
                int ret2=findP(nums,l2,hi);
                return ret2;
            }
        }
    }





    @Test
    public void testMaxP(){
        int[] arr=new int[] {1,12,-3,1,2,6,2};
        System.out.println(maxProduct(arr));

    }

    /**
     *
     *
     i:1    val:12    max:12     min:12    result:12
     i:2    val:-3    max:-3     min:-36    result:12
     i:3    val:1    max:1     min:-36    result:12
     i:4    val:2    max:2     min:-72    result:12
     i:5    val:6    max:12     min:-432    result:12
     i:6    val:2    max:24     min:-864    result:24
     24

     */
    public int maxProduct(int[] A) {
        if (A == null || A.length == 0) {
            return 0;
        }
        int max = A[0], min = A[0], result = A[0];
        for (int i = 1; i < A.length; i++) {
            int temp = max;
            max = Math.max(Math.max(max * A[i], min * A[i]), A[i]);
            min = Math.min(Math.min(temp * A[i], min * A[i]), A[i]);
            if (max > result) {
                result = max;
            }
            System.out.println( "i:"+i+"    val:"+A[i]+"    max:"+max+"     min:"+min+"    result:"+result);
        }
        return result;
    }



    @Test
    public void testR(){
        System.out.println(Math.random());
    }


}
