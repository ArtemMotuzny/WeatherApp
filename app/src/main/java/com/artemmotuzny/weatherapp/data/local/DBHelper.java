package com.artemmotuzny.weatherapp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tema_ on 13.10.2016.
 */

class DBHelper extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "WeatherDB";

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstants.CoordinateEntity.CREATE);
        db.execSQL(DBConstants.CloudsEntity.CREATE);
        db.execSQL(DBConstants.WeatherEntity.CREATE);
        db.execSQL(DBConstants.MainWeatherInfoEntity.CREATE);
        db.execSQL(DBConstants.RainEntity.CREATE);
        db.execSQL(DBConstants.SysEntity.CREATE);
        db.execSQL(DBConstants.ExpandedWeatherInfoEntity.CREATE);
        db.execSQL(DBConstants.WindEntity.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
