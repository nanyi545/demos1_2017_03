package test1.nh.com.demos1.heap;

/**
 * Created by Administrator on 2017/6/23.
 */
import org.junit.Test;


public class HeapArray {

    @Test
    public void testSort(){
        int[] test=new int[]{3,7,5,9,23,1,23,54,23,12,1,55,7,2,5,345,1};
        heapSort(test);
        print(test);
    }


    void heapSort(int[] arr){
         buildMaxHeap(arr);
        for (int downBubbleCount=1;downBubbleCount<arr.length;downBubbleCount++){
            swap(arr,0,arr.length-downBubbleCount);
            heapify(arr,0,downBubbleCount);
        }
    }

    /**
     *    do heapify on all non-leaf nodes
     */
    void buildMaxHeap(int[] arr){
        int lastNonLeaf=getParent(arr.length-1);
        for (int i=lastNonLeaf;i>=0;i--){
            heapify(arr,i,0);
        }
    }


    /**
     *   this is down-ward bubble action
     *
     * @param arr
     * @param root   this is a max heap except at the root ...
     */
    void heapify(int[] arr,int root,int excludeLast){
        int size=arr.length-excludeLast;
        if (root<size){
            int maxIndex=root;
            int left=getLeft(root);
            int right=getRight(root);
            if (left<size){
                if(arr[left]>=arr[maxIndex]){
                    maxIndex=left;
                }
            }
            if (right<size){
                if(arr[right]>=arr[maxIndex]){
                    maxIndex=right;
                }
            }
            if(maxIndex!=root){
                swap(arr,maxIndex,root);
                heapify(arr,maxIndex,excludeLast);
            }
        }
    }


    void swap(int[] arr,int ind1,int ind2 ){
        int temp=arr[ind1];
        arr[ind1]=arr[ind2];
        arr[ind2]=temp;
    }


    int getLeft(int parent){
        return parent*2+1;
    }


    int getRight(int parent){
        return parent*2+2;
    }

    int getParent(int child){
        int res=child%2;
        if(res==1){
            return child/2;
        }
        if (res==0){
            return child/2-1;
        }
        return 0;
    }


    boolean isRoot(int ind){
        return ind==0;
    }


    private static void print(int[] arr){
        StringBuilder sb=new StringBuilder();
        for(int temp:arr){
            sb.append("-"+temp);
        }
        System.out.println( sb.toString()) ;
    }








}
