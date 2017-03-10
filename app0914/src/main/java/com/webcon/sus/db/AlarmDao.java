package com.webcon.sus.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.webcon.sus.entity.AlarmInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 报警信息表Dao
 * 
 * @author Vieboo
 * 
 */
public class AlarmDao {

	private DatabaseHelper dbHelper;

	public AlarmDao(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	/**
	 * 插入数据
	 */
	public void alarmInsertOrRelaceData(String userName,
			List<AlarmInfo> alarmList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		for (int i = 0; i < alarmList.size(); i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT OR REPLACE INTO ");
			sb.append(DBConstant.ALARMTABLENAME);
			sb.append("(");
			sb.append(DBConstant.ALARMUSERNAME);
			sb.append(", ");
			sb.append(DBConstant.ALARMEQUIPMENTID);
			sb.append(", ");
			sb.append(DBConstant.ALARMEQUIPMENTNAME);
			sb.append(", ");
			sb.append(DBConstant.ALARMDATETIME);
			sb.append(", ");
			sb.append(DBConstant.ALARMTYPE);
			sb.append(", ");
			sb.append(DBConstant.ALARMINFO);
			sb.append(", ");
			sb.append(DBConstant.ALARMSOLVE);
			sb.append(", ");
			sb.append(DBConstant.DEALUSERNICK);
			sb.append(", ");
			sb.append(DBConstant.DEALTIME);
			sb.append(")");
			sb.append("VALUES");
			sb.append("('");
			sb.append(userName);
			sb.append("', '");
			sb.append(alarmList.get(i).getAlarmEqId());
			sb.append("', '");
			sb.append(alarmList.get(i).getAlarmEqName());
			sb.append("', '");
			sb.append(alarmList.get(i).getAlarmDateTime());
			sb.append("', ");
			sb.append(alarmList.get(i).getAlarmType());
			sb.append(", '");
			sb.append(alarmList.get(i).getAlarmInfo());
			sb.append("', ");
			sb.append(alarmList.get(i).getIsSolve());
			sb.append(", '");
			sb.append(alarmList.get(i).getDealUserNick());
			sb.append("', ");
			sb.append(alarmList.get(i).getDealTime());
			sb.append(")");
			// System.out.println("AlarmInsertSql------------>" +
			// sb.toString());
			try {
				db.execSQL(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public void alarmInsertOrRelaceData(String userName, AlarmInfo alarmInfo) {
		List<AlarmInfo> alarmInfoList = new ArrayList<AlarmInfo>();
		alarmInfoList.add(alarmInfo);
		alarmInsertOrRelaceData(userName, alarmInfoList);
	}

	/**
	 * 删除数据
	 */
	public boolean alarmDeleteData(String userName, List<AlarmInfo> alarmList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			for (int i = 0; i < alarmList.size(); i++) {
				StringBuilder sb = new StringBuilder();
				sb.append("DELETE FROM [");
				sb.append(DBConstant.ALARMTABLENAME);
				sb.append("] WHERE ");
				sb.append(DBConstant.ALARMUSERNAME);
				sb.append(" = '");
				sb.append(userName);
				sb.append("' AND ");
				sb.append(DBConstant.ALARMEQUIPMENTID);
				sb.append(" = '");
				sb.append(alarmList.get(i).getAlarmEqId());
				sb.append("' AND ");
				sb.append(DBConstant.ALARMDATETIME);
				sb.append(" = '");
				sb.append(alarmList.get(i).getAlarmDateTime());
				sb.append("' AND ");
				sb.append(DBConstant.ALARMTYPE);
				sb.append(" = ");
				sb.append(alarmList.get(i).getAlarmType());
				db.execSQL(sb.toString());
				// System.out.println("AlarmDeleteSql------------>" +
				// sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
		return true;
	}

	/**
	 * 删除某个摄像头的报警数据
	 */
	public boolean alarmDeleteEquipmentData(String equipmentID) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {

			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM [");
			sb.append(DBConstant.ALARMTABLENAME);
			sb.append("] WHERE ");
			sb.append(DBConstant.ALARMEQUIPMENTID);
			sb.append(" = '");
			sb.append(equipmentID);
			sb.append("' ");
			db.execSQL(sb.toString());
			// System.out.println("AlarmDeleteSql------------>" +
			// sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
		return true;
	}

	/**
	 * 查询所有报警信息(ID、降序查询)
	 */
	public List<AlarmInfo> alarmSelectAllData(String userName) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.beginTransaction();
		List<AlarmInfo> alarmInfoList = new ArrayList<AlarmInfo>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ");
		sb.append(DBConstant.ALARMTABLENAME);
		sb.append(" WHERE ");
		sb.append(DBConstant.ALARMUSERNAME);
		sb.append(" = '");
		sb.append(userName);
		sb.append("' ORDER BY ");
		sb.append(DBConstant.ALARMSOLVE);
		sb.append(" ASC, ");
		sb.append(DBConstant.ALARMDATETIME);
		sb.append(" DESC");
		// System.out.println("AlarmSelectAllSql------------>" + sb.toString());
		Cursor cursor = db.rawQuery(sb.toString(), null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			AlarmInfo alarmInfo = new AlarmInfo();
			alarmInfo.setAlarmEqId(cursor.getString(cursor
					.getColumnIndex(DBConstant.ALARMEQUIPMENTID)));
			alarmInfo.setAlarmEqName(cursor.getString(cursor
					.getColumnIndex(DBConstant.ALARMEQUIPMENTNAME)));
			alarmInfo.setAlarmDateTime(cursor.getString(cursor
					.getColumnIndex(DBConstant.ALARMDATETIME)));
			alarmInfo.setAlarmType(cursor.getShort(cursor
					.getColumnIndex(DBConstant.ALARMTYPE)));
			alarmInfo.setAlarmInfo(cursor.getString(cursor
					.getColumnIndex(DBConstant.ALARMINFO)));
			alarmInfo.setIsSolve(cursor.getShort(cursor
					.getColumnIndex(DBConstant.ALARMSOLVE)));
			alarmInfo.setDealUserNick(cursor.getString(cursor
					.getColumnIndex(DBConstant.DEALUSERNICK)));
			alarmInfo.setDealTime(cursor.getLong(cursor
					.getColumnIndex(DBConstant.DEALTIME)));
			alarmInfoList.add(alarmInfo);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		cursor.close();
		db.close();
		return alarmInfoList;
	}
	
	public List<AlarmInfo> deleteAlarmSelectAllData(String userName) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.beginTransaction();
		List<AlarmInfo> alarmInfoList = new ArrayList<AlarmInfo>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ");
		sb.append(DBConstant.DELETEALARMTABLENAME);
		sb.append(" WHERE ");
		sb.append(DBConstant.DELETEALARMUSERNAME);
		sb.append(" = '");
		sb.append(userName);
		sb.append("' ORDER BY ");
		sb.append(DBConstant.DELETEALARMSOLVE);
		sb.append(" ASC, ");
		sb.append(DBConstant.DELETEALARMDATETIME);
		sb.append(" DESC");
		// System.out.println("AlarmSelectAllSql------------>" + sb.toString());
		Cursor cursor = db.rawQuery(sb.toString(), null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			AlarmInfo alarmInfo = new AlarmInfo();
			alarmInfo.setAlarmEqId(cursor.getString(cursor
					.getColumnIndex(DBConstant.DELETEALARMEQUIPMENTID)));
			alarmInfo.setAlarmEqName(cursor.getString(cursor
					.getColumnIndex(DBConstant.DELETEALARMEQUIPMENTNAME)));
			alarmInfo.setAlarmDateTime(cursor.getString(cursor
					.getColumnIndex(DBConstant.DELETEALARMDATETIME)));
			alarmInfo.setAlarmType(cursor.getShort(cursor
					.getColumnIndex(DBConstant.DELETEALARMTYPE)));
			alarmInfo.setAlarmInfo(cursor.getString(cursor
					.getColumnIndex(DBConstant.DELETEALARMINFO)));
			alarmInfo.setIsSolve(cursor.getShort(cursor
					.getColumnIndex(DBConstant.DELETEALARMSOLVE)));
			alarmInfo.setDealUserNick(cursor.getString(cursor
					.getColumnIndex(DBConstant.DELETEDEALUSERNICK)));
			alarmInfo.setDealTime(cursor.getLong(cursor
					.getColumnIndex(DBConstant.DELETEDEALTIME)));
			alarmInfoList.add(alarmInfo);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		cursor.close();
		db.close();
		return alarmInfoList;
	}
	
	

	/**
	 * 更新设置报警信息已处理状态
	 */
	public void alarmInfoUpdate(String userName, List<AlarmInfo> alarmInfoList) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.beginTransaction();
		for (int i = 0; i < alarmInfoList.size(); i++) {
			if (alarmInfoList.get(i).getIsSolve() == 1) {
				continue;
			}
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE ");
			sb.append(DBConstant.ALARMTABLENAME);
			sb.append(" SET ");
			sb.append(DBConstant.ALARMSOLVE);
			sb.append(" = ");
			sb.append((short) 1);
			sb.append(" , ");
			sb.append(DBConstant.DEALTIME);
			sb.append(" = ");
			sb.append(alarmInfoList.get(i).getDealTime());
			sb.append(" , ");
			sb.append(DBConstant.DEALUSERNICK);
			sb.append(" = '");
			sb.append(alarmInfoList.get(i).getDealUserNick());
			sb.append("' WHERE ");
			sb.append(DBConstant.ALARMUSERNAME);
			sb.append(" = '");
			sb.append(userName);
			sb.append("' AND ");
			sb.append(DBConstant.ALARMEQUIPMENTID);
			sb.append(" = '");
			sb.append(alarmInfoList.get(i).getAlarmEqId());
			sb.append("' AND ");
			sb.append(DBConstant.ALARMDATETIME);
			sb.append(" = '");
			sb.append(alarmInfoList.get(i).getAlarmDateTime());
			sb.append("' AND ");
			sb.append(DBConstant.ALARMTYPE);
			sb.append(" = ");
			sb.append(alarmInfoList.get(i).getAlarmType());
			// System.out.println("AlarmIsExistsql------------>" +
			// sb.toString());
			try {
				db.execSQL(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	/**
	 * 查询当前用户在报警表中未处理的报警信息的有多少
	 */
	public long selectCountNoResolved(String userName) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		long count = 0;
		try {
			String sql = "select count(*) from " + DBConstant.ALARMTABLENAME
					+ " where " + DBConstant.ALARMUSERNAME + "='" + userName
					+ "' and " + DBConstant.ALARMSOLVE + " = " + 0;
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			count = cursor.getLong(0);
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public void deleteAlarmInsertOrRelaceData(String userName,
			List<AlarmInfo> alarmList) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		for (int i = 0; i < alarmList.size(); i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT OR REPLACE INTO ");
			sb.append(DBConstant.DELETEALARMTABLENAME);
			sb.append("(");
			sb.append(DBConstant.DELETEALARMUSERNAME);
			sb.append(", ");
			sb.append(DBConstant.DELETEALARMEQUIPMENTID);
			sb.append(", ");
			sb.append(DBConstant.DELETEALARMEQUIPMENTNAME);
			sb.append(", ");
			sb.append(DBConstant.DELETEALARMDATETIME);
			sb.append(", ");
			sb.append(DBConstant.DELETEALARMTYPE);
			sb.append(", ");
			sb.append(DBConstant.DELETEALARMINFO);
			sb.append(", ");
			sb.append(DBConstant.DELETEALARMSOLVE);
			sb.append(", ");
			sb.append(DBConstant.DELETEDEALUSERNICK);
			sb.append(", ");
			sb.append(DBConstant.DELETEDEALTIME);
			sb.append(")");
			sb.append("VALUES");
			sb.append("('");
			sb.append(userName);
			sb.append("', '");
			sb.append(alarmList.get(i).getAlarmEqId());
			sb.append("', '");
			sb.append(alarmList.get(i).getAlarmEqName());
			sb.append("', '");
			sb.append(alarmList.get(i).getAlarmDateTime());
			sb.append("', ");
			sb.append(alarmList.get(i).getAlarmType());
			sb.append(", '");
			sb.append(alarmList.get(i).getAlarmInfo());
			sb.append("', ");
			sb.append(alarmList.get(i).getIsSolve());
			sb.append(", '");
			sb.append(alarmList.get(i).getDealUserNick());
			sb.append("', ");
			sb.append(alarmList.get(i).getDealTime());
			sb.append(")");
			// System.out.println("AlarmInsertSql------------>" +
			// sb.toString());
			try {
				db.execSQL(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("保存暂时删除的记录："+i);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public boolean deleteAlarmDeleteData(String userName) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
				StringBuilder sb = new StringBuilder();
				sb.append("DELETE FROM [");
				sb.append(DBConstant.DELETEALARMTABLENAME);
				sb.append("] WHERE ");
				sb.append(DBConstant.DELETEALARMUSERNAME);
				sb.append(" = '");
				sb.append(userName);
				db.execSQL(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
		return true;
	}
}
