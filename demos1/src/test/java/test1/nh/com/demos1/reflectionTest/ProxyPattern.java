package test1.nh.com.demos1.reflectionTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import test1.nh.com.demos1.fundamental.cls_loader.dynamicproxy.Shopping;
import test1.nh.com.demos1.fundamental.cls_loader.dynamicproxy.ShoppingHandler;
import test1.nh.com.demos1.fundamental.cls_loader.dynamicproxy.ShoppingImpl;

/**
 * Created by Administrator on 15-12-30.
 */
public class ProxyPattern {
    InvocationHandler iHandler;


    @Before
    public void before(){
    }

    @After
    public void after(){
    }


    @Test
    public void test1(){

        Shopping women = new ShoppingImpl();

        // 正常购物
        System.out.println("------------regular shopping------------");
        System.out.println(Arrays.toString(women.doShopping(100)));

        // 招代理
        women = (Shopping) Proxy.newProxyInstance(Shopping.class.getClassLoader(),
                women.getClass().getInterfaces(), new ShoppingHandler(women));
        System.out.println("------------proxy shopping------------");
        System.out.println(Arrays.toString(women.doShopping(100)));

    }


}
