package com.vm.mserver.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by VanManh on 04-Dec-17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder query1 = new StringBuilder();
        query1.append("CREATE TABLE " + Constants.TB_PHONE_NAME);
        query1.append("( ID INTEGER PRIMARY KEY AUTOINCREMENT,");
        query1.append(Constants.NAME_COLUMN + " TEXT,");
        query1.append(Constants.PHONE_COLUMN + " TEXT )");

        StringBuilder query2 = new StringBuilder();
        query2.append("CREATE TABLE " + Constants.TB_MARK_NAME);
        query2.append("(" + Constants.MS + " INTEGER PRIMARY KEY,");
        query2.append(Constants.S_NAME + " TEXT,");
        query2.append(Constants.S_TOAN + " REAL,");
        query2.append(Constants.S_LY + " REAL,");
        query2.append(Constants.S_HOA + " REAL )");

        db.execSQL(query1.toString());
        db.execSQL(query2.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + Constants.TB_PHONE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + Constants.TB_MARK_NAME);
        onCreate(db);
    }
}
