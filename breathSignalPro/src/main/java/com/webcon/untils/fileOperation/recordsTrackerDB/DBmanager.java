package com.webcon.untils.fileOperation.recordsTrackerDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// ----is this needed--???--
/**
 * Created by Administrator on 16-2-18.
 *
 *  SQLite management : http://beust.com/weblog/2015/06/01/easy-sqlite-on-android-with-rxjava/
 *
 *  RecordsTable is responsible for "records" table, which documents all the records taken on the freescale platform
 */
public class DBmanager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recordsSQL.db";
    private static int DATABASE_VER = 1;
    public static final String TABLE_NAME = "records";
    private SQLiteDatabase db;

    public DBmanager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
//The first time this is called, the database will be opened and  onCreate,onUpgrade and/or onOpen will be called.
        db = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME +
                        " (id integer primary key autoincrement, startTime integer,endTime integer,sensorId integer)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
