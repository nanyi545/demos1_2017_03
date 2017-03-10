package com.webcon.sus.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库Helper类
 * 
 * @author Vieboo
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, DBConstant.DBNAME, null, DBConstant.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(getAlarmTableSql()); // 创建报警信息表
		db.execSQL(getDeleteAlarmTableSql()); // 创建报警信息表
//		db.execSQL(getTrafficStatisticsTableSql()); // 创建流量信息表
	}

	private String getDeleteAlarmTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(DBConstant.DELETEALARMTABLENAME);
		sb.append("(");
		sb.append(DBConstant.DELETEALARMUSERNAME);
		sb.append(" TEXT, ");
		sb.append(DBConstant.DELETEALARMEQUIPMENTID);
		sb.append(" TEXT, ");
		sb.append(DBConstant.DELETEALARMEQUIPMENTNAME);
		sb.append(" TEXT, ");
		sb.append(DBConstant.DELETEALARMDATETIME);
		sb.append(" TEXT, ");
		sb.append(DBConstant.DELETEALARMTYPE);
		sb.append(" SHORT, ");
		sb.append(DBConstant.DELETEALARMINFO);
		sb.append(" TEXT, ");
		sb.append(DBConstant.DELETEALARMSOLVE);
		sb.append(" SHORT, ");
		sb.append(DBConstant.DELETEDEALUSERNICK);
		sb.append(" TEXT, ");
		sb.append(DBConstant.DELETEDEALTIME);
		sb.append(" INTEGER, ");
		sb.append("PRIMARY KEY(" + DBConstant.DELETEALARMUSERNAME + ", "
				+ DBConstant.DELETEALARMEQUIPMENTID + ", " + DBConstant.DELETEALARMDATETIME
				+ ", " + DBConstant.DELETEALARMTYPE + ")");
		sb.append(");");
		System.out.println("CreateAlarmTableSql------------>" + sb.toString());
		return sb.toString();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 升级报警信息表
		String columns = getColumnNames(db, DBConstant.ALARMTABLENAME);
		upgradeTables(db, DBConstant.ALARMTABLENAME, columns,
				getAlarmTableSql());
//		// 升级流量信息表
//		columns = getColumnNames(db, DBConstant.TRAFFICTABLENAME);
//		upgradeTables(db, DBConstant.TRAFFICTABLENAME, columns,
//				getTrafficStatisticsTableSql());
	}

	/**
	 * Upgrade tables. In this method, the sequence is: <b>
	 * <p>
	 * [1] Rename the specified table as a temporary table.
	 * <p>
	 * [2] Create a new table which name is the specified name.
	 * <p>
	 * [3] Insert data into the new created table, data from the temporary
	 * table.
	 * <p>
	 * [4] Drop the temporary table. </b>
	 * 
	 * @param db
	 *            The database.
	 * @param tableName
	 *            The table name.
	 * @param columns
	 *            The columns range, format is "ColA, ColB, ColC, ... ColN";
	 */
	protected void upgradeTables(SQLiteDatabase db, String tableName,
			String columns, String createSql) {
		try {
			db.beginTransaction();

			// 1, Rename table.
			String tempTableName = tableName + "_temp";
			String sql = "ALTER TABLE " + tableName + " RENAME TO "
					+ tempTableName;
			db.execSQL(sql);

			// 2, Create table.
			db.execSQL(createSql);

			// 3, Load data
			sql = "INSERT INTO " + tableName + " (" + columns + ") "
					+ " SELECT " + columns + " FROM " + tempTableName;
			db.execSQL(sql);

			// 4, Drop the temporary table.
			db.execSQL("DROP TABLE IF EXISTS " + tempTableName, new Object[] {});

			db.setTransactionSuccessful();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	protected String getColumnNames(SQLiteDatabase db, String tableName) {
		StringBuffer sb = null;
		Cursor c = null;
		try {
			c = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
			if (null != c) {
				int columnIndex = c.getColumnIndex("name");
				if (-1 == columnIndex) {
					return null;
				}

				// int index = 0;
				sb = new StringBuffer(c.getCount());
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					sb.append(c.getString(columnIndex));
					sb.append(",");
					// index++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}

		return sb.toString().substring(0, sb.toString().length() - 1);
	}

	/**
	 * 报警信息表建表sql语句
	 * 
	 * @return
	 */
	private String getAlarmTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(DBConstant.ALARMTABLENAME);
		sb.append("(");
		sb.append(DBConstant.ALARMUSERNAME);
		sb.append(" TEXT, ");
		sb.append(DBConstant.ALARMEQUIPMENTID);
		sb.append(" TEXT, ");
		sb.append(DBConstant.ALARMEQUIPMENTNAME);
		sb.append(" TEXT, ");
		sb.append(DBConstant.ALARMDATETIME);
		sb.append(" TEXT, ");
		sb.append(DBConstant.ALARMTYPE);
		sb.append(" SHORT, ");
		sb.append(DBConstant.ALARMINFO);
		sb.append(" TEXT, ");
		sb.append(DBConstant.ALARMSOLVE);
		sb.append(" SHORT, ");
		sb.append(DBConstant.DEALUSERNICK);
		sb.append(" TEXT, ");
		sb.append(DBConstant.DEALTIME);
		sb.append(" INTEGER, ");
		sb.append("PRIMARY KEY(" + DBConstant.ALARMUSERNAME + ", "
				+ DBConstant.ALARMEQUIPMENTID + ", " + DBConstant.ALARMDATETIME
				+ ", " + DBConstant.ALARMTYPE + ")");
		sb.append(");");
		System.out.println("CreateAlarmTableSql------------>" + sb.toString());
		return sb.toString();
	}

//	/**
//	 * 流量统计表建表sql语句
//	 */
//	private String getTrafficStatisticsTableSql() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("CREATE TABLE IF NOT EXISTS ");
//		sb.append(DBConstant.TRAFFICTABLENAME);
//		sb.append("(");
//		sb.append(DBConstant.TRAFFICID);
//		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
//		sb.append(DBConstant.TRAFFICYEAR);
//		sb.append(" INTEGER, ");
//		sb.append(DBConstant.TRAFFICMONTH);
//		sb.append(" INTEGER, ");
//		sb.append(DBConstant.TRAFFICDAY);
//		sb.append(" INTEGER, ");
//		sb.append(DBConstant.TRAFFICOFDAY);
//		sb.append(" FLOAT");
//		sb.append(")");
//		// System.out.println("CreateTrafficTableSql------------>" +
//		// sb.toString());
//		return sb.toString();
//	}

}
