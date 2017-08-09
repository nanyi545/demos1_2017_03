package test1.nh.com.demos1.mathTests;

/**
 * Created by Administrator on 2017/6/29.
 */
import org.junit.Test;

public class MathOverflow {


    @Test
    public void test(){
        int a=Integer.MAX_VALUE;
        int b=10;
        int overflow1=a*2;
        int overflow2=a+b;

        System.out.println(" overflow1:"+overflow1);
        System.out.println(" overflow1 --> backward operation  /2  :"+(overflow1)/2+"    backward operation /2   return to starting value:"+(((overflow1-b)/2)==a) );    //   *2  /2   can detect overflow for multiplication
        System.out.println(" overflow1 --> backward operation  >>>1:"+(overflow1>>>1)+"  backward operation >>>1 return to starting value:"+((overflow1>>>1)==a) );      //   *2  >>>1   can not detect overflow for multiplication

        System.out.println(" overflow2:"+overflow2);
        System.out.println(" overflow2 --> backward operation:"+(overflow2-b) +"  backward operation return to starting value:"+((overflow2-b)==a) );

    }


    /**
     *
     * to detect overflow in addition ...
     *
     *
     *
     * https://codereview.stackexchange.com/questions/6255/int-overflow-check-in-java
     *
     Apache Commons Math also uses long conversion:

     public static int addAndCheck(int x, int y) throws MathArithmeticException {
        long s = (long)x + (long)y;
        if (s < Integer.MIN_VALUE || s > Integer.MAX_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, x, y);
        }
        return (int)s;
     }

     */


    /**
     *

     Reverse digits of an integer.

     Example1: x = 123, return 321
     Example2: x = -123, return -321

     ---------The input is assumed to be a 32-bit signed integer. Your function should return 0 when the reversed integer overflows.----------------

     1534236469  --> this can cause overflow
     */
    public int reverse(int x) {
        int result=0;
        long curentResult=0;
        while(x!=0){
            int tail=x%10;
            curentResult=((long)result)*10+tail;  //   convert to long and then * 10 is crucial !!!!!!    -->  curentResult= result*10+tail  will fail here....
            if (curentResult > Integer.MAX_VALUE || curentResult < Integer.MIN_VALUE){  // check if there is an integer overflow
                return 0;
            }
            x=x/10;
            result= (int) curentResult;
        }
        return result;
    }


    @Test
    public void testReverse(){
        System.out.println(reverse(1534236469));
    }



}
