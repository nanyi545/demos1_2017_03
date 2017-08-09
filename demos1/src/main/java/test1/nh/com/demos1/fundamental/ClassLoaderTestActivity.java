package test1.nh.com.demos1.fundamental;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import test1.nh.com.demos1.R;
import test1.nh.com.demos1.fundamental.hook.amshook.AmsPmsHookActivity;
import test1.nh.com.demos1.fundamental.hook.fox_ams.FoxAmsActivity;
import test1.nh.com.demos1.fundamental.hook.simplehook.HookHelper;
import test1.nh.com.demos1.fundamental.hook.binderhook.TestBinderHookActivity;

public class ClassLoaderTestActivity extends Activity {

    public static void start(Context c){
        Intent i=new Intent(c,ClassLoaderTestActivity.class);
        c.startActivity(i);
    }

    private static final String TAG="class_loader";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate");
        setContentView(R.layout.activity_class_loader_test);
        ClassLoader classLoader = getClassLoader();
        int i=0;
        if (classLoader != null){
            Log.i(TAG, "[onCreate] classLoader " + (i++) + " : " + classLoader.toString());
            while (classLoader.getParent()!=null){
                classLoader = classLoader.getParent();
                Log.i(TAG,"[onCreate] classLoader " + (i++) + " : " + classLoader.toString());
            }
        }

        DexClassLoader dclsl;
        PathClassLoader pclsl;


        try {
            HookHelper.attachContext(ClassLoaderTestActivity.this);    //  this will hook the Activity.startActivity() method
        } catch (Exception e) {
            Log.e(TAG,Log.getStackTraceString(e));
            e.printStackTrace();
        }
        
    }



    public void startAct(View v){
        // 注意这里使用的ApplicationContext 启动的Activity
        // 因为Activity对象的startActivity使用的并不是ContextImpl的mInstrumentation
        // 而是自己的mInstrumentation, 如果你需要这样, 可以自己Hook
        // 比较简单, 直接替换这个Activity的此字段即可.
//        Intent intent = new Intent(this,LoadingTestActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        getApplicationContext().startActivity(intent);
        /**
         *  hooked by {@link HookHelper#attachContext(Activity)}
         *
         */
        Intent intent = new Intent(this,TestBinderHookActivity.class);
        startActivity(intent);
    }

    public void startAct2(View v){
        Intent intent = new Intent(this,AmsPmsHookActivity.class);
        startActivity(intent);
    }
    public void startAct3(View v){
        Intent intent = new Intent(this,FoxAmsActivity.class);
        startActivity(intent);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        Log.i(TAG,"attachBaseContext");
        try {
            // 在这里进行Hook
//            HookHelper.attachContext();                                //  this will hook the Application.startActivity() method
//            HookHelper.attachContext(ClassLoaderTestActivity.this);    //  this will hook the Activity.startActivity() method   !!!  does not work here--->  mInstrumentation instance not yet created here ... should do this in on create...
        } catch (Exception e) {
            Log.e(TAG,Log.getStackTraceString(e));
            e.printStackTrace();
        }
    }




}
