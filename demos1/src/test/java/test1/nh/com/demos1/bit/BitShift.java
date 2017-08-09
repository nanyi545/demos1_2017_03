package test1.nh.com.demos1.bit;

/**
 * Created by Administrator on 2017/6/28.
 */
import org.junit.Test;

public class BitShift {


    /**
     * The signed left shift operator "<<" shifts a bit pattern to the left,
     * and the signed right shift operator ">>" shifts a bit pattern to the right.
     *
     * The bit pattern is given by the left-hand operand, and the number of positions to shift by the right-hand operand.
     * The unsigned right shift operator ">>>" shifts a zero into the leftmost position,
     *
     * while the leftmost position after ">>" depends on sign extension.
     *
     *
     *---------------------------------------------------------------------------
     *    https://stackoverflow.com/questions/19058859/what-does-mean-in-java
     *
     *    The >>> operator is the unsigned right bit-shift operator in Java. It effectively divides the operand by 2
     *
     *    The difference between >> and >>> would only show up when shifting negative numbers. The >> operator shifts a 1 bit into the most significant bit if it was a 1, and the >>> shifts in a 0 regardless.
     *

     Let's average 1 and 2147483647 (Integer.MAX_VALUE). We can do the math easily:
     (1 + 2147483647) / 2 = 2147483648 / 2 = 1073741824



     Now, with the code (low + high) / 2, these are the bits involved:

     1:           00000000 00000000 00000000 00000001
     +2147483647: 01111111 11111111 11111111 11111111
     ================================================
     -2147483648: 10000000 00000000 00000000 00000000  // Overflow
     /2
     ================================================
     -1073741824: 11000000 00000000 00000000 00000000  // Signed divide, same as >> 1.



     Let's use     "shift" to >>>:

     1:           00000000 00000000 00000000 00000001
     +2147483647: 01111111 11111111 11111111 11111111
     ================================================
     -2147483648: 10000000 00000000 00000000 00000000  // Overflow
     >>> 1
     ================================================
     +1073741824: 01000000 00000000 00000000 00000000  // Unsigned shift right.


     *
     */

    @Test
    public void testShift(){
        int a1=2147483647;
        int a2=2147483647;
        System.out.println((a1+a2)/2);
        System.out.println((a1+a2)>>>1);
    }







}
