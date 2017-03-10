package test1.nh.com.demos1.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import pl.droidsonroids.gif.GifImageView;
import rx.Observer;
import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.DrawerActivity;
import test1.nh.com.demos1.annotations.MyAnnotation;
import test1.nh.com.demos1.callBackDemo.CounterWithCallBack;
import test1.nh.com.demos1.callBackDemo.CounterWithObserver;
import test1.nh.com.demos1.dependencyInjection.di1.NetworkApi;
import test1.nh.com.demos1.dependencyInjection.di3.Di3_Obj;
import test1.nh.com.demos1.dependencyInjection.di3.Di3_ParentObj;
import test1.nh.com.demos1.entities.TestReflection;
import test1.nh.com.demos1.entities.TestReflection2;
import test1.nh.com.demos1.utils.DMapplication;
import test1.nh.com.demos1.utils.EventbBusEvent;
import test1.nh.com.demos1.utils.TestObject;
import test1.nh.com.demos1.utils.TestObject2;
import test1.nh.com.demos1.utils.TestObject3;

/**
 * Created by Administrator on 15-9-30.
 */
public class JavaFundaFrag extends Fragment {

    private TestObject obj1;
    private TestObject2 obj2;
    private TextView textView3;

    private TextView textView4;

    Timer timer1 = new Timer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        Intent i1=getActivity().getIntent();
        // method1
        Bundle bun1 = i1.getExtras();
        if (null != bun1) {
            obj1 = (TestObject) bun1.getSerializable("object1");
            obj2 = (TestObject2) bun1.getSerializable("object2");
        }
        // method2
//        obj1= (TestObject) i1.getSerializableExtra("object1");
    }

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    //empty constructor
    public JavaFundaFrag(){
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static JavaFundaFrag newInstance(int sectionNumber) {
        JavaFundaFrag fragment = new JavaFundaFrag();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @TargetApi(11)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get section id from intent
        // getArg--return the Bundle
        int section_number=getArguments().getInt(ARG_SECTION_NUMBER);

        View rootView = inflater.inflate(R.layout.fragment_fundamantal_drawer, container, false);

        String objLength;
        boolean b1=(obj1== DMapplication.getInstance().getObj1());
        if (null!=obj1){
            objLength=""+obj1.getLength();
        }
        else {objLength="";}

        TextView textView=(TextView)rootView.findViewById(R.id.container_big_tv1);
        textView.setText("varifiy clone:    this object length"+objLength+"-----allication object length"+DMapplication.getInstance().getObj1().getLength()+"---the 2 are the same??"+b1);

        // seri
        TextView textView2=(TextView)rootView.findViewById(R.id.container_big_tv2);
        textView2.setText("deep/shalow? if false it is deep clone: "+(obj2.getObj1()==DMapplication.getInstance().getObj2().getObj1()));


        textView3=(TextView)rootView.findViewById(R.id.container_big_tv3);
//        // post e1, 看收到的object是否科隆
//        EventbBusEvent e1 = new EventbBusEvent(DMapplication.getInstance().getObj1());
//        EventBus.getDefault().post(e1);


        /**
         * test of poly-morphism
         */
        textView4=(TextView)rootView.findViewById(R.id.container_big_tv4);
        TestObject obj1=new TestObject3(11);
        obj1.talk();  //  ----obj3--
//        obj1.talk2(); // obj1不能access talk2()
        TestObject3 obj2=(TestObject3)obj1; //
        obj1.talk();  //  ----obj3--
        obj2.talk();
        obj2.talk2();// cast 成object3 后，obj2 能access talk2()

        textView4.setText(""+(obj1==obj2));  // obj1与obj2指向相同地址



        /**
         * test of switch case
         */
        TextView textView5=(TextView)rootView.findViewById(R.id.container_big_tv5);
        String a1="1-2-3";
        switch (a1){
            case "123":
                textView5.setText("is 123");
                break;
            case "456":
                textView5.setText("is 456");
                break;
            default:
                textView5.setText("neither 123 nor 456");
                break;
        }


        /**
         * test of reflection---------1
         */
        TextView textView6=(TextView)rootView.findViewById(R.id.container_big_tv6);

        PackageManager pm = getActivity().getPackageManager();
        Method[] methods = pm.getClass().getMethods();
        for (int i = 0; i < methods.length; i++)
        {
            Log.d("REFLECTION", methods[i].getName());
        }
//        pm.INSTALL_EXTERNAL; // is set as annotation hide--> can not be accessed in the normal way
        // bypass the hide annotation to get this field
        try {
            Field field_INSTALL_EXTERNAL = pm.getClass().getField("INSTALL_EXTERNAL");
            int test_ref=(Integer)field_INSTALL_EXTERNAL.get(pm.getClass());
            textView6.setText(""+test_ref);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        /**
         * test of reflection---------2
         */
        TestReflection.print();
        TestReflection testRef1=new TestReflection();

        TestReflection2 testRef2=new TestReflection2();


        try {

            //----------change private static fields -----------
            Field field_FINAL_STRING = TestReflection.class.getDeclaredField("FINAL_INT");
            field_FINAL_STRING.setAccessible(true);

            Log.i("AAA", "before" + (int) field_FINAL_STRING.get(null));
            field_FINAL_STRING.setInt(null, 555);
            Log.i("AAA", "after" + (int) field_FINAL_STRING.get(null));
            TestReflection.print();  // still 789, because reflection works in runtime

            TestReflection t1=(TestReflection)Class.forName("test1.nh.com.demos1.entities.TestReflection").newInstance();
            Method methodprint=t1.getClass().getDeclaredMethod("print", (Class[]) null);
            methodprint.invoke(t1,new Object[]{}); // call the method in runtime...


            Method getModifierMethod=field_FINAL_STRING.getClass().getDeclaredMethod("getModifiers",(Class[])null);
            int modifierType=(int)getModifierMethod.invoke(field_FINAL_STRING,new Object[]{});
            Log.i("AAA", "modifier"+modifierType);  // private static final=26

            Field field_FINAL_STRING2 = TestReflection.class.getDeclaredField("FINAL_STRING2");
            int modifierType2=(int)getModifierMethod.invoke(field_FINAL_STRING2,new Object[]{});   //*** instead of null
            Log.i("AAA", "modifier2" + modifierType2);  // public static final=225

            //----------change private non-static fields -----------
            testRef2.print();
            Field field2_int = TestReflection2.class.getDeclaredField("PRI_INT");
            Field field2_string = TestReflection2.class.getDeclaredField("PRI_STR");

            field2_int.setAccessible(true);
            field2_string.setAccessible(true);

            Log.i("AAA", "before  int:" + field2_int.get(testRef2) + "  str:" + field2_string.get(testRef2));
            field2_int.setInt(testRef2, 555);
            Object obj=new String("new private string");
            field2_string.set(testRef2, obj);
            Log.i("AAA", "after  int" + field2_int.get(testRef2) + "  str:" + field2_string.get(testRef2));
            testRef2.print();


        } catch (Exception e) {
            e.printStackTrace();
            Log.i("AAA",Log.getStackTraceString(e));
        }
        TestReflection.print();


        /**
         * use of custom annotation
         */
        MyAnnoClass m1=new MyAnnoClass();

        Annotation[] annotations=m1.getClass().getAnnotations();
        Log.i("AAA","annotations---length" +annotations.length);

        //-----method 1-----
        for (Annotation annotation : annotations)
        {
            if (annotation instanceof MyAnnotation)
            {
                MyAnnotation myAnnotation = (MyAnnotation) annotation;
                Log.i("AAA","annotations---age :" + myAnnotation.age());
                Log.i("AAA","annotations---names :" + myAnnotation.newNames());
            }
        }
        //----method 2-----
        Class<MyAnnoClass> cls3 = MyAnnoClass.class;
        MyAnnotation anno = cls3.getAnnotation(MyAnnotation.class); // OK
        Log.i("AAA","annotation-getAnnotation---age :" + anno.age());
        Log.i("AAA","annotation-getAnnotation---names :" + anno.newNames());


        /**
         * call back VS rx-java
         */
        //--------callback approach-----------
        CounterWithCallBack counter1=new CounterWithCallBack(20,"counter1","COUNTERTAG");
        counter1.startCountDown(); // start countdown
        CounterWithCallBack counter2=new CounterWithCallBack(20,"counter2","COUNTERTAG");
        counter2.registerFinishCallback(new CounterWithCallBack.FinishCallBack(){
            @Override
            public void callUponFinish() {
                Log.i("COUNTERTAG","counter2----calling from the UI");
            }
        });
        counter2.startCountDown();// start countdown  --with finish callback


        //--------rxjava approach-----------
        CounterWithObserver counter3=new CounterWithObserver(20,"counter3","COUNTERTAG_OBS");
        counter3.startCountDown();

        CounterWithObserver counter4=new CounterWithObserver(20,"counter4","COUNTERTAG_OBS");

        Observer finishObserver = new Observer<String>() {
            @Override
            public void onCompleted() { Log.i("COUNTERTAG_OBS","counter4--completed--calling from the UI");}
            @Override
            public void onError(Throwable e) {}
            @Override
            public void onNext(String s) {Log.i("COUNTERTAG_OBS","counter4--next--calling from the UI"+s);}
        };
        counter4.setObserver(finishObserver);

        counter4.startCountDown();


        /**
         * daggerII test1 ---- field injection
         */
        DMapplication.getInstance().getComponent().inject(this);
        boolean injected=networkApi==null?false:true;
        Log.i("BBB","dagger success?"+injected);
        Log.i("BBB","valid user?????"+networkApi.validateUsers(null,"111"));
        NetworkApi networkApi2=new NetworkApi();
        NetworkApi networkApi4=new NetworkApi();
        Log.i("BBB","injected == newed?? "+(networkApi2==networkApi));   // false
        Log.i("BBB","injected == injected?? "+(networkApi3==networkApi));// true
        Log.i("BBB","newed == newed?? "+(networkApi2==networkApi4));     // false
        // because in the injection module NetworkApi is specified as Singleton --> singleton pattern is used in INJECTION
        // objects created by new operator does not follow Singleton


        boolean objInjected=null==obj_0?false:true;
        Log.i("BBB","object injected success:"+objInjected);
        Log.i("BBB","object contains:"+obj_0.getLength());
        boolean objInjected2=null==obj_2?false:true;
        Log.i("BBB","object2 injected success:"+objInjected2);
        Log.i("BBB","object2 contains:"+obj_2.getObj1().getLength());


        /**
         * daggerII test2 ----  constructor injection
         */
        in_di3_obj=DMapplication.getInstance().getDi3Component().provideDi3Component();//  in_di3_obj is created here ...
        Di3_Obj di3_obj=new Di3_Obj("18k");
        Log.i("BBB","di3 object name:"+di3_obj.name);
        Log.i("BBB","inj di3 object name:"+in_di3_obj.name);
        in_di3_Pobj=DMapplication.getInstance().getDi3Component().provideDi3ParentComponent();
        Log.i("BBB","inj di3 Pobject name:"+in_di3_Pobj.child.name);


        /**
         * timer test:
         */
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("BBB","TASK1");
            }
        }, 3000L);
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("BBB","TASK2");
            }
        }, 1000L);


        /**
         * use of Future... use a GIF image to show UI thread jitter
         *
         * ****   future.get will！！！ block UI thread, needs to put it in a separate thread
         *
         */
        giv1=(GifImageView)rootView.findViewById(R.id.gif_testFuture);
        giv1.setImageResource(R.drawable.shiver_me_timbers);

        b_future=(Button)rootView.findViewById(R.id.container_big_bn_future);

        b_future.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ExecutorService executor = Executors.newSingleThreadExecutor();


                final Future future = executor.submit(new Runnable(){
                    @Override
                    public void run() {
                        for (int aa=0;aa<9;aa++) {  // Just to demo a long running task .
                            Log.i("CCC",""+aa+"th sub call inCALLABLE");
                            try {
                                Thread.sleep(100);   // if total time is less than 3 secs --> future.get() will not timeOut
//                                Thread.sleep(500);   // if total time is larger than 3 secs --> future.get() will not timeOut
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            future.get(3000, TimeUnit.MILLISECONDS);  // this will block UI thread, timeOut in 3 sec,
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            Log.i("CCC", "timeout from CALLABLE");
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });


        /**
         * for  VS for-each
         */

        new Thread(){
            @Override
            public void run() {
                int n=30000000;
                int[] aa=new int[n];

                long long1=System.nanoTime();
                for(int a=0;a<n;a++){aa[a]=8;}
                long long2=System.nanoTime();
                Log.i("NANOTIME","int array:for loop"+(long2-long1)/1000000000f);

                long1=System.nanoTime();
                for(int temp:aa){temp=8;}
                long2=System.nanoTime();
                Log.i("NANOTIME","int array:for each loop"+(long2-long1)/1000000000f);


                n=3000000;
                List<Integer> list=new ArrayList(n);
                for(int a=0;a<n;a++){list.add(a);}

                long1=System.nanoTime();
                for(int a=0;a<n;a++){list.set(a,8);}
                long2=System.nanoTime();
                Log.i("NANOTIME","int ArrayList:for loop"+(long2-long1)/1000000000f);


                long1=System.nanoTime();
                for(int temp:list){temp=8;}
                long2=System.nanoTime();
                Log.i("NANOTIME","int ArrayList:for each loop"+(long2-long1)/1000000000f);



            }
        }.start();


        return rootView;
    }

    Di3_Obj in_di3_obj;
    Di3_ParentObj in_di3_Pobj;

    @Inject NetworkApi networkApi;
    @Inject NetworkApi networkApi3;

    @Inject TestObject obj_0;
    @Inject TestObject2 obj_2;

    @MyAnnotation(age=28,newNames={"Jenkov", "Peterson","jiangq"})
    public class MyAnnoClass {
    }


    Button b_future;
    GifImageView giv1;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //如果attach到drawerActivity上 设置activity label
        try {    //  called by DrawerActivity
            ((DrawerActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {
            e.printStackTrace();
//            Log.i("AAA", "Fragment called not by DrawerActivity");
        }

    }



    public void onEventMainThread(EventbBusEvent e1){
//        Log.i("AAA","received");
        boolean a1=(e1.obj1==DMapplication.getInstance().getObj1());
        textView3.setText("event bus传递的引用还是克隆？true的话是引用:"+a1);
    }



}
