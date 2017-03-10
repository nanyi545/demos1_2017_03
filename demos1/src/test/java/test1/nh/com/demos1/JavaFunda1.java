package test1.nh.com.demos1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Dictionary;
import java.util.Map;

import test1.nh.com.demos1.entities.testOveride.A;
import test1.nh.com.demos1.entities.testOveride.B;

/**
 * Created by Administrator on 15-12-30.
 */
public class JavaFunda1 {




    @Before
    public void before(){
    }

    @After
    public void after(){
    }


    @Test
    public void testSwitchCase(){
        int a=10;
        String aa="12345";
        switch (aa) {
            case "123":
                a=10;
                System.out.println("a="+a);
                break;
            case "1234":
                a=10;
                System.out.println("a="+a);
                break;
            default:
                a=9;
                System.out.println("a="+a);
                break;
        }
    }


    @Test
    public void testEqual(){
        A a1=new A(1);
        A a2=new A(1);
        A a3=a1;
        System.out.println("------a1 a2--------");
        System.out.println("=="+(a1==a2));
        System.out.println("------a1 a3--------");
        System.out.println("=="+(a1==a3));
    }



    @Test
    public void testOveride(){
//        A a1=new A(1);
//        a1.printThis();  // can not access the printThis() method
        B b1=new B(1);
        b1.printThis();  // this is OK
    }


    @Test
    public void testMap(){
        Map m1;
        Dictionary d1;
    }

    @Test
    public void testTryCatch(){
        tryCatchFinally1(-1);
    }

    private void tryCatchFinally1(int a){
        try{
            System.out.println("throwing");

            if (a>0){
                throw new Exception();
            }
            return;

        } catch (Exception e) {
            System.out.println("catching....");
            System.out.println(e.toString());
//            e.printStackTrace();
        } finally{
            System.out.println("finally");
        }
        System.out.println("after try-catch-finally");

    }




}
