package test1.nh.com.demos1;


import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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


    @Test
    public void mapTest1(){
        HashMap<String ,String > map1=new HashMap();
        map1.put("a","1");
        map1.put("b","2");
        map1.put("c","3");
        map1.put("d","4");
        map1.put("dd","4");
        map1.put("dddd","4");
        map1.put("dddddd","4");
        map1.put("ddddddddd","4");

        try {
            Field tableField=HashMap.class.getDeclaredField("table");
            tableField.setAccessible(true);
            Map.Entry[] table= (Map.Entry[]) tableField.get(map1);

            int size=table.length;
            for (int i=0;i<size;i++){
                Log.i("ccc","i:"+i+"    "+table[i]+"   "+(table[i]!=null?table[i].getClass().getName():"null"));
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }






}
