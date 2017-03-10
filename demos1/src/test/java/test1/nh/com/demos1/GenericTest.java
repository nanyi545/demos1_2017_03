package test1.nh.com.demos1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import test1.nh.com.demos1.utils.collections.TestBean;
import test1.nh.com.demos1.utils.genericTest.GenericFactory;

/**
 * Created by Administrator on 15-12-30.
 */
public class GenericTest {


    @Before
    public void before(){

    }

    @After
    public void after(){

    }


    @Test
    public void test1(){
        List list = new ArrayList();
        list.add(new Integer(2));
        list.add("a String");
        System.out.println("0:"+list.get(0)+"   1:"+list.get(1));
        Integer integer = (Integer) list.get(0);  // casting is needed
        String string   = (String) list.get(1);
    }

    @Test
    public void test2(){
        GenericFactory<TestBean> testFactory=new GenericFactory(TestBean.class); // <> diamond operator, to specify generic
        TestBean tb1=testFactory.createInstance();
        System.out.println(tb1.toString());
    }


}
