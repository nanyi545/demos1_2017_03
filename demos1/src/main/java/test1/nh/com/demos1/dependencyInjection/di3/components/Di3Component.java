package test1.nh.com.demos1.dependencyInjection.di3.components;

import javax.inject.Singleton;

import dagger.Component;
import test1.nh.com.demos1.dependencyInjection.di3.Di3_Obj;
import test1.nh.com.demos1.dependencyInjection.di3.Di3_ParentObj;
import test1.nh.com.demos1.dependencyInjection.di3.modules.Di3_ObjModule;

/**
 * Created by Administrator on 16-1-7.
 */


@Singleton
@Component(modules = {Di3_ObjModule.class})
public interface Di3Component {
    Di3_Obj provideDi3Component();   // constructor injection
    Di3_ParentObj provideDi3ParentComponent();   // constructor injection

}


