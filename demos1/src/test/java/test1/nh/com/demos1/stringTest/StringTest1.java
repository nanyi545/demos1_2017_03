package test1.nh.com.demos1.stringTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test1.nh.com.demos1.activities.horizontalScroll.ItemPicker;

/**
 * Created by Administrator on 15-12-30.
 */
public class StringTest1 {


    @Before
    public void before(){
    }

    @After
    public void after(){
    }


    @Test
    public void test1(){
        String str1="012345654321";
        int indexOf3=str1.indexOf("3");
        System.out.println(""+indexOf3);
    }

    @Test
    public void test2(){
        String str1="012345654321";
        int indexOfA=str1.indexOf("A");
        System.out.println(""+indexOfA);  // gives -1
    }

    @Test
    public void test3(){
        String str1="012345654321";
        char char1=str1.toCharArray()[0];
        System.out.println(""+(char1==(""+0).toCharArray()[0]));
    }


    @Test
    public void test4(){
        String str1="1 2 3 4 5";
        int length=str1.length();
        String[] strs=str1.split(" ");   // split
        for (int a=0;a<strs.length;a++){
            System.out.println(""+a+":"+strs[a]);
        }
    }

    @Test
    public void test5(){
        String str1="1 2 3 4 5";
        String[] strs=str1.split(" ");
        for (int a=0;a<strs.length;a++){
            int temp=Integer.parseInt(strs[a]);   // convert string 2 int
            System.out.println("string:"+a+":"+strs[a]);
            System.out.println("int:"+a+":"+temp);
        }
    }

    @Test
    public void test6(){
        ItemPicker.Formatter formatter=new ItemPicker.DateFormatter();
        System.out.println(formatter.format(1));
        System.out.println(formatter.format(4));
    }

    @Test
    public void test7(){
        System.out.println(String.format("%.2f", 12312.9913f)+"元");
        System.out.println(String.format("%.2f元", 12312.9913f));
    }



    @Test
    public void test8(){
        float max=100f;
        float progress=1.333f;
        System.out.println(String.format("%.2f/%.2f", progress, max));
    }



}
