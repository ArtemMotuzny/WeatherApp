package com.artemmotuzny.weatherapp.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;

import com.artemmotuzny.weatherapp.data.WeatherRepository;
import com.artemmotuzny.weatherapp.data.models.Clouds;
import com.artemmotuzny.weatherapp.data.models.Coordinates;
import com.artemmotuzny.weatherapp.data.models.ExpandedWeatherInfo;
import com.artemmotuzny.weatherapp.data.models.MainWeatherInfo;
import com.artemmotuzny.weatherapp.data.models.Rain;
import com.artemmotuzny.weatherapp.data.models.Sys;
import com.artemmotuzny.weatherapp.data.models.Weather;
import com.artemmotuzny.weatherapp.data.models.Wind;
import com.artemmotuzny.weatherapp.utils.BitmapUtils;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tema_ on 13.10.2016.
 */

public class LocalDataSource implements WeatherRepository{
    private BriteDatabase dataBaseHelper;

    public LocalDataSource(@NonNull Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        dataBaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
    }

    //get observables
    @Override
    public Observable<Weather> getWeatherByLocation(Location location) {
        return getCoordByWeatherDataId(location.getLatitude(), location.getLongitude()).flatMap(new Func1<Coordinates, Observable<Weather>>() {
            @Override
            public Observable<Weather> call(final Coordinates coordinates) {
                long id = coordinates.getWeatherDataId();
                MainWeatherInfo mainWeatherInfo = getMainWeatherInfoByWeatherId(id);
                Clouds clouds = getCloudsByWeatherId(id);
                List<ExpandedWeatherInfo> expandedWeatherInfos = getExpandedWeatherInfoByWeatherId(id);
                Rain rain = getRainByWeatherId(id);
                Sys sys = getSysByWeatherId(id);
                Wind wind = getWindByWeatherId(id);
                Weather weather = getWeatherById(id);

                //set weather modules
                weather.setClouds(clouds);
                weather.setCoord(coordinates);
                weather.setMainWeatherInfo(mainWeatherInfo);
                weather.setExpandedWeatherInfo(expandedWeatherInfos);
                weather.setRain(rain);
                weather.setSys(sys);
                weather.setWind(wind);


                return Observable.just(weather);
            }
        });
    }

    private Observable<Coordinates> getCoordByWeatherDataId(double lat, double lon) {
        String sql = "SELECT * FROM " + DBConstants.CoordinateEntity.TABLE_NAME + " WHERE " + DBConstants.CoordinateEntity.LAT + " = ? AND " + DBConstants.CoordinateEntity.LON + " = ?";
        return dataBaseHelper.createQuery(DBConstants.CoordinateEntity.TABLE_NAME, sql, String.valueOf(lat), String.valueOf(lon)).mapToOneOrDefault(Mapper.cursorToCoordinates(),null);
    }

    //get models
    private Weather getWeatherById(long id) {
        String sql = "SELECT * FROM " + DBConstants.WeatherEntity.TABLE_NAME + " WHERE " + DBConstants.MyBaseColumn.WEATHER_DATA_ID+ " = ?";
        return Mapper.cursorToWeather(dataBaseHelper.query(sql, String.valueOf(id)));
    }

