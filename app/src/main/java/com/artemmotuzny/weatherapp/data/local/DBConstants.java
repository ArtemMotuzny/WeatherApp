package com.artemmotuzny.weatherapp.data.local;

import android.provider.BaseColumns;

/**
 * Created by tema_ on 13.10.2016.
 */

public class DBConstants {

    public DBConstants() {
    }

    public interface MyBaseColumn extends BaseColumns {
        String WEATHER_DATA_ID = "weather_data_id";
    }


    public static abstract class WeatherEntity implements MyBaseColumn {
        public static final String TABLE_NAME = "WeatherTable";
        public static final String NAME = "name";
        public static final String DATA = "my_data";
        public static final String COD = "cod";

        public static final String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + WEATHER_DATA_ID + " integer unique, "
                + NAME + " text, "
                + DATA + " integer, "
                + COD + " integer);";
    }

    public static abstract class ExpandedWeatherInfoEntity implements MyBaseColumn {
        public static final String TABLE_NAME = "ExpandedWeatherInfoTable";
        public static final String EXPANDED_WEATHER_ID = "expandedWeatherId";
        public static final String MAIN = "my_main";
        public static final String DESCRIPTION = "description";
        public static final String ICON = "icon";


        public static final String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + EXPANDED_WEATHER_ID + " integer, "
                + WEATHER_DATA_ID + " integer unique, "
                + MAIN + " text, "
                + DESCRIPTION + " text, "
                + ICON + " text);";
    }

    public static abstract class WindEntity implements MyBaseColumn {
        public static final String TABLE_NAME = "WindTable";
        public static final String SPEED = "speed";
        public static final String WIND_DEGREES = "degrees";

        public static final String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + WEATHER_DATA_ID + " integer unique, "
                + SPEED + " real, "
                + WIND_DEGREES + " real);";
    }


    public static abstract class SysEntity implements MyBaseColumn {
        public static final String TABLE_NAME = "SysTable";
        public static final String COUNTRY = "country";
        public static final String SUNRISE = "sunrise";
        public static final String SUNSET = "sunset";

        public static final String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + WEATHER_DATA_ID + " integer unique, "
                + COUNTRY + " text, "
                + SUNRISE + " integer, "
                + SUNSET + " integer);";
    }

    public static abstract class RainEntity implements MyBaseColumn {
        public static final String TABLE_NAME = "RainTable";
        public static final String _3HOUR = "_3h";

        public static final String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + WEATHER_DATA_ID + " integer unique, "
                + _3HOUR + " real);";
    }

    public static abstract class MainWeatherInfoEntity implements MyBaseColumn {
        public static final String TABLE_NAME = "MainWeatherInfoTable";
        public static final String TEMP = "temp";
        public static final String HUMIDITY = "humidity";
        public static final String PRESSURE = "pressure";
        public static final String TEMP_MIN = "temp_min";
        public static final String TEMP_MAX = "temp_max";

        public static final String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + WEATHER_DATA_ID + " integer unique, "
                + TEMP + " real, "
                + HUMIDITY + " integer, "
                + PRESSURE + " pressure, "
                + TEMP_MIN + " real, "
                + TEMP_MAX + " real);";
    }

    public static abstract class CoordinateEntity implements MyBaseColumn {
        public static final String TABLE_NAME = "CoordinateTable";
        public static final String LON = "lon";
        public static final String LAT = "lat";

        public static final String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + WEATHER_DATA_ID + " integer unique, "
                + LON + " real, "
                + LAT + " real);";
    }


    public static abstract class CloudsEntity implements MyBaseColumn {
        public static final String TABLE_NAME = "CloudsTable";
        public static final String CLOUDINESS = "cloudiness";

        public static final String CREATE = "create table if not exists " + TABLE_NAME + " ( "
                + _ID + " integer primary key autoincrement, "
                + WEATHER_DATA_ID + " integer unique, "
                + CLOUDINESS + " integer);";
    }

}
