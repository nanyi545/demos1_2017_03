package test1.nh.com.demos1.mathTests;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test1.nh.com.demos1.utils.math.MathVector2D;

/**
 * Created by Administrator on 15-12-30.
 */
public class MathTest1 {

    @Before
    public void before(){
    }

    @After
    public void after(){
    }


    @Test
    public void test1(){
        System.out.println(""+(5%7));
        System.out.println(""+(21%7));
        System.out.println(""+(25%7));
    }


    @Test
    public void test2(){
        int a1=-1;
        int a=Math.abs(a1);
        System.out.println(""+a1+"   "+a);
    }


    @Test
    public void test3(){
        double a=Math.sin(Math.PI*30/180);
        System.out.println("   "+a+"    "+Math.PI);
    }
    @Test
    public void test4(){
        int a=30%50;
        int b=-20%50;
        int c=-70%50;
        System.out.println("   "+a+"    "+b+"    "+c);
    }



    @Test
    public void test5(){
        Vector2D v2=new Vector2D(100,100);
        Vector2D v1=new Vector2D(100,0);
        Vector2D v3=new Vector2D(0,100);
        System.out.println("angle v2 :"+Vector2D.angle(v2,v1)/Math.PI*180);
        System.out.println("angle v1 :"+Vector2D.angle(v2,v1)/Math.PI*180);
    }

    @Test
    public void test6(){
        MathVector2D.Vector v2 =new MathVector2D.Vector(100,100);
        MathVector2D.Vector v1 =new MathVector2D.Vector(100,-100);
        MathVector2D.Vector v3 =new MathVector2D.Vector(-100,1);
        System.out.println("length:"+v2.getLength());
        System.out.println("angle2:"+v2.getAngle());
        System.out.println("angle1:"+v1.getAngle());
        System.out.println("angle3:"+v3.getAngle());
        v2.scaleTo(50);
        System.out.println("length:"+v2.getLength()+"   "+v2);
        v2.addAngle(45);
        System.out.println("length:"+v2.getLength()+"   "+v2);
    }

    @Test
    public void test7(){
        int int1 = (int) (Math.log10(1)/Math.log10(1024));
        int int2 = (int) (Math.log10(200)/Math.log10(1024));
        int int3 = (int) (Math.log10(500)/Math.log10(1024));
        int int4 = (int) (Math.log10(1023)/Math.log10(1024));
        int int5 = (int) (Math.log10(1024)/Math.log10(1024));
        int int6 = (int) (Math.log10(1500)/Math.log10(1024));
        int int7 = (int) (Math.log10(2048)/Math.log10(1024));
        int int8 = (int) (Math.log10(1024*1024)/Math.log10(1024));
        int int9 = (int) (Math.log10(1024*1024*1024)/Math.log10(1024));

        System.out.println("int1:"+int1);
        System.out.println("int2:"+int2);
        System.out.println("int3:"+int3);
        System.out.println("int4:"+int4);
        System.out.println("int5:"+int5);
        System.out.println("int6:"+int6);
        System.out.println("int7:"+int7);
        System.out.println("int8:"+int8);
        System.out.println("int9:"+int9);


    }





}
