package test1.nh.com.demos1.entities.testOveride;

import java.io.Serializable;

/**
 * Created by Administrator on 16-3-30.
 */
public class B extends A implements Runnable,Cloneable,Serializable{

    public B(int a) {
        super(a);
    }
    public B(){super();}


    @Override
    public void printThis() {
        super.printThis();
    }

    @Override
    protected void printThis(String str) {
        super.printThis(str);
    }

    @Override
    public void run() {}
}
