package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import test1.nh.com.demos1.R;
import test1.nh.com.demos1.utils.rxtest.Course;
import test1.nh.com.demos1.utils.rxtest.Student;


// *1 Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
// *2 Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
// ------------------------------------------------------------------------
// *3 Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。
// 行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，
// 因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
// -------------------------------------------------------------------------
// *4 Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，
// 例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，
// 否则 I/O 操作的等待时间会浪费 CPU。
// -------------------------------------------------------------------------


public class RXjavaActivity extends AppCompatActivity {


    public static void start(Context context) {
        final Intent intent = new Intent(context, RXjavaActivity.class);
        context.startActivity(intent);
    }

    Observer<String> observer;
    Observable observable;

    Button b1;
    Button b2;
    //sample5 ---------rxjava 防抖---------
    Observable<View> view_observable;
    Action1<Integer> myAction1=new Action1<Integer>() {
        private int total_clicks=0;
        @Override
        public void call(Integer integer) {
            Log.i("AAA", "observer5 total clicks="+(++total_clicks)); //
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        b1=(Button)findViewById(R.id.button1_rx);
        b2=(Button)findViewById(R.id.button2_rx);

        // simple sample1
        Log.i("AAA", "----------example 1--------");
        initObserver();
        initObservable();
        observable.subscribe(observer);


        // sample2--> schedule on different threads
        Log.i("AAA", "----------example 2---------");
        example2();


        // sample3--> map the input to other objects  ---delay a bit......ops...
        Log.i("AAA", "----------example 3---------");
        example3_map();


        // sample4 -->flatMap
        Log.i("AAA", "----------example 4---------");
        example4_flatMap();



        //sample5 ---------rxjava 防抖---------.
        example5_viewClick();


        //sample6--test of using array to pass parameter
        example6_arrayParameter();


        // example 7 test of action0/action1 to act as subscriber/observer
        example7();


        // rx-binding test
        example8();



    }







    private void example8() {
        RxView.clicks(b2) // 以 Observable 形式来反馈点击事件
                .throttleFirst(1500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void x) {
                        Log.i("AAA","rx binding!");}
                });
    }


    // example 7 test of action0/action1 to act as subscriber/observer
    private void example7() {
        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Log.i("AAA", "example7"+s);}
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {}
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.i("AAA", "example7---completed");
            }
        };

        // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
//        observable.subscribe(onNextAction);
        // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
//        observable.subscribe(onNextAction, onErrorAction);
        // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }


    private void example6_arrayParameter() {
        Observer<int[]> integerObserver=new Observer<int[]>() {
            @Override public void onCompleted() {

            }
            @Override public void onError(Throwable e) {

            }
            @Override public void onNext(int[] ints) {
                for (int aa=0;aa<ints.length;aa++){
                    Log.i("AAA","example6--"+aa+"th integer in the array:"+ints[aa]);
                }
            }
        };

        Observable integerObservable=Observable.create(new Observable.OnSubscribe<int[]>(){
            @Override
            public void call(Subscriber<? super int[]> subscriber) {
                int[] testInt={1,2,3,4,5};
                subscriber.onNext(testInt);
            }
        });

        integerObservable.subscribe(integerObserver);
    }


    //------？？？？？？ better understanding of this part is needed...-------------
    private void example5_viewClick() {
        view_observable= Observable.create(new Observable.OnSubscribe<View>(){
            @Override
            public void call(final Subscriber<? super View> subscriber) {  // called-->on subscribe  传入subscriber
                b1.setOnClickListener(new View.OnClickListener(){
                    private int clicks=0;
                    @Override
                    public void onClick(final View v) {
                        Log.i("AAA", "sample5--click--"+(++clicks));
                        subscriber.onNext(v);   //
                    }
                });
            }
        });
        view_observable.map(new Func1<View,Integer>(){
            @Override
            public Integer call(View view) {
                return 1;
            }
        }).throttleFirst(3, TimeUnit.SECONDS).subscribe(myAction1);
    }


    // flatMap--> flat and then map
    private void example4_flatMap() {
        Course c1 = new Course("chemistry");
        Course c2 = new Course("physics");
        Course c3 = new Course("JAVA");
        Course c4 = new Course("android");

        Student s1 = new Student("tom");
        s1.setCourses(new Course[]{c1, c2});

        Student s2 = new Student("david");
        s2.setCourses(new Course[]{c3, c4});

        Student s3 = new Student("ziyang");
        s3.setCourses(new Course[]{c1, c2});

        Subscriber<Course> sub1 = new Subscriber<Course>() {
            private int num_calls=0;
            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Course course) {
                Log.i("AAA", "observer4" + course.getName()+"  calls:"+(num_calls++));
                // finally called 6 times--->
                // -->  they are generated and then "flated" and then sent to the final subscriber:sub1
            }

            @Override
            public void onCompleted() {
            }
        };

        Observable.from(new Student[]{s1, s2, s3})
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(sub1);
    }


    //   map: 事件对象直接变换
    private void example3_map() {
        Observable.just("this is a string input")
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        Log.i("AAA", "example3--first map func1 in thread" + Thread.currentThread().getName());
                        return s.getBytes().length;
                    }
                })
                .subscribeOn(Schedulers.io()) // 指定subscribe 发生在IO线程上---Observable.OnSubscribe()的线程
//                .subscribeOn(AndroidSchedulers.mainThread()) // 指定subscribe 发生在UI线程上---Observable.OnSubscribe()的线程
                .observeOn(AndroidSchedulers.mainThread())   // 多次切换线程时候，使用observeOn

                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer int_intermediate) {
                        Log.i("AAA", "example3--second map func1 in thread"+int_intermediate+"---" + Thread.currentThread().getName());
                        return 999;
                    }
                })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i("AAA", " observer 3--> number of bytes in int:" + integer +"--"+ Thread.currentThread().getName()); //
                    }
                });
    }
    ////  .subscribeOn  -->  return the source Observable modified so that its subscriptions happen on the specified Scheduler


    private void example2() {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io()) // 指定subscribe发生在IO线程上---Observable.OnSubscribe()的线程
//        指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
                .observeOn(AndroidSchedulers.mainThread())  // 指定observer/subscriber 回调的线程  // main
//                .observeOn(Schedulers.newThread())  // 指定observer/subscriber 回调的线程   //  RxNewThreadScheduler-1
                .take(3)    // 选择前三个item
//                .throttleFirst(500, TimeUnit.MILLISECONDS)  // 防抖设置, 500ms内收到的 无效
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.i("AAA", "observer2 " + integer + " in thread:" + Thread.currentThread().getName()); //
                    }
                });
    }

    private void initObservable() {
        /**
         * 以下三种方法初始化一个observable
         */
//-----------------------------------method 1
        observable= Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });


//------------------------------------method 2
//        observable=Observable.just("Hello","Hi","ALOHA"); // 依次调用 onNext onNext onNext onCompleted


//------------------------------------method 3
//        String[] words = {"Hello", "Hi", "ALOHA"};
//        observable = Observable.from(words);
    }


    private void initObserver() {
        observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.i("AAA", "obs1 completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("AAA", "obs1 onerror");
            }

            @Override
            public void onNext(String s) {
                Log.i("AAA", "obs1 onnext" + s);
            }
        };
    }

//    observable.subscribe(observer);
    
}
