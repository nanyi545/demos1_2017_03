package test1.nh.com.demos1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import test1.nh.com.demos1.utils.collections.QuickSorter;
import test1.nh.com.demos1.utils.collections.TestBean;

/**
 * Created by Administrator on 15-12-30.
 */
public class QuickSortTest {


    private <T> void dumpArray(T[] arr){
        for (int aa=0;aa<arr.length;aa++) System.out.println(arr[aa].toString());
    }

    private TestBean tb1=new TestBean(1);
    private TestBean tb2=new TestBean(2);
    private TestBean tb3=new TestBean(3);
    private TestBean tb4=new TestBean(4);
    private TestBean tb5=new TestBean(5);
    private List<Object> myList=new ArrayList();

    Comparator c=new TestBean.TestBeanComparator();
    private TestBean[] myArr={tb1,tb5,tb4,tb3,tb5,tb2};

    @Before
    public void before(){
        myList.add(tb2);
        myList.add(tb4);
        myList.add(tb1);
        myList.add(tb1);
        myList.add(tb5);
        myList.add(tb5);
        myList.add(tb1);
        myList.add(tb3);
    }

    @After
    public void after(){
    }


    @Test
    public void testArray(){
        Integer[] array=new Integer[0];
        System.out.println("equals(null)?"+array.equals(null)+"----null == ?"+(null==array));
        dumpArray(array);
    }



    @Test
    public void test2array1(){
    }

    @Test
    public void test2array2(){
    }


    @Test
    public void testQuickSort(){
        System.out.println(myList);
        QuickSorter.quickSort(myList, c);
        System.out.println(myList);
    }



}
