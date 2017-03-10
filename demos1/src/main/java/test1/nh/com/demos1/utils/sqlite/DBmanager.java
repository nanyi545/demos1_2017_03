package test1.nh.com.demos1.utils.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 15-12-14.
 */
public class DBmanager {

    private DBhelper helper;
    private SQLiteDatabase db;

    public DBmanager(Context context){
        helper=new DBhelper(context);
        db=helper.getWritableDatabase();
    }


    /************************************************************************
     * --------------db operations:-------------------
     * @param name
     * @param age
     * @return
     */
    public boolean insertContact(String name, int age)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("age", age);
        db.insert("contacts", null, contentValues);  // "contacts"=table name
        return true;
    }

    public void printdb()
    {
        Cursor c=db.rawQuery("SELECT * FROM contacts",null);
        while (c.moveToNext()){
            Log.i("AAA", "id=" + c.getInt(c.getColumnIndex("id")) + "  name=" + c.getString(1) + "  age=" + c.getInt(2));
        }
    }

    public void closeDB(){
        db.close();
    }



    public List<Person4DB> getPersons() {   // return persons older than 20
        // select * from users where _id = {userId}
        Cursor c = db.rawQuery("SELECT * FROM contacts WHERE age >= ?", new String[]{"20"});
        List<Person4DB> list=new ArrayList<Person4DB>();
        while (c.moveToNext()){
//            Log.i("AAA", "id=" + c.getInt(c.getColumnIndex("id")) + "  name=" + c.getString(1) + "  age=" + c.getInt(2));
            list.add(new Person4DB(c.getInt(2),c.getString(1)));
        }
        return list;
    }

    /**
     *  使用callable来封装  IO操作比如getPersons()
     *  返回一个callable，IO操作覆盖call方法  ---->   把这个callable传给observer，
     *   使得callable的call中的IO操作得以通过  .subscribeOn(Schedulers.computation()); 设置在workerthread中
     * @return
     */
    public Callable<List<Person4DB>> getPersonsRx() {
        return new Callable<List<Person4DB>>() {
            @Override
            public List<Person4DB> call() {
                Cursor c = db.rawQuery("SELECT * FROM contacts WHERE age >= ?", new String[]{"20"});
                List<Person4DB> list=new ArrayList<Person4DB>();
                while (c.moveToNext()){
//            Log.i("AAA", "id=" + c.getInt(c.getColumnIndex("id")) + "  name=" + c.getString(1) + "  age=" + c.getInt(2));
                    list.add(new Person4DB(c.getInt(2),c.getString(1)));
                }
//                Log.i("AAA",Thread.currentThread().getName()+"--in callable--"+list.toString()); // RxComputationThreadPool-1
                return list;
            }
        };
    }

    public Observable<List<Person4DB>> getPersonsRx2() {
        return makeObservable(getPersonsRx())
                .subscribeOn(Schedulers.computation()); // note: do not use Schedulers.io() // io on worker thread
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch(Exception ex) {
                            Log.e("AAA", "Error reading from the database", ex);
                        }
                    }
                });
    }




}
