package test1.nh.com.demos1.stack;

/**
 * Created by Administrator on 2017/6/22.
 */
import org.junit.Test;

import java.util.Stack;

public class NextLargerElement {


    /**
     *   next larger element :  for each element array --> find its nearest larger( >= ) element on its right
     *
     *    example input  : [6 , 3, 1,2, 4]
     *    example ouput  : [-1, 4, 2,4,-1]
     *
     *    use stack !!!!!!!!!
     *
     *    *6 3 1 2 4
     *                   s: 6       /top
     *     6*3 1 2 4
     *                   s: 6 3     /top
     *     6 3*1 2 4
     *                   s: 6 3 1   /top
     *     6 3 1*2 4
     *                  s: 6 3 2   /top          1->2
     *     6 3 1 2*4
     *                  s: 6 4     /top          2->4  3->4
     *                  s:         /top          4->-1   6->-1
     */

    @Test
    public void testFind(){
        int[] test=new int[]{6,3,1,2,4};
        System.out.println(print(findNextLargerE(test)));
    }



    public int[] findNextLargerE(int[] arr){
        int size=arr.length;  // init
        int[] ret=new int[size];
        Stack<ItemInArr> s=new Stack();
        s.push(new ItemInArr(0,arr[0])); // end of init

        for (int i=1;i<size;i++){
            int currentElement=arr[i];
            int currentIndex=i;
            while((s.size()>0)  && (currentElement >= s.peek().value) ){    // pop all elements that are smaller than the current   (  find are 'shadowed' elements !!! all elements need only be shadowed once!!!  )
                ItemInArr item=s.pop();
                ret[item.index]=currentElement;
            }
            s.push(new ItemInArr(currentIndex,currentElement));
        }
        while(s.size()>0){
            ItemInArr item=s.pop();
            ret[item.index]=-1;
        }
        return ret;
    }



    /**
     * class to store item in the stack ( we want to store the value as well as the index in array)
     */
    public class ItemInArr{
        int index;
        int value;
        public ItemInArr(int index, int value) {
            this.index = index;
            this.value = value;
        }
    }


    private static String print(int[] arr){
        StringBuilder sb=new StringBuilder();
        for(int temp:arr){
            sb.append("-"+temp);
        }
        return sb.toString();
    }




}
