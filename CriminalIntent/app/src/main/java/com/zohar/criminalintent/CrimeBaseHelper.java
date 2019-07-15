package com.zohar.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeDB.NAME + "(" +
                "_id integer primary key autoincrement" + "," +
                CrimeDB.Col.UUID + "," +
                CrimeDB.Col.TITLE + "," +
                CrimeDB.Col.DATE + "," +
                CrimeDB.Col.SOLVED
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
