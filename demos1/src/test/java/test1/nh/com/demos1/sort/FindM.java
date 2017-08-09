package test1.nh.com.demos1.sort;

/**
 * Created by Administrator on 2017/6/28.
 */
import org.junit.Test;

public class FindM {


    @Test
    public void test2(){
        int[] arr={5,2,1,4,1,34,44,444};
        System.out.println(findKth(arr,1));
        System.out.println(findMedian(arr));
    }


    double findMedian(int[] arr){
        if (arr.length%2==1){
            int mid=arr.length/2;
            return findKth(arr,mid);
        }
        else {
            int mid1=arr.length/2-1;
            int mid2=arr.length/2;
            return (findKth(arr,mid1)+findKth(arr,mid2)+0d)/2;
        }
    }


    int findKth(int[] arr,int k){
        return findKth(arr,k,0,arr.length-1);
    }


    int findKth(int[] arr,int k,int lo,int hi){
        int pivotIndex=regroup(arr,lo,hi);
        if (k==pivotIndex) return arr[k];
        if (k<pivotIndex){
            return findKth(arr,k,lo,pivotIndex-1);
        }
        if (k>pivotIndex){
            return findKth(arr,k,pivotIndex+1,hi);
        }
        return -1;  // this can not happend
    }


    int regroup(int[] arr,int lo,int hi){
        int smallerCount=0;
        for (int i=lo;i<hi;i++){
            if (arr[i]<arr[hi]){
                swap(arr,i,lo+smallerCount);
                smallerCount+=1;
            }
        }
        swap(arr,hi,lo+smallerCount);
        return lo+smallerCount;
    }


    void swap(int[] arr,int i1,int i2){
        int temp=arr[i1];
        arr[i1]=arr[i2];
        arr[i2]=temp;
    }




}
