package test1.nh.com.demos1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test1.nh.com.demos1.test.entities.Father;
import test1.nh.com.demos1.test.entities.Son;

/**
 * Created by Administrator on 15-12-30.
 */
public class DynamicBinding {


    @Before
    public void before() {
    }

    @After
    public void after() {
    }


    @Test
    public void test1() {
        Father instance = new Son();
        instance.method();
        System.out.println(instance.name);//  动态绑定只是针对对象的方法，对于属性无效。因为属性不能被重写。
    }


}
