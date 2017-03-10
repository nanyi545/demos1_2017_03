package test1.nh.com.demos1.testDemo.di_pack.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import test1.nh.com.demos1.testDemo.models.ItestData;
import test1.nh.com.demos1.testDemo.models.NetData;

/**
 * Created by Administrator on 16-1-11.
 */
@Module
public class DataModule {


    @Provides
    @Singleton
    public ItestData getDataSrc(){
        return new NetData();
    }



}
