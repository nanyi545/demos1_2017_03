package test1.nh.com.demos1.number;

/**
 * Created by Administrator on 2017/6/26.
 */
import org.junit.Test;

public class MaxProductOf3 {

    /**
     * A unsorted array of integers is given; you need to find the max product formed my multiplying three numbers. (You cannot sort the array, watch out when there are negative numbers)
     *
     *   >0  max1 max2 max3
     *                  ... maxN2 maxN1 maxN>0
     *   <0  min1 min2 min3
     *                  ... minN2 minN1 minN<0
     * solution :
     *  case 1: result>0, max1 * max2 * max3      ===>    positive count>2
     *  case 2: result>0, min1 * min2 * max1      ===>    positive count>2   or  positive count=1
     *  case 3: result==0, 0 * anything         -+
     *  case 4: result<0, minN * minN1 * minN2   |===>    positive count=2 or 0
     *  case 5: result<0, maxN1 * maxN * minN   -+
     */
    @Test
    public void testCheck(){
        int[] test=new int[]{1,-1,3,-2,0,2,7,-10,-2};

    }



}
