package test1.nh.com.demos1.utils;

import java.io.Serializable;

/**
 * Created by Administrator on 15-11-23.
 */
public class TestObject2 implements Serializable{

    public TestObject2(TestObject obj1){
        this.obj1=obj1;
    }

    private TestObject obj1;

    public TestObject getObj1() {
        return obj1;
    }

    public void setObj1(TestObject obj1) {
        this.obj1 = obj1;
    }
}
