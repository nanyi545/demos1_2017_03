package test1.nh.com.demos1.activities.tracking.tracking_utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import test1.nh.com.demos1.utils.DMapplication;

/**
 * Created by Administrator on 2017/5/20.
 */

public class TrackingDB extends SQLiteOpenHelper {

    public static TrackingDB db;

    public static TrackingDB getInstance(Context c){
        synchronized (TrackingDB.class){
            if (db==null){
                db=new TrackingDB(c,DB_NAME,null,1);
                return db;
            } else {
                return db;
            }
        }
    }

    private static final String DB_NAME="TRACKING";
    private static final String TABLE_NAME="LOGS";
    private static final String TABLE_FIELD_NAME="log_name",TABLE_FIELD_START="log_start_time",TABLE_FIELD_END="log_end_time",TABLE_FIELD_TYPE="log_type",TABLE_FIELD_SUCCESS="log_ret";

    private TrackingDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " +TABLE_NAME+" "+
                        "(id integer primary key, "+TABLE_FIELD_NAME+" text,"+TABLE_FIELD_START+" integer,"+TABLE_FIELD_END+" integer, "+TABLE_FIELD_TYPE+" integer, "+TABLE_FIELD_SUCCESS+" integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public synchronized void insertContact(LogItem item) {
        Log.i(DMapplication.DEM0_APP,"insert to db in:"+Thread.currentThread().getName());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_FIELD_NAME, item.getLogItemName());
        contentValues.put(TABLE_FIELD_START, item.getStartTime());
        contentValues.put(TABLE_FIELD_END, item.getEndTime());
        contentValues.put(TABLE_FIELD_TYPE, item.getType());
        contentValues.put(TABLE_FIELD_SUCCESS, item.isSuccess()?1:0);
        db.insert(TABLE_NAME, null, contentValues);
    }



    private LogItem readItem(Cursor res){
        long startTime=res.getLong(res.getColumnIndex(TABLE_FIELD_START));
        long endTime=res.getLong(res.getColumnIndex(TABLE_FIELD_END));
        String name=res.getString(res.getColumnIndex(TABLE_FIELD_NAME));
        int type=res.getInt(res.getColumnIndex(TABLE_FIELD_TYPE));
        boolean success =(res.getInt(res.getColumnIndex(TABLE_FIELD_SUCCESS))==1);
        return new LogItem(type,name,startTime,endTime,success);
    }



    public TrackingManager.UploaderPact restorePrevSession(){
        SQLiteDatabase db = this.getWritableDatabase();
        LogSession prevSession=new LogSession();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            int type=res.getInt(res.getColumnIndex(TABLE_FIELD_TYPE));
            switch (type){
                case LogItem.VIEW_CHANGE:
                    prevSession.viewLogs.add(readItem(res));
                    break;
                case LogItem.IO_NETWORK:
                    prevSession.netIoLogs.add(readItem(res));
                    break;
                case LogItem.USER_ACTION:
                    prevSession.userActionLogs.add(readItem(res));
                    break;
                case LogItem.HANDLE_THROWABLE:
                    prevSession.errorLogs.add(readItem(res));
                    break;
            }
            res.moveToNext();
        }
        long[] duration = prevSession.computeDuration();
        Gson gson=new Gson();
        db.delete(TABLE_NAME,null,null);
        Log.i(DMapplication.DEM0_APP,"prevSession json str:"+gson.toJson(prevSession));
        TrackingManager.UploaderPact pact=new TrackingManager.UploaderPact(duration,prevSession,prevSession.getDeviceType(),prevSession.getDeviceInfo());
        return pact;
    }



}
