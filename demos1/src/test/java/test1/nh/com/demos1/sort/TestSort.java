package test1.nh.com.demos1.sort;

import org.junit.Test;

/**
 * Created by Administrator on 2017/7/31.
 */

public class TestSort {


    @Test
    public void testFind(){
        int[] test=new int[]{1,3,3,4,5,2,7,-2};
        qsort(test);
        print(test);
    }



    private void print(int[] arr){
        StringBuilder sb=new StringBuilder();
        for (int temp:arr){
            sb.append("|"+temp);
        }
        System.out.println(sb.toString());
    }


    private void qsort(int[] arr){
        int start=0;
        int end=arr.length-1;
        qsort1(arr,start,end);
    }



    private void qsort1(int[] arr,int start,int end){
        if (start<end){
            int pivotIndex=qsort(arr,start,end);
            qsort1(arr,start,pivotIndex-1);
            qsort1(arr,pivotIndex+1,end);
        }
    }


    private int qsort(int[] arr,int start,int end){
        int pivot=arr[end];
        int smallerCount=0;
        for (int i=start;i<=end-1;i++){
            if(arr[i]<pivot){
                swap(arr,i,start + smallerCount);
                smallerCount+=1;
            }
        }
        swap(arr,end,start + smallerCount);
        return start+smallerCount;
    }


    private void swap(int[] arr,int i1,int i2){
        int temp=arr[i1];
        arr[i1]=arr[i2];
        arr[i2]=temp;
    }



}
