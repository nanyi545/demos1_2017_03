package test1.nh.com.demos1.dependencyInjection.di2.components;

/**
 * Created by Administrator on 16-1-7.
 */

import javax.inject.Singleton;

import dagger.Component;
import test1.nh.com.demos1.activities.MainActivity_from;
import test1.nh.com.demos1.dependencyInjection.di2.modules.Obj_Module;


@Singleton
@Component(modules={Obj_Module.class})
public interface ObjComponent {
    // you can not have 2 interfaces that inject to the same target, in the case below --> JavaFundaFrag mFrag
    // since dependencyInjection.di1.components.DiComponent already specified JavaFundaFrag as injection target
    // if you want more stuff injected, you need to add Obj_Module.class to the modules in DiComponent
    //
    //     void inject1(JavaFundaFrag mFrag);


    void inject(MainActivity_from mFrag);
}
