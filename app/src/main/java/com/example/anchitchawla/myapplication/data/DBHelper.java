package com.example.anchitchawla.myapplication.data;

/**
 * Created by Anchit Chawla on 19-12-2017.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.anchitchawla.myapplication.data.Contract.Entry;

public class DBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = DBHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "inventory.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_DB = "CREATE TABLE " + Entry.Table_Name + " (" + Entry._Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Entry.Coloumn_Name + " TEXT NOT NULL, "
                + Entry.Column_Quantity + " INTEGER NOT NULL DEFAULT 0, "
                + Entry.Column_Price + " INTEGER NOT NULL DEFAULT 0, "
                + Entry.Column_SName + " TEXT NOT NULL, "
                + Entry.Coloumn_SEmail + " TEXT NOT NULL, "
                + Entry.Coloumn_SPhone + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(SQL_CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
