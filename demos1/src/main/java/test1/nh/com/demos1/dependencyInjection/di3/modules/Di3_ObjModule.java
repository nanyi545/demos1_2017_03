package test1.nh.com.demos1.dependencyInjection.di3.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import test1.nh.com.demos1.dependencyInjection.di3.Di3_Obj;
import test1.nh.com.demos1.dependencyInjection.di3.Di3_ParentObj;

/**
 * Created by Administrator on 16-1-7.
 */
@Module
public class Di3_ObjModule {

    @Provides
    @Singleton
    public Di3_Obj getDi3Obj(){
        return new Di3_Obj("DAGGER");
    }

    @Provides
    @Singleton
    public Di3_ParentObj getDi3ParentObj(){
        return new Di3_ParentObj(new Di3_Obj("DAGGER  II"));
    }



}
