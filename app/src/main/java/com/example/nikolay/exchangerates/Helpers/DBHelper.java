package com.example.nikolay.exchangerates.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nikolay on 19.07.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "myLogs";
    private static final String DATABASE_NAME = "myDB";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate database");
        db.execSQL("create table current_data ("
                + "id integer primary key autoincrement,"
                + "eur_buy text,"
                + "eur_sell text,"
                + "rur_buy text,"
                + "rur_sell text,"
                + "usd_buy text,"
                + "usd_sell text,"
                + "btc_buy text,"
                + "btc_sell text" + ");");

        db.execSQL("create table graph_data ("
                + "id integer primary key autoincrement,"
                + "data text,"
                + "eur_buy real,"
                + "usd_buy real" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS current_data");
        db.execSQL("DROP TABLE IF EXISTS graph_data");
        onCreate(db);
    }
}
