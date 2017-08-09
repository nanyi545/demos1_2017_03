package test1.nh.com.demos1.heap;

/**
 * Created by Administrator on 2017/6/23.
 */
import org.junit.Test;

public class HeapSort {

    @Test
    public void testSort(){
        int[] test=new int[]{1,8,13,24,45,88,120};
    }


    public class Heap{
        int count;
        Node root;
        Node last=root;

        void add(Node v){


        }
    }


    public Heap buildMaxHeap(int[] arr){
        return null;
    }



    /**
     *   up bubble in a max heap
     */
    public void upBubble(Node v){
        Node current=v;
        while(current.isRoot()){
            if (current.parent.value>=current.value){
                return;
            } else {
                swapValueWithParent(current);
                current = current.parent;
            }
        }
    }




    public void swapValueWithParent(Node v){
        Node parent=v.parent;
        int temp=v.value;
        v.value=parent.value;
        parent.value=temp;
    }





}
