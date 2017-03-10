package test1.nh.com.demos1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.EnumMap;
import java.util.EnumSet;

import test1.nh.com.demos1.javaSE.enumTest.Light;

/**
 * Created by Administrator on 15-12-30.
 */
public class enumLightTest1 {


    @Before
    public void before(){

    }

    @After
    public void after(){

    }


    @Test
    public void test1(){
        Light[] allLight = Light.values(); // get an array of all enum results -->where is this function
        for (Light aLight : allLight) {
            System.out.println(" 当前灯 name ： " + aLight.name());
            System.out.println(" 当前灯 ordinal ： " + aLight.ordinal());
            System.out.println(" 当前灯： " + aLight);
        }
    }

    @Test
    public void test2(){
        // 1. 演示定义 EnumMap 对象， EnumMap 对象的构造函数需要参数传入 , 默认是 key 的类的类型
        EnumMap<Light, String> currEnumMap = new EnumMap<Light, String>(
                Light. class );
        currEnumMap.put(Light. RED , " 红灯 " );
        currEnumMap.put(Light. GREEN , " 绿灯 " );
        currEnumMap.put(Light. YELLOW , " 黄灯 " );

        // 2. 遍历对象
        for (Light aLight : Light.values ()) {
            System. out .println( "[key=" + aLight.name() + ",value="
                    + currEnumMap.get(aLight) + "]" );
        }

    }

    @Test
    public void test3() {
        EnumSet<Light> currEnumSet = EnumSet.allOf(Light.class);
        for (Light aLightSetElement : currEnumSet) {
            System.out.println(" 当前 EnumSet 中数据为： " + aLightSetElement);
        }
    }


}
