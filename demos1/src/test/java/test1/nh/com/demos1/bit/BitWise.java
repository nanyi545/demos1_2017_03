package test1.nh.com.demos1.bit;

/**
 * Created by Administrator on 2017/6/27.
 */
import org.junit.Test;

public class BitWise {



    @Test
    public void testOR(){
        System.out.println("and");
        System.out.println("1&1="+(1&1));
        System.out.println("1&0="+(1&0));
        System.out.println("0&1="+(0&1));
        System.out.println("0&0="+(0&0));
        System.out.println("inclusive OR");
        System.out.println("1|1="+(1|1));
        System.out.println("1|0="+(1|0));
        System.out.println("0|1="+(0|1));
        System.out.println("0|0="+(0|0));
        System.out.println("exclusive OR");
        System.out.println("1^1="+(1^1));
        System.out.println("1^0="+(1^0));
        System.out.println("0^1="+(0^1));
        System.out.println("0^0="+(0^0));
        System.out.println("3^4="+(3^4));
    }



}
