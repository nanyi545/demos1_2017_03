package test1.nh.com.demos1.activities.generic_test;

import android.util.Log;

/**
 * Created by Administrator on 2016/12/19.
 */
public class ExtendedBox<T,V > extends Box<T> {


    private V v;
    public void setV(V v){
     this.v=v;
    }
    public V getV(){
        return v;
    }




    public ExtendedBox(T t,V v) {
        super(t);
        this.v=v;
    }

    @Override
    public <U extends Number> void inspect(U u) {
        super.inspect(u);
        Log.i("ddd","V: " + v.getClass().getName());
    }
}
