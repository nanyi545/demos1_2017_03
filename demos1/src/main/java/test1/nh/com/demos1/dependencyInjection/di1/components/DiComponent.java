package test1.nh.com.demos1.dependencyInjection.di1.components;

import javax.inject.Singleton;

import dagger.Component;
import test1.nh.com.demos1.dependencyInjection.di1.modules.NetworkApiModule;
import test1.nh.com.demos1.dependencyInjection.di2.modules.Obj_Module;
import test1.nh.com.demos1.fragments.JavaFundaFrag;

/**
 * Created by Administrator on 15-12-29.
 */

@Singleton
@Component(modules={NetworkApiModule.class,Obj_Module.class})  // dependencies =TestObject.class 有啥用？？
public interface DiComponent {
    void inject(JavaFundaFrag mJavaFundaFrag);  //--> field injection
}
