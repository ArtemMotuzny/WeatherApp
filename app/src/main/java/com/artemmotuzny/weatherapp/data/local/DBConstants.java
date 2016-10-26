package com.artemmotuzny.weatherapp.data.local;

import android.provider.BaseColumns;

/**
 * Created by tema_ on 13.10.2016.
 */

class DBConstants {

    public DBConstants() {
    }


    interface WeatherEntity extends BaseColumns {
        String TABLE_NAME = "WeatherTable";
        String WEATHER_ID ="weather_id";
        String NAME = "name";
        String DATA = "my_data";
        String COD = "cod";


        String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + WEATHER_ID + " integer, "
                + NAME + " text, "
                + DATA + " integer, "
                + COD + " integer);";
    }

    interface ExpandedWeatherInfoEntity extends BaseColumns {
        String TABLE_NAME = "ExpandedWeatherInfoTable";
        String EXPANDED_WEATHER_ID = "expanded_weather_id";
        String MAIN = "my_main";
        String DESCRIPTION = "description";
        String ICON = "icon";
        String WEATHER_ID ="weather_id";

        String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + EXPANDED_WEATHER_ID + " integer, "
                + WEATHER_ID + " integer, "
                + MAIN + " text, "
                + DESCRIPTION + " text, "
                + ICON + " text);";
    }

    interface WindEntity extends BaseColumns {
        String TABLE_NAME = "WindTable";
        String SPEED = "speed";
        String WIND_DEGREES = "degrees";

        String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + SPEED + " real, "
                + WIND_DEGREES + " real);";
    }


    interface SysEntity extends BaseColumns {
        String TABLE_NAME = "SysTable";
        String COUNTRY = "country";
        String SUNRISE = "sunrise";
        String SUNSET = "sunset";

        String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + COUNTRY + " text, "
                + SUNRISE + " integer, "
                + SUNSET + " integer);";
    }

    interface RainEntity extends BaseColumns {
        String TABLE_NAME = "RainTable";
        String _3HOUR = "_3h";

        String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + _3HOUR + " real);";
    }

    interface MainWeatherInfoEntity extends BaseColumns {
        String TABLE_NAME = "MainWeatherInfoTable";
        String TEMP = "temp";
        String HUMIDITY = "humidity";
        String PRESSURE = "pressure";
        String TEMP_MIN = "temp_min";
        String TEMP_MAX = "temp_max";

        String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + TEMP + " real, "
                + HUMIDITY + " integer, "
                + PRESSURE + " real, "
                + TEMP_MIN + " real, "
                + TEMP_MAX + " real);";
    }

    interface CoordinateEntity extends BaseColumns {
        String TABLE_NAME = "CoordinateTable";
        String LON = "lon";
        String LAT = "lat";

        String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + LON + " real, "
                + LAT + " real);";
    }


    interface CloudsEntity extends BaseColumns {
        String TABLE_NAME = "CloudsTable";
        String CLOUDINESS = "cloudiness";

        String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + CLOUDINESS + " integer);";
    }

}
