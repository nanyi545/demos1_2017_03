package test1.nh.com.demos1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 15-12-30.
 */
public class ConcurrentTest {




    @Before
    public void before(){
    }

    @After
    public void after(){
    }



    private ThreadLocal myThreadLocal = new ThreadLocal<String>() {
        @Override protected String initialValue() {
            return "This is the initial value";
        }
    };

    public class MyRunnable implements Runnable {
        public MyRunnable(String runnableName){this.runnableName=runnableName;}
        public MyRunnable(String runnableName,boolean reset){
            this.runnableName=runnableName;
            this.resetThreadLocal=reset;
        }

        private String runnableName;
        private boolean resetThreadLocal=false;
        public ThreadLocal<String> myThreadLocal1=myThreadLocal;
        @Override
        public void run() {
            if (resetThreadLocal) myThreadLocal1.set("modified string");
            System.out.println(runnableName+myThreadLocal1.get());
        }
    }


    @Test
    public void testThreadLocal1(){
        new Thread(new MyRunnable("thread1")).start();
        new Thread(new MyRunnable("thread2")).start();
    }

    @Test
    public void testThreadLocal2(){
        new Thread(new MyRunnable("thread1",true)).start();
        new Thread(new MyRunnable("thread2",false)).start();
    }




    public class DeadLockDemo{
        Object lockA=new Object();
        Object lockB=new Object();
        public void print1(){
            System.out.println("1:...");
            synchronized (lockA){
                System.out.println("1:--AAA--");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (lockB){
                System.out.println("1:--BBB--");
            }
        }
        public void print2(){
            System.out.println("2:...");
            synchronized (lockB){
                System.out.println("2:--BBB--");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            synchronized (lockA){
                System.out.println("2:--AAA--");
            }
        }
    }

    @Test
    public void testDeadLock(){
        final DeadLockDemo dlock1=new DeadLockDemo();
        new Thread( ){
            @Override
            public void run() {
                dlock1.print1();
            }
        }.start();
        new Thread( ){
            @Override
            public void run() {
                dlock1.print2();
            }
        }.start();
    }




    @Test
    public void testThreading(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<10;i++){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print("call:"+i+"   thread:"+Thread.currentThread().getName());
                }

            }
        }).start();


    }






}
