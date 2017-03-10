package test1.nh.com.demos1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 15-12-30.
 */
public class ConcurTest {

    private void dumpArray(int[] ints){
        int intLength=ints.length;
        System.out.println("length:"+intLength);
        for (int ii=0;ii<intLength;ii++){
            System.out.print(" "+ints[ii]);
        }
        System.out.println("end of array");
    }

    private class MyRun implements Runnable{
        private int times;
        private String tag;

        public MyRun(int times,String tag){
            this.times=times;
            this.tag=tag;
        }

        @Override
        public void run() {
            while (times>0){
                times--;
                System.out.println(""+times+"th print:myRunable"+tag);
            }
        }
    }


    private class MyCall implements Callable<String> {
        String str;
        public MyCall(String str){this.str=str;}
        @Override
        public String call() throws Exception {
            for (int aa=0;aa<7;aa++) {  // Just to demo a long running task .
                System.out.println(str+" "+aa+"th run in Callable");
                Thread.sleep(100);
            }
            return str;
        }
    }



    @Before
    public void before(){
    }

    @After
    public void after(){
    }


    @Test
    public void test_execute(){
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        threadPool.execute(new MyRun(3,"ExecutorService"));

        Executor threadPool2 = Executors.newFixedThreadPool(3);
        threadPool2.execute(new MyRun(4,"Executor"));

        ExecutorService executorService3 = Executors.newScheduledThreadPool(10);

    }

    @Test
    public void test_submit(){

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new MyCall("Call1"));

        try {
            System.out.println("Started..");
            System.out.println(future.get(6000, TimeUnit.MILLISECONDS));   // return String "Call1"
            System.out.println("Finished!");
        } catch (TimeoutException e) {
            future.cancel(true);
            System.out.println("Timeout Terminated!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();



    }






}
