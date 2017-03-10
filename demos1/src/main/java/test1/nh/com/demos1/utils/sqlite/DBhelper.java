package test1.nh.com.demos1.utils.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 15-12-14.
 */
public class DBhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="sqliteDemo.db";
    private static int DATABASE_VER=1;

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    // --called when the db is created at the first time---
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key autoincrement, name text,age integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}


