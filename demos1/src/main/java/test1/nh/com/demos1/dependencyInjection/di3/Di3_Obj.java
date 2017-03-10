package test1.nh.com.demos1.dependencyInjection.di3;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by Administrator on 16-1-7.
 */
public class Di3_Obj {

    public String name;

    @Inject
    public Di3_Obj(String name){
        this.name=name;
        Log.i("BBB","DI3 object "+name+" created in:"+Thread.currentThread().getName());
    }

}
