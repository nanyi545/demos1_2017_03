package test1.nh.com.demos1.fundamental.hook.fox_ams.hook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/6/7.
 */

class ActivityThreadHandlerCallback implements Handler.Callback {

    Handler mBase;

    public ActivityThreadHandlerCallback(Handler base) {
        mBase = base;
    }

    @Override
    public boolean handleMessage(Message msg) {

        Log.i("IActivityManagerHandler","handleMessage --> msg.what:"+msg.what);
        switch (msg.what) {
            // ActivityThread里面 "LAUNCH_ACTIVITY" 这个字段的值是100
            // 本来使用反射的方式获取最好, 这里为了简便直接使用硬编码
            case 100:
                handleLaunchActivity(msg);
                break;
        }
        mBase.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
        // 这里简单起见,直接取出TargetActivity;

        Log.i("IActivityManagerHandler","hook --> handle launch");

        Object obj = msg.obj;
        // 根据源码:
        // 这个对象是 ActivityClientRecord 类型
        // 我们修改它的intent字段为我们原来保存的即可.
        // switch (msg.what) {
        //      case LAUNCH_ACTIVITY: {
        //          Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
        //          final ActivityClientRecord r = (ActivityClientRecord) msg.obj;

        //          r.packageInfo = getPackageInfoNoCheck(
        //                  r.activityInfo.applicationInfo, r.compatInfo);
        //         handleLaunchActivity(r, null);


        try {
            // 把替身恢复成真身
            Field intent = obj.getClass().getDeclaredField("intent");
            Log.i("IActivityManagerHandler","object-->"+obj.getClass().getCanonicalName());//   object-->android.app.ActivityThread.ActivityClientRecord
            intent.setAccessible(true);
            Intent raw = (Intent) intent.get(obj);
            Log.i("IActivityManagerHandler","raw intent --> raw pkg:"+raw.getComponent().getPackageName()+"   cls:"+raw.getComponent().getClassName());

            Intent target = raw.getParcelableExtra(AMSHookHelper.EXTRA_TARGET_INTENT);
            Log.i("IActivityManagerHandler","target intent --> target pkg:"+target.getComponent().getPackageName()+"   cls:"+target.getComponent().getClassName());

            raw.setComponent(target.getComponent());

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}