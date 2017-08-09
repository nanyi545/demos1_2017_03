package test1.nh.com.demos1.sort;

import org.junit.Test;

/**
 * Created by Administrator on 2017/6/21.
 */

public class QuickSort {

    @Test
    public void testFind(){
        int[] test=new int[]{1,3,3,4,5};
//        quickSort(test,0,test.length-1);
//        System.out.println(print(test));
        System.out.println(findKthSmallest(test,0,test.length-1,2));
    }



    public static int findKthSmallest(int[] arr,int lo, int hi,int k){
        if ( hi>= lo){
            int pivotIndex = regroup2(arr,lo,hi);
            if (pivotIndex==k) {
                return arr[k];
            }
            if (pivotIndex > k){
                return findKthSmallest(arr,lo,pivotIndex-1,k);
            }
            if (pivotIndex < k){
                return findKthSmallest(arr,pivotIndex+1,hi,k);
            }
            return -1;
        } else  return -1;
    }



    public static void quickSort(int[] arr,int lo, int hi){
        if(hi>lo){
            int pivotIndex=regroup2(arr,lo,hi);
            quickSort(arr,lo,pivotIndex-1);
            quickSort(arr,pivotIndex+1,hi);
        }
    }


    /**
     *      lo   ~  ret-1  are all <= pivot
     *      ret+1 ~  hi    are all >= pivot
     *                     arr[ret]= pivot
     *
     *                     this is the more 'proper' way
     */
    public static int regroup2(int[] arr,int lo,int hi){
        int pivot=arr[hi];
        int smallCount=0;
        for (int i=lo;i<hi;i++){
            if(arr[i]<pivot){
                swap(arr,lo+smallCount,i);
                smallCount+=1;
            }
        }
        swap(arr,smallCount+lo,hi);
        return smallCount+lo;
    }

    private static void swap(int[] arr,int ind1,int ind2){
        int temp=arr[ind1];
        arr[ind1]=arr[ind2];
        arr[ind2]=temp;
    }


    /**
     *      lo   ~  ret-1  are all <= pivot
     *      ret+1 ~  hi    are all >= pivot
     *                     arr[ret]= pivot
     *
     *                     this is the tricky way
     */
    public static int regroup(int[] arr,int lo,int hi){
        int pivot=arr[hi];
        int unChecked_start=lo;
        int unChecked_end=hi-1;
        boolean checkNextFromStart=true;
        int smallerCount=0;
        while(unChecked_start <= unChecked_end){
            if (checkNextFromStart){   //  check from start...
                if (arr[unChecked_start]>pivot){
                    arr[unChecked_end+1]=arr[unChecked_start];
                    unChecked_start+=1;
                    checkNextFromStart=false;
                } else{
                    smallerCount+=1;
                    unChecked_start+=1;
                    checkNextFromStart=true;
                }
            }  else {  //  check from end...
                if(arr[unChecked_end]>pivot){
                    unChecked_end-=1;
                    checkNextFromStart=false;
                } else {
                    smallerCount+=1;
                    arr[unChecked_start-1]=arr[unChecked_end];
                    unChecked_end-=1;
                    checkNextFromStart=true;
                }
            }
        }
        arr[lo+smallerCount]=pivot;
        return lo+smallerCount;
    }


    private static String print(int[] arr){
        StringBuilder sb=new StringBuilder();
        for(int temp:arr){
            sb.append("-"+temp);
        }
        return sb.toString();
    }




}
