package test1.nh.com.demos1.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.util.Log;

import com.baidu.apistore.sdk.ApiStoreSDK;
import com.codemonkeylabs.fpslibrary.TinyDancer;

import test1.nh.com.demos1.dependencyInjection.di1.components.DaggerDiComponent;
import test1.nh.com.demos1.dependencyInjection.di1.components.DiComponent;
import test1.nh.com.demos1.dependencyInjection.di3.components.DaggerDi3Component;
import test1.nh.com.demos1.dependencyInjection.di3.components.Di3Component;
import test1.nh.com.demos1.testDemo.di_pack.components.DaggerDataComponent;
import test1.nh.com.demos1.testDemo.di_pack.components.IComponent;

import static android.support.multidex.MultiDex.install;

/**
 * Created by Administrator on 15-11-23.
 */
public class DMapplication extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        install(this);
    }

    private Runnable checkStateRunnable=new Runnable(){
        @Override
        @TargetApi(16)
        public void run() {
            while(true){
                ActivityManager.getMyMemoryState(new ActivityManager.RunningAppProcessInfo());
                //  ActivityManager.getRunningAppProcesses() **** !!!
            }
        }
    };




    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        String memory_state=null;
        if (level < ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE)
            memory_state = "<5:TRIM_MEMORY_RUNNING_MODERATE";
        else if (level < ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW)
            memory_state = "5-10:TRIM_MEMORY_RUNNING_LOW";
        else if (level < ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL)
            memory_state = "10-15:TRIM_MEMORY_RUNNING_CRITICAL";
        else if (level < ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN)
            memory_state = "15-20:TRIM_MEMORY_UI_HIDDEN";
        else if (level < ComponentCallbacks2.TRIM_MEMORY_BACKGROUND)
            memory_state = "20-40:TRIM_MEMORY_BACKGROUND";
        else if (level < ComponentCallbacks2.TRIM_MEMORY_MODERATE)
            memory_state = "40-60:TRIM_MEMORY_MODERATE";
        else if (level < ComponentCallbacks2.TRIM_MEMORY_COMPLETE)
            memory_state = "60-80:TRIM_MEMORY_COMPLETE";

        Log.i("CCC", "memory_state:"+memory_state);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private static DMapplication me;

    public static DMapplication getInstance() {
        return me;
    }


    //----------test of serialized obejct
    private TestObject obj1;

    public TestObject getObj1() {
        return obj1;
    }

    private TestObject2 obj2;

    public TestObject2 getObj2() {
        return obj2;
    }


    //---- dagger----
    DiComponent component;

    public DiComponent getComponent() {
        return component;
    }

    Di3Component component1;

    public Di3Component getDi3Component() {
        return component1;
    }


    // test of dagger for DI in unit tests
    IComponent componentData;

    public IComponent getDataComponent() {
        return componentData;
    }

    public void setDateComponent(IComponent component) {
        componentData = component;
    }


    @Override
    public void onCreate() {
        Log.i("CCC", "ONCREATE");


        TinyDancer.create().show(this); //  FPS checker...


        me = this;
        int aa = 11;
        obj1 = new TestObject(aa);
        obj2 = new TestObject2(new TestObject(aa));  //
//        Log.i("AAA",""+(obj2.getObj1()==obj1));
        super.onCreate();

        ApiStoreSDK.init(this, "3e9a52050ceb54ea20f97d6c374cafe3");  // baidu api key

        // dagger:DI
        component = DaggerDiComponent.builder().build();
        //component= DaggerDiComponent.builder().networkApiModule(new NetworkApiModule()).build();  //与上一行等价
        // DaggerDiComponent中需要调用 networkApiModule()/obj_Module()  设置module, as above line
        // 如果不设置，像上上行那样，会自动设置module（调用module类的空构造方法）
        // @component中设置了几个module 就需要设置几个module
        component1 = DaggerDi3Component.builder().build();
        componentData = DaggerDataComponent.builder().build(); // 测试unit test with DI
    }
}
