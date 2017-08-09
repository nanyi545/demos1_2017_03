package test1.nh.com.demos1.heap;

/**
 * Created by Administrator on 2017/6/23.
 */
import org.junit.Test;

import java.util.Stack;

public class IsFull {


    @Test
    public void testCheck(){
        int[] test=new int[]{1,2,3,4,5};
        System.out.println(isFullRecursion(test,0));
        System.out.println(isFull(test));
    }

    /**
     * proper binary tree: each node has either 0 or 2 children -->  also called full binary tree
     */


    /**
     *  -----------  this is a recursive call ------------------
     */
    private boolean isFullRecursion(int[] arr,int index){
        int left   = index*2+1;
        int right  = index*2+2;
        if ( (left>=arr.length)&&(right>=arr.length) ) {  // has 0 children
            return true;
        }
        if ( (left< arr.length)&&(right< arr.length) ) {  // has 2 children
            return isFullRecursion(arr,left)&&isFullRecursion(arr,right);
        }
        else {  // has 1 children
            return false;
        }
    }

    /**
     *  -----------  this is a non recursive call ------------------
     */
    private boolean isFull(int[] arr){
        Stack<Integer> s=new Stack();
        s.push(0);
        while(s.size()>0){
            int current=s.pop();
            int left   = current*2+1;
            int right  = current*2+2;
            if ( (left>=arr.length)&&(right>=arr.length) ) {  // has 0 children
                continue;
            }
            if ( (left< arr.length)&&(right< arr.length) ) {  // has 2 children
                s.push(left);
                s.push(right);
                continue;
            }
            else {  // has 1 children
                return false;
            }
        }
        return true;
    }









}
