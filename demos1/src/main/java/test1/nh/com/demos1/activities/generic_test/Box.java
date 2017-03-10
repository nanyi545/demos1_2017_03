package test1.nh.com.demos1.activities.generic_test;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/19.
 */
public class Box<T> {

    private T t;

    public void set(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }


    public Box(T t) {
        this.t = t;
    }

    public <U extends Number > void inspect(U u){
        Log.i("ddd","T: " + t.getClass().getName());
        Log.i("ddd","U: " + u.getClass().getName());
    }

//    <T extends B1 & B2 & B3>
//    A type variable with multiple bounds is a subtype of all the types listed in the bound. If one of the bounds is a class, it must be specified first. For example:
//
//    Class A { /* ... */ }
//    interface B { /* ... */ }
//    interface C { /* ... */ }




}
