package test1.nh.com.demos1.dependencyInjection.di2.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import test1.nh.com.demos1.utils.TestObject;
import test1.nh.com.demos1.utils.TestObject2;

/**
 * Created by Administrator on 16-1-7.
 */
@Module
public class Obj_Module {

    @Provides
    @Singleton
    public TestObject getObj_0(){
        return new TestObject(10);
    }

    @Provides
    @Singleton
    public TestObject2 getObj_2(){
        return new TestObject2(new TestObject(8) );
    }

}
