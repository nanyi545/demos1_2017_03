package test1.nh.com.demos1.mvpSQL.pack.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import test1.nh.com.demos1.utils.sqlite.Person4DB;

/**
 * Created by Administrator on 16-1-26.
 *
 * this class manages the "contact" table in the DB
 *
 * Your Table Java classes should contain no public methods: only package protected methods
 * (which are accessed exclusively by your Helper, located in the same package) and private methods.
 * No other classes should ever access these Table classes directly.
 *
 */
public class ContactTable {


    //----- actual DB operations-----
    private List<Person4DB> getPersons(SQLiteDatabase db) {
        // select * from users where _id = {userId}
        Log.i("AAA", "getPersons   " + Thread.currentThread().getName());
        Cursor c = db.rawQuery("SELECT * FROM contacts WHERE age >= ?", new String[]{"0"});
        List<Person4DB> list = new ArrayList<Person4DB>();
        while (c.moveToNext()) {
            // manual sleeping..... imitating slow IO operation---
            try {
                Thread.sleep(500);
                Log.i("AAA", "looping..." + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add(new Person4DB(c.getInt(2), c.getString(1)));
        }
        return list;
    }

    private long addPerson(Person4DB p1,SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("name", p1.getName());
        values.put("age", p1.getAge());
        try {
            Log.i("AAA", "adding..." + Thread.currentThread().getName());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long rowsNum = db.insert(DBmanager.TABLE_NAME, "name", values);
        return rowsNum;
    }



    // wrap operations in callables
    protected Callable<List<Person4DB>> getPersonsCallable(final SQLiteDatabase db) {
        return new Callable<List<Person4DB>>() {
            @Override
            public List<Person4DB> call() {
                return getPersons(db);
            }
        };
    }

    protected Callable<Long> addPersonCallable(final Person4DB p1, final SQLiteDatabase db) {
        return new Callable() {
            @Override
            public Long call() {
                return addPerson(p1,db);
            }
        };
    }


}
