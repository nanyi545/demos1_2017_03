package test1.nh.com.demos1.testDemo.di_pack.components;

import javax.inject.Singleton;

import dagger.Component;
import test1.nh.com.demos1.testDemo.di_pack.modules.DataModule;

/**
 * Created by Administrator on 16-1-11.
 */
@Singleton
@Component(modules={DataModule.class})
public interface DataComponent extends IComponent{
}
