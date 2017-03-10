package test1.nh.com.demos1.dependencyInjection.di1.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import test1.nh.com.demos1.dependencyInjection.di1.NetworkApi;

/**
 * Created by Administrator on 15-12-29.
 */
@Module
public class NetworkApiModule {

    @Provides
    @Singleton
    public NetworkApi getNetwork(){
        return new NetworkApi();
    }


}
