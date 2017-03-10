package test1.nh.com.demos1.testDemo.di_pack.modules;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import test1.nh.com.demos1.testDemo.models.ItestData;
import test1.nh.com.demos1.testDemo.models.NetData;

/**
 * Created by Administrator on 16-1-11.
 */
@Module
public class DebugDataModule {


    public DebugDataModule(boolean mockMode){
        this.mockMode=mockMode;
    }

    private final boolean mockMode;

    @Provides
    @Singleton
    ItestData getDataSrc() {
        if (mockMode) {
            return Mockito.mock(NetData.class);
//            return new FakeNetData();
        } else {
            return new NetData();
        }
    }



}