    private Wind getWindByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.WindEntity.TABLE_NAME + " WHERE " + DBConstants.MyBaseColumn.WEATHER_DATA_ID+ " = ?";
        return Mapper.cursorToWind(dataBaseHelper.query(sql, String.valueOf(id)));
    }

    private Sys getSysByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.SysEntity.TABLE_NAME + " WHERE " + DBConstants.MyBaseColumn.WEATHER_DATA_ID+ " = ?";
        return Mapper.cursorToSys(dataBaseHelper.query(sql, String.valueOf(id)));
    }

    private Rain getRainByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.RainEntity.TABLE_NAME + " WHERE " + DBConstants.MyBaseColumn.WEATHER_DATA_ID+ " = ?";
        return Mapper.cursorToRain(dataBaseHelper.query(sql, String.valueOf(id)));
    }


    private MainWeatherInfo getMainWeatherInfoByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.MainWeatherInfoEntity.TABLE_NAME + " WHERE " + DBConstants.MyBaseColumn.WEATHER_DATA_ID+ " = ?";
        return Mapper.cursorToMainWeatherInfo(dataBaseHelper.query(sql, String.valueOf(id)));
    }


    private Clouds getCloudsByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.CloudsEntity.TABLE_NAME + " WHERE " + DBConstants.MyBaseColumn.WEATHER_DATA_ID+ " = ?";
        return Mapper.cursorToClouds(dataBaseHelper.query(sql, String.valueOf(id)));
    }

    private List<ExpandedWeatherInfo> getExpandedWeatherInfoByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.ExpandedWeatherInfoEntity.TABLE_NAME + " WHERE " + DBConstants.MyBaseColumn.WEATHER_DATA_ID+ " = ?";
        return Mapper.cursorToExpandedWeather(dataBaseHelper.query(sql, String.valueOf(id)));
    }

    //Save methods
    @Override
    public void saveWeather(Weather weather) {
        BriteDatabase.Transaction transaction = dataBaseHelper.newTransaction();
        try {

            saveCoord(weather.getId(), weather.getCoord());
            saveClouds(weather.getId(), weather.getClouds());
            saveMain(weather.getId(), weather.getMainWeatherInfo());
            saveRain(weather.getId(), weather.getRain());
            saveSys(weather.getId(), weather.getSys());
            saveWeather(weather.getId(), weather.getExpandedWeatherInfo());
            saveWind(weather.getId(), weather.getWind());

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.WeatherEntity.NAME, weather.getName());
            contentValues.put(DBConstants.WeatherEntity.COD, weather.getCod());
            contentValues.put(DBConstants.WeatherEntity.DATA, weather.getData());
            contentValues.put(DBConstants.MyBaseColumn.WEATHER_DATA_ID, weather.getId());
            dataBaseHelper.insert(DBConstants.WeatherEntity.TABLE_NAME, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    private void saveWind(Integer id, Wind wind) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.MyBaseColumn.WEATHER_DATA_ID, id);
        contentValues.put(DBConstants.WindEntity.WIND_DEGREES, wind.getDegrees());
        contentValues.put(DBConstants.WindEntity.SPEED, wind.getSpeed());
        dataBaseHelper.insert(DBConstants.WindEntity.TABLE_NAME, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void saveWeather(Integer id, List<ExpandedWeatherInfo> expandedWeatherInfo) {
        ContentValues contentValues = new ContentValues();
        for (ExpandedWeatherInfo w : expandedWeatherInfo) {
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity._ID,w.getId());
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.ICON, BitmapUtils.encodeToBase64(w.getBitmapIcon(), Bitmap.CompressFormat.PNG, 100));
            contentValues.put(DBConstants.MyBaseColumn.WEATHER_DATA_ID, id);
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.MAIN, w.getMain());
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.DESCRIPTION, w.getDescription());
            dataBaseHelper.insert(DBConstants.ExpandedWeatherInfoEntity.TABLE_NAME, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    private void saveSys(Integer id, Sys sys) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.MyBaseColumn.WEATHER_DATA_ID, id);
        contentValues.put(DBConstants.SysEntity.COUNTRY, sys.getCountry());
        contentValues.put(DBConstants.SysEntity.SUNRISE, sys.getSunrise());
        contentValues.put(DBConstants.SysEntity.SUNSET, sys.getSunset());
        dataBaseHelper.insert(DBConstants.SysEntity.TABLE_NAME, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void saveRain(Integer id, Rain rain) {
        if (rain != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.MyBaseColumn.WEATHER_DATA_ID, id);

            contentValues.put(DBConstants.RainEntity._3HOUR, rain.get3h());
            dataBaseHelper.insert(DBConstants.RainEntity.TABLE_NAME, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    private void saveMain(Integer id, MainWeatherInfo mainWeatherInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.MyBaseColumn.WEATHER_DATA_ID, id);
        contentValues.put(DBConstants.MainWeatherInfoEntity.HUMIDITY, mainWeatherInfo.getHumidity());
        contentValues.put(DBConstants.MainWeatherInfoEntity.PRESSURE, mainWeatherInfo.getPressure());
        contentValues.put(DBConstants.MainWeatherInfoEntity.TEMP, mainWeatherInfo.getTemp());
        contentValues.put(DBConstants.MainWeatherInfoEntity.TEMP_MAX, mainWeatherInfo.getTempMax());
        contentValues.put(DBConstants.MainWeatherInfoEntity.TEMP_MIN, mainWeatherInfo.getTempMin());
        dataBaseHelper.insert(DBConstants.MainWeatherInfoEntity.TABLE_NAME, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void saveClouds(Integer id, Clouds clouds) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.MyBaseColumn.WEATHER_DATA_ID, id);
        contentValues.put(DBConstants.CloudsEntity.CLOUDINESS, clouds.getCloudiness());
        dataBaseHelper.insert(DBConstants.CloudsEntity.TABLE_NAME, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void saveCoord(Integer id, Coordinates coordinates) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.MyBaseColumn.WEATHER_DATA_ID, id);
        contentValues.put(DBConstants.CoordinateEntity.LAT, coordinates.getLat());
        contentValues.put(DBConstants.CoordinateEntity.LON, coordinates.getLon());
        dataBaseHelper.insert(DBConstants.CoordinateEntity.TABLE_NAME, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }


}
