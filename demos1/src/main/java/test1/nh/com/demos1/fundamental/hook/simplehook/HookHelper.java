package test1.nh.com.demos1.fundamental.hook.simplehook;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/6/5.
 */

public class HookHelper {

    public static void attachContext() throws Exception{
        // 先获取到当前的ActivityThread对象
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
        currentActivityThreadMethod.setAccessible(true);
        //currentActivityThread是一个static函数所以可以直接invoke，不需要带实例参数
        Object currentActivityThread = currentActivityThreadMethod.invoke(null);
        // 拿到原始的 mInstrumentation字段
        Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);
        // 创建代理对象
        Instrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);
        // 偷梁换柱
        mInstrumentationField.set(currentActivityThread, evilInstrumentation);
    }


    private static final String TAG="hook_helper";

    public static void attachContext(Activity activity) throws Exception{
        Class<?> activityClass = activity.getClass();
        // 拿到原始的 mInstrumentation字段
//        Field mInstrumentationField = activityClass.getDeclaredField("mInstrumentation");
        Field mInstrumentationField = Activity.class.getDeclaredField("mInstrumentation");
        Log.e(TAG,"mInstrumentationField:" + mInstrumentationField);

        mInstrumentationField.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(activity);
        Log.e(TAG,"originInstrumentation:" + mInstrumentation);

        // 创建代理对象
        Instrumentation evilInstrumentation = new EvilInstrumentation(mInstrumentation);
        Log.e(TAG,"new  Instrumentation:" + evilInstrumentation);
        // 偷梁换柱
        mInstrumentationField.set(activity, evilInstrumentation);
    }



}
