package com.team8.cs408.yadonDataBase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper {
    private static final String DATABASE_NAME = "accountbook.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB._CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB._TABLENAME);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context) {
        this.mCtx = context;
    }

    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDB.close();
    }

    // Insert DB
    public long insertColumn(String groupName, String name, String phoneNumber,
                             int debt, int alarmStart, int alarmPeriod) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.GROUPNAME, groupName);
        values.put(DataBases.CreateDB.NAME, name);
        values.put(DataBases.CreateDB.PHONENUMBER, phoneNumber);
        values.put(DataBases.CreateDB.DEBT, debt);
        values.put(DataBases.CreateDB.ALARMSTART, alarmStart);
        values.put(DataBases.CreateDB.ALARMPERIOD, alarmPeriod);
        return mDB.insert(DataBases.CreateDB._TABLENAME, null, values);
    }

    // Update DB
    public boolean updateColumn(long id, String groupName, String name, String phoneNumber, int debt) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.GROUPNAME, groupName);
        values.put(DataBases.CreateDB.NAME, name);
        values.put(DataBases.CreateDB.PHONENUMBER, phoneNumber);
        values.put(DataBases.CreateDB.DEBT, debt);
        return mDB.update(DataBases.CreateDB._TABLENAME, values, "_id=" + id, null) > 0;
    }

    public void flush() {
        mDB.delete(DataBases.CreateDB._TABLENAME, null, null);
    }

    // Delete ID
    public boolean deleteColumn(long id) {
        return mDB.delete(DataBases.CreateDB._TABLENAME, "_id=" + id, null) > 0;
    }

    // Delete Contact
    public boolean deleteColumn(String groupName) {
        return mDB.delete(DataBases.CreateDB._TABLENAME, "groupName=" + "'" + groupName + "'", null) > 0;
    }

    // Select All
    public Cursor getAllColumns() {
        return mDB.query(DataBases.CreateDB._TABLENAME, null, null, null, null, null, null);
    }

    public Cursor getGroupColumns(String groupName) {
        return mDB.query(DataBases.CreateDB._TABLENAME, null, "groupName=" + "'" + groupName + "'", null, null, null, null);
    }

    public Cursor getTheColumn(String groupName, String name) {
        return mDB.query(DataBases.CreateDB._TABLENAME, null,
                "groupName=" + "'" + groupName + "'" + " AND name=" + "'" + name + "'", null, null, null, null);
    }

    public int updateTheColumn_debt(String groupName, String name, int debt) {
        ContentValues values = new ContentValues();
        values.put("debt", debt);
        return mDB.update(DataBases.CreateDB._TABLENAME, values, "groupName=" + "'" + groupName + "'" + " AND name=" + "'" + name + "'", null);
    }

    public int updateTheColumn_alarm(String groupName, int alarmStart, int alarmPeriod){
        ContentValues values = new ContentValues();
        values.put("alarmStart", alarmStart);
        values.put("alarmPeriod", alarmPeriod);
        return mDB.update(DataBases.CreateDB._TABLENAME, values, "groupName=" + "'" + groupName + "'", null);
    }

    // ID 컬럼 얻어 오기
    public Cursor getColumn(long id) {
        Cursor c = mDB.query(DataBases.CreateDB._TABLENAME, null,
                "_id=" + id, null, null, null, null);
        if (c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }

    // 이름 검색 하기 (rawQuery)
    public Cursor getMatchName(String name) {
        Cursor c = mDB.rawQuery("select * from address where name=" + "'" + name + "'", null);
        return c;
    }
}
