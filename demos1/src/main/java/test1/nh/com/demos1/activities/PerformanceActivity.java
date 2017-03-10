package test1.nh.com.demos1.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;

import test1.nh.com.demos1.R;

public class PerformanceActivity extends AppCompatActivity {

    public static void start(Context context){
        final Intent intent=new Intent(context,PerformanceActivity.class);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        //  temp
//        this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }




    public void testList(View v){

        new Thread(new Runnable() {
            @Override
            public void run() {

                LinkedList<Integer> linkedList=new LinkedList();
                ArrayList<Integer> arrayList=new ArrayList();

                for (int i=0;i<300000;i++){
                    linkedList.add(i);
                    arrayList.add(i);
                }
                long startTime=System.currentTimeMillis();
                while (!linkedList.isEmpty()){
                    linkedList.pollFirst();
                }
                long endTime=System.currentTimeMillis();

                Log.i("eee","linked-list pollFirst():"+(endTime-startTime));


                startTime=System.currentTimeMillis();
                while (!arrayList.isEmpty()){
                    arrayList.remove(0);
                }
                endTime=System.currentTimeMillis();
                Log.i("eee","array-list  remove(0):"+(endTime-startTime));


            }
        }).start();



    }



    public void dowork(View v){
        new Thread(new Runnable(){
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("AAA","thread waiting---"+Thread.currentThread().getName());
                }
            }
        }).start();
    }


    public void cal_fib(View v){
        new Thread(testRunnable).start();
    }

    private Runnable testRunnable=new Runnable(){
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
//            Trace.beginSection("Data Structures");
            Log.i("AAA",""+calculate_fib(200));
//            Trace.endSection();
        }
    };

    private int calculate_fib(int a1){
        Log.i("AAA","--"+Thread.currentThread().getName()+"--"+a1);
        if (a1<2) return 1;
        else return calculate_fib(a1-1)+calculate_fib(a1-2);
    }









}
