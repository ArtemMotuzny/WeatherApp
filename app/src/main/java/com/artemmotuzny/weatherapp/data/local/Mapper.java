package com.artemmotuzny.weatherapp.data.local;

import android.database.Cursor;

import com.artemmotuzny.weatherapp.data.models.Clouds;
import com.artemmotuzny.weatherapp.data.models.Coordinates;
import com.artemmotuzny.weatherapp.data.models.ExpandedWeatherInfo;
import com.artemmotuzny.weatherapp.data.models.MainWeatherInfo;
import com.artemmotuzny.weatherapp.data.models.Rain;
import com.artemmotuzny.weatherapp.data.models.Sys;
import com.artemmotuzny.weatherapp.data.models.Weather;
import com.artemmotuzny.weatherapp.data.models.Wind;
import com.artemmotuzny.weatherapp.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by tema_ on 13.10.2016.
 */

class Mapper {
    static Func1<Cursor, Coordinates> cursorToCoordinates() {
        return new Func1<Cursor, Coordinates>() {
            @Override
            public Coordinates call(Cursor cursor) {
                Coordinates coordinates = new Coordinates();
                coordinates.setWeatherDataId(cursor.getInt(cursor.getColumnIndex(DBConstants.CoordinateEntity.WEATHER_DATA_ID)));
                coordinates.setLat(cursor.getDouble(cursor.getColumnIndex(DBConstants.CoordinateEntity.LAT)));
                coordinates.setLon(cursor.getDouble(cursor.getColumnIndex(DBConstants.CoordinateEntity.LON)));
                return coordinates;
            }
        };
    }

    static Weather cursorToWeather(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            Weather weather = new Weather();
            weather.setName(cursor.getString(cursor.getColumnIndex(DBConstants.WeatherEntity.NAME)));
            weather.setData(cursor.getInt(cursor.getColumnIndex(DBConstants.WeatherEntity.DATA)));
            weather.setCod(cursor.getInt(cursor.getColumnIndex(DBConstants.WeatherEntity.COD)));
            weather.setId(cursor.getInt(cursor.getColumnIndex(DBConstants.WeatherEntity.WEATHER_DATA_ID)));
            cursor.close();
            return weather;
        }
        return null;
    }

    static Wind cursorToWind(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            Wind wind= new Wind();
            wind.setDegrees(cursor.getDouble(cursor.getColumnIndex(DBConstants.WindEntity.WIND_DEGREES)));
            wind.setSpeed(cursor.getDouble(cursor.getColumnIndex(DBConstants.WindEntity.SPEED)));
            return wind;
        }
        return null;
    }

    public static List<ExpandedWeatherInfo> cursorToExpandedWeather(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            List<ExpandedWeatherInfo> expandedWeatherInfos = new ArrayList<>();

            while (!cursor.isAfterLast()){
                ExpandedWeatherInfo expandedWeatherInfo = new ExpandedWeatherInfo();
                expandedWeatherInfo.setId(cursor.getInt(cursor.getColumnIndex(DBConstants.ExpandedWeatherInfoEntity.EXPANDED_WEATHER_ID)));
                expandedWeatherInfo.setDescription(cursor.getString(cursor.getColumnIndex(DBConstants.ExpandedWeatherInfoEntity.DESCRIPTION)));
                expandedWeatherInfo.setMain(cursor.getString(cursor.getColumnIndex(DBConstants.ExpandedWeatherInfoEntity.MAIN)));
                expandedWeatherInfo.setBitmapIcon(BitmapUtils.decodeBase64(cursor.getString(cursor.getColumnIndex(DBConstants.ExpandedWeatherInfoEntity.ICON))));
                expandedWeatherInfos.add(expandedWeatherInfo);
                cursor.moveToNext();
            }
            cursor.close();
            return expandedWeatherInfos;
        }
        return null;
    }

    public static Clouds cursorToClouds(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            Clouds clouds= new Clouds();
            clouds.setCloudiness(cursor.getInt(cursor.getColumnIndex(DBConstants.CloudsEntity.CLOUDINESS)));
            return clouds;
        }
        return null;
    }

    public static MainWeatherInfo cursorToMainWeatherInfo(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            MainWeatherInfo mainWeatherInfo= new MainWeatherInfo();
            mainWeatherInfo.setPressure(cursor.getDouble(cursor.getColumnIndex(DBConstants.MainWeatherInfoEntity.PRESSURE)));
            mainWeatherInfo.setTemp(cursor.getDouble(cursor.getColumnIndex(DBConstants.MainWeatherInfoEntity.TEMP)));
            mainWeatherInfo.setTempMax(cursor.getDouble(cursor.getColumnIndex(DBConstants.MainWeatherInfoEntity.TEMP_MAX)));
            mainWeatherInfo.setTempMin(cursor.getDouble(cursor.getColumnIndex(DBConstants.MainWeatherInfoEntity.TEMP_MIN)));
            mainWeatherInfo.setHumidity(cursor.getInt(cursor.getColumnIndex(DBConstants.MainWeatherInfoEntity.HUMIDITY)));
            return mainWeatherInfo;
        }
        return null;
    }

    public static Rain cursorToRain(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            Rain rain = new Rain();
            rain.set3h(cursor.getDouble(cursor.getColumnIndex(DBConstants.RainEntity._3HOUR)));
            return rain;
        }
        return null;
    }

    public static Sys cursorToSys(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            Sys sys = new Sys();
            sys.setSunrise(cursor.getInt(cursor.getColumnIndex(DBConstants.SysEntity.SUNRISE)));
            sys.setSunset(cursor.getInt(cursor.getColumnIndex(DBConstants.SysEntity.SUNSET)));
            sys.setCountry(cursor.getString(cursor.getColumnIndex(DBConstants.SysEntity.COUNTRY)));
            return sys;
        }
        return null;
    }
}
