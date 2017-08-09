package test1.nh.com.demos1.sort;

import org.junit.Test;

/**
 * Created by Administrator on 2017/6/21.
 */

public class FindPair {


    /**
     *
     Given a sorted array and a number x, find a pair in array whose sum is closest to x.

     Examples:

     Input: arr[] = {10, 22, 28, 29, 30, 40}, x = 54
     Output: 22 and 30

     Input: arr[] = {1, 3, 4, 7, 10}, x = 15
     Output: 4 and 10

     */

    @Test
    public void testFind(){
        int[] test=new int[]{1,8,13,24,45,88,120};
        System.out.println(print(findPair(test,1)));
    }



    public int[] findPair(int[] arr,int target){

        int[] pairMin=new int[2];
        pairMin[0]=0;
        pairMin[1]=arr.length-1;

        int[] pair=new int[2];
        pair[0]=0;
        pair[1]=arr.length-1;

        int diff=arr[pair[0]]+arr[pair[1]]-target;
        int minimum = Math.abs( diff );

        while (pair[0]<pair[1]){
            System.out.println(pairMin[0]+"---"+pairMin[1]);
            if (diff==0) return pairMin;

            if ( diff > 0 ){
                pair[1]=pair[1]-1;
            } else {
                pair[0]=pair[0]+1;
            }
            if(pair[0]<pair[1]){  //   the case   pair[0]==pair[1]   should be ignored in searching for minimum ...
                diff=arr[pair[0]]+arr[pair[1]]-target;
                if ( Math.abs(diff) < minimum){
                    minimum = Math.abs(diff);
                    pairMin[0]=pair[0];
                    pairMin[1]=pair[1];
                }
            }
        }
        return pairMin;
    }


    private static String print(int[] arr){
        StringBuilder sb=new StringBuilder();
        for(int temp:arr){
            sb.append("-"+temp);
        }
        return sb.toString();
    }


}
