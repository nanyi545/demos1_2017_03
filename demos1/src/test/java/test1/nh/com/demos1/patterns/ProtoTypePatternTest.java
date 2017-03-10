package test1.nh.com.demos1.patterns;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import test1.nh.com.demos1.activities.patterns.ProtoTypePattern;
import test1.nh.com.demos1.utils.collections.TestBean;
import test1.nh.com.demos1.utils.genericTest.GenericFactory;

/**
 * Created by Administrator on 15-12-30.
 */
public class ProtoTypePatternTest {

    ProtoTypePattern p1;
    ProtoTypePattern p1_clone;

    @Before
    public void before(){
        p1=new ProtoTypePattern();
        p1.setTestInt(11);
        p1.setStr("hehe");
        p1_clone=p1.clone();
    }

    @After
    public void after(){

    }


    @Test
    public void test1(){
        System.out.println("p1:"+p1.toString());
        System.out.println("p1_clone:"+p1_clone.toString());
    }



}
