package test1.nh.com.demos1.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.baidu.apistore.sdk.ApiStoreSDK;
import com.codemonkeylabs.fpslibrary.TinyDancer;

import test1.nh.com.demos1.activities.tracking.tracking_utils.CrashHandler;
import test1.nh.com.demos1.activities.tracking.tracking_utils.LogItem;
import test1.nh.com.demos1.activities.tracking.tracking_utils.LogNetIO;
import test1.nh.com.demos1.activities.tracking.tracking_utils.LogSession;
import test1.nh.com.demos1.activities.tracking.tracking_utils.LogUserAction;
import test1.nh.com.demos1.activities.tracking.tracking_utils.LogView;
import test1.nh.com.demos1.activities.tracking.tracking_utils.TrackingManager;
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


    public static final String DEM0_APP="DEM0_APP";


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

        Log.i(DEM0_APP, "onTrimMemory   memory_state:"+memory_state);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(DEM0_APP, "onTrimMemory   onLowMemory");
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
        CrashHandler handler=CrashHandler.getInstance(this);  //  init a crash handler

        Log.i(DEM0_APP, "ONCREATE");
        TrackingManager manager=TrackingManager.getInstance(this);
        manager.uploadPreviousSession();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                LogItem log1=new LogView("haha");
//
//                manager.addTrackingLog(new LogUserAction("button1"));
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                log1.setEndTime();
//                manager.addTrackingLog(log1);
//
//                LogItem log2=new LogView("hehe");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                log2.setEndTime();
//                manager.addTrackingLog(log2);
//                manager.addTrackingLog(new LogUserAction("button2"));
//                LogItem log3=new LogNetIO("api1");
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                log3.setSuccess(true);
//                manager.addTrackingLog(log3);
//                manager.addTrackingLog(new LogUserAction("exit"));
//            }
//        }).start();

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



        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.i("bbb","onActivityCreated:"+activity.toString());
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.i("bbb","onActivityStarted:"+activity.toString());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.i("bbb","onActivityResumed:"+activity.toString());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.i("bbb","onActivityPaused:"+activity.toString());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.i("bbb","onActivityStopped:"+activity.toString());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.i("bbb","onActivitySaveInstanceState:"+activity.toString());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.i("bbb","onActivityDestroyed:"+activity.toString());
            }
        });

        registerComponentCallbacks(new ComponentCallbacks() {
            @Override
            public void onConfigurationChanged(Configuration newConfig) {

            }

            @Override
            public void onLowMemory() {

            }
        });




    }
}
