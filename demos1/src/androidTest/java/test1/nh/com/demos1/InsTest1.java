package test1.nh.com.demos1;


import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import test1.nh.com.demos1.utils.TestObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Administrator on 15-12-31.
 */
@RunWith(AndroidJUnit4.class)
public class InsTest1 {

    private TestObject testObj1;

    @Before
    public void before(){
        testObj1=new TestObject(10);
    }

    @After
    public void after(){
        testObj1=null;
    }


    @Test
    public void test1(){
        testObj1.talk();
        assertEquals(10,testObj1.getLength());

    }

    @Test
    public void test2(){
        testObj1.talk();
        assertNotEquals(8,testObj1.getLength());
    }



}
