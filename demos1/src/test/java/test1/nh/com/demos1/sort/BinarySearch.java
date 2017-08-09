package test1.nh.com.demos1.sort;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/19.
 */

public class BinarySearch {


    /**
     * Q1
     * find a target in a ordered array...
     */
    @Test
    public void testFind(){
        int[] test=new int[]{1,2,4,6,7,9,10,12,44,44,55,66,77};
        System.out.println(findTarget(test,99));
    }





    public static int findTarget(int[] arr, int target){
        int startIndex=0;
        int endIndex=arr.length-1;
        int midIndex=(startIndex+endIndex)/2;
        while (startIndex <= endIndex) {
            if (arr[midIndex]==target){
                return midIndex;
            }
            if (arr[midIndex]>target){
                endIndex =midIndex-1;
                midIndex =(startIndex+endIndex)/2;
            }
            if (arr[midIndex]<target){
                startIndex =midIndex+1;
                midIndex =(startIndex+endIndex)/2;
            }
        }
        return -1;
    }


    /**
     *  Q2
     *  given an ordered array, and a value target,   find the minimum in array that is >= target
     *                                                find the max     in array that is <= target
     */
    @Test
    public void testfindLowerBound(){
        int[] test=new int[]{1,2,4,6,7,9,10,12,44,44,55,66,77};
        System.out.println(findLowerBound(test,8));
    }


    public static int findLowerBound(int[] arr, int target){
        int lo=0;
        int hi=arr.length-1;
        while ( (hi-lo)>1 ) {
            int mid=(lo+hi)/2;
            if (arr[mid] >= target){
                hi = mid ;
            } else {
                lo =mid ;
            }
        }
        if (arr[hi] >= target){
            return arr[hi];
        }else{
            return -1;
        }
    }





}
