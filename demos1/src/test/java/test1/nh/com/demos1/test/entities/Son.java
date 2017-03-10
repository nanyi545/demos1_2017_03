package test1.nh.com.demos1.test.entities;

/**
 * Created by Administrator on 16-4-4.
 */
public class Son extends Father {

    public String name = "孩子属性";
    @Override
    public void method() {
        System.out.println("子类方法"+this.getClass());
    }
}
