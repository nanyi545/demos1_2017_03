package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import test1.nh.com.demos1.R;
import test1.nh.com.demos1.utils.sqlite.DBmanager;
import test1.nh.com.demos1.utils.sqlite.Person4DB;

public class SQLiteActivity extends AppCompatActivity {

    public static void start(Context context){
        final Intent intent=new Intent(context,SQLiteActivity.class);
        context.startActivity(intent);
    }



    private DBmanager dbmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        dbmanager=new DBmanager(this);


        //------read the database file location ------
        TextView tv1=(TextView)findViewById(R.id.tv1);
        File f = getDatabasePath("sqliteDemo.db");
        if (f != null)
            tv1.setText(f.getAbsolutePath());
        else tv1.setText("no file");

        Person4DB p1=new Person4DB(81,"ww");
        Log.i("AAA",""+p1.toString());


        // test callable------.call() can be accessed just like Runnable.run()
        try {
            List list=dbmanager.getPersonsRx().call();
            Log.i("AAA", "callable.call??" + list.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("AAA", "callable.call-->exception" );
        }




    }

    /**
     *    this is caller's responsibility to make sure that
     *    IO operations such as dbmanager.insertContact() as below
     *                                    is in worker thread.......
     */
    public void write1(View view){
        dbmanager.insertContact("wei wang", 28);
    }

    public void read1(View view){
        dbmanager.printdb();
    }

    public void getPersons(View view){
        Observable observable=dbmanager.getPersonsRx2();

        Observer<List<Person4DB>> observer = new Observer<List<Person4DB>>() {
            @Override
            public void onCompleted() {
                Log.i("AAA", "obs1 completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("AAA", "obs1 onerror");
            }

            @Override
            public void onNext(List list) {
                Log.i("AAA", "obs1 onnext---"+Thread.currentThread().getName()+"----" + list.toString());
            }
        };

        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(observer); // observe on UI thread

    }



}
