package com.artemmotuzny.weatherapp.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by tema_ on 13.10.2016.
 */

public class LocalDataSource implements WeatherRepository {
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public LocalDataSource(@NonNull Context context) {
        dbHelper = new DBHelper(context);
    }

    //get observables
    @Override
    public Observable<Weather> getWeatherByLocation(Location location) {
        return Observable.just(getCoordByWeatherDataId(location.getLatitude(), location.getLongitude())).flatMap(new Func1<Coordinates, Observable<Weather>>() {
            @Override
            public Observable<Weather> call(final Coordinates coordinates) {
                long id = coordinates.getDbId();
                MainWeatherInfo mainWeatherInfo = getMainWeatherInfoByWeatherId(id);
                Clouds clouds = getCloudsByWeatherId(id);
                Rain rain = getRainByWeatherId(id);
                Sys sys = getSysByWeatherId(id);
                Wind wind = getWindByWeatherId(id);
                Weather weather = getWeatherById(id);
                List<ExpandedWeatherInfo> expandedWeatherInfos = getExpandedWeatherInfoByWeatherId(weather.getId());

                //set weather modules
                weather.setClouds(clouds);
                weather.setCoord(coordinates);
                weather.setMainWeatherInfo(mainWeatherInfo);
                weather.setExpandedWeatherInfo(expandedWeatherInfos);
                weather.setRain(rain);
                weather.setSys(sys);
                weather.setWind(wind);

                database.close();
                return Observable.create(subscriber -> subscriber.onNext(weather));
            }
        });
    }

    private Coordinates getCoordByWeatherDataId(double lat, double lon) {
        database = dbHelper.getWritableDatabase();
        String sql = "SELECT * FROM " + DBConstants.CoordinateEntity.TABLE_NAME + " WHERE " + DBConstants.CoordinateEntity.LAT + " = ? AND " + DBConstants.CoordinateEntity.LON + " = ?";
        return Mapper.cursorToCoordinates(database.rawQuery(sql, new String[]{String.valueOf(lat), String.valueOf(lon)}));
    }

    //get models
    private Weather getWeatherById(long id) {
        String sql = "SELECT * FROM " + DBConstants.WeatherEntity.TABLE_NAME + " WHERE " + DBConstants.WeatherEntity._ID + " = ?";
        return Mapper.cursorToWeather(database.rawQuery(sql, new String[]{String.valueOf(id)}));
    }

    private Wind getWindByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.WindEntity.TABLE_NAME + " WHERE " + DBConstants.WindEntity._ID + " = ?";
        return Mapper.cursorToWind(database.rawQuery(sql, new String[]{String.valueOf(id)}));
    }

    private Sys getSysByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.SysEntity.TABLE_NAME + " WHERE " + DBConstants.SysEntity._ID + " = ?";
        return Mapper.cursorToSys(database.rawQuery(sql, new String[]{String.valueOf(id)}));
    }

    private Rain getRainByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.RainEntity.TABLE_NAME + " WHERE " + DBConstants.RainEntity._ID + " = ?";
        return Mapper.cursorToRain(database.rawQuery(sql, new String[]{String.valueOf(id)}));
    }


    private MainWeatherInfo getMainWeatherInfoByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.MainWeatherInfoEntity.TABLE_NAME + " WHERE " + DBConstants.MainWeatherInfoEntity._ID + " = ?";
        return Mapper.cursorToMainWeatherInfo(database.rawQuery(sql, new String[]{String.valueOf(id)}));
    }


    private Clouds getCloudsByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.CloudsEntity.TABLE_NAME + " WHERE " + DBConstants.CloudsEntity._ID + " = ?";
        return Mapper.cursorToClouds(database.rawQuery(sql, new String[]{String.valueOf(id)}));
    }

    private List<ExpandedWeatherInfo> getExpandedWeatherInfoByWeatherId(long id) {
        String sql = "SELECT * FROM " + DBConstants.ExpandedWeatherInfoEntity.TABLE_NAME + " WHERE " + DBConstants.ExpandedWeatherInfoEntity.WEATHER_ID + " = ?";
        return Mapper.cursorToExpandedWeather(database.rawQuery(sql, new String[]{String.valueOf(id)}));
    }

    //Save methods
    @Override
    public void saveOrUpdateWeather(Weather weather) {
        database = dbHelper.getReadableDatabase();

        database.beginTransaction();
        try {
            long id = dbIsContainsWeather(weather.getId());
            if (id == -1) {
                saveWeather(weather);
            } else {
                updateWeather(id, weather);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
    }

    private long dbIsContainsWeather(Integer weatherId) {
        String sql = "SELECT " + DBConstants.WeatherEntity._ID + " FROM " + DBConstants.WeatherEntity.TABLE_NAME + " WHERE " + DBConstants.WeatherEntity.WEATHER_ID + " = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(weatherId)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            long id = cursor.getLong(cursor.getColumnIndex(DBConstants.WeatherEntity._ID));
            cursor.close();
            return id;
        }
        return -1;
    }

    private void saveWeather(Weather weather) {
        saveCoord(weather.getCoord());
        saveClouds(weather.getClouds());
        saveMain(weather.getMainWeatherInfo());
        saveRain(weather.getRain());
        saveSys(weather.getSys());
        saveExpandedWeather(weather.getId(), weather.getExpandedWeatherInfo());
        saveWind(weather.getWind());

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.WeatherEntity.WEATHER_ID, weather.getId());
        contentValues.put(DBConstants.WeatherEntity.NAME, weather.getName());
        contentValues.put(DBConstants.WeatherEntity.COD, weather.getCod());
        contentValues.put(DBConstants.WeatherEntity.DATA, weather.getData());
        database.insert(DBConstants.WeatherEntity.TABLE_NAME, null, contentValues);
    }

    private void saveExpandedWeather(Integer weatherId, List<ExpandedWeatherInfo> expandedWeatherInfo) {
        ContentValues contentValues = new ContentValues();
        for (ExpandedWeatherInfo expandedWeather : expandedWeatherInfo) {
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.EXPANDED_WEATHER_ID, expandedWeather.getId());
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.ICON, BitmapUtils.encodeToBase64(expandedWeather.getBitmapIcon(), Bitmap.CompressFormat.PNG, 100));
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.WEATHER_ID, weatherId);
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.MAIN, expandedWeather.getMain());
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.DESCRIPTION, expandedWeather.getDescription());
            database.insert(DBConstants.ExpandedWeatherInfoEntity.TABLE_NAME,null, contentValues);
        }
    }

    private void saveWind(Wind wind) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.WindEntity.WIND_DEGREES, wind.getDegrees());
        contentValues.put(DBConstants.WindEntity.SPEED, wind.getSpeed());
        database.insert(DBConstants.WindEntity.TABLE_NAME, null,contentValues);
    }

    private void saveSys(Sys sys) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.SysEntity.COUNTRY, sys.getCountry());
        contentValues.put(DBConstants.SysEntity.SUNRISE, sys.getSunrise());
        contentValues.put(DBConstants.SysEntity.SUNSET, sys.getSunset());
        database.insert(DBConstants.SysEntity.TABLE_NAME, null,contentValues);

    }

    private void saveRain(Rain rain) {
        if (rain != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.RainEntity._3HOUR, rain.get3h());
            database.insert(DBConstants.RainEntity.TABLE_NAME, null,contentValues);
        }
    }

    private void saveMain(MainWeatherInfo mainWeatherInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.MainWeatherInfoEntity.HUMIDITY, mainWeatherInfo.getHumidity());
        contentValues.put(DBConstants.MainWeatherInfoEntity.PRESSURE, mainWeatherInfo.getPressure());
        contentValues.put(DBConstants.MainWeatherInfoEntity.TEMP, mainWeatherInfo.getTemp());
        contentValues.put(DBConstants.MainWeatherInfoEntity.TEMP_MAX, mainWeatherInfo.getTempMax());
        contentValues.put(DBConstants.MainWeatherInfoEntity.TEMP_MIN, mainWeatherInfo.getTempMin());
        database.insert(DBConstants.MainWeatherInfoEntity.TABLE_NAME,null, contentValues);
    }

    private void saveClouds(Clouds clouds) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.CloudsEntity.CLOUDINESS, clouds.getCloudiness());
        database.insert(DBConstants.CloudsEntity.TABLE_NAME, null,contentValues);
    }

    private void saveCoord(Coordinates coordinates) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.CoordinateEntity.LAT, coordinates.getLat());
        contentValues.put(DBConstants.CoordinateEntity.LON, coordinates.getLon());
        database.insert(DBConstants.CoordinateEntity.TABLE_NAME, null,contentValues);

    }

    private void updateWeather(long id, Weather weather) {
        updateCoord(id, weather.getCoord());
        updateClouds(id, weather.getClouds());
        updateMain(id, weather.getMainWeatherInfo());
        updateRain(id, weather.getRain());
        updateSys(id, weather.getSys());
        updateExpandedWeather(id, weather.getExpandedWeatherInfo());
        updateWind(id, weather.getWind());

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.WeatherEntity.NAME, weather.getName());
        contentValues.put(DBConstants.WeatherEntity.COD, weather.getCod());
        contentValues.put(DBConstants.WeatherEntity.DATA, weather.getData());
        database.update(DBConstants.WeatherEntity.TABLE_NAME, contentValues, DBConstants.WeatherEntity._ID + " = ?", new String[]{String.valueOf(id)});
    }

    private void updateWind(long id, Wind wind) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.WindEntity.WIND_DEGREES, wind.getDegrees());
        contentValues.put(DBConstants.WindEntity.SPEED, wind.getSpeed());
        database.update(DBConstants.WindEntity.TABLE_NAME, contentValues, DBConstants.WindEntity._ID + " = ?", new String[]{String.valueOf(id)});
    }

    private void updateExpandedWeather(long weatherId, List<ExpandedWeatherInfo> expandedWeatherInfo) {
        ContentValues contentValues = new ContentValues();
        for (ExpandedWeatherInfo w : expandedWeatherInfo) {
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity._ID, w.getId());
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.ICON, BitmapUtils.encodeToBase64(w.getBitmapIcon(), Bitmap.CompressFormat.PNG, 100));
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.WEATHER_ID, weatherId);
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.MAIN, w.getMain());
            contentValues.put(DBConstants.ExpandedWeatherInfoEntity.DESCRIPTION, w.getDescription());
            database.update(DBConstants.ExpandedWeatherInfoEntity.TABLE_NAME, contentValues, DBConstants.ExpandedWeatherInfoEntity.WEATHER_ID + " = ?", new String[]{String.valueOf(weatherId)});
        }
    }

    private void updateSys(long id, Sys sys) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.SysEntity.COUNTRY, sys.getCountry());
        contentValues.put(DBConstants.SysEntity.SUNRISE, sys.getSunrise());
        contentValues.put(DBConstants.SysEntity.SUNSET, sys.getSunset());
        database.update(DBConstants.SysEntity.TABLE_NAME, contentValues, DBConstants.SysEntity._ID + " = ?", new String[]{String.valueOf(id)});
    }

    private void updateRain(long id, Rain rain) {
        if (rain != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstants.RainEntity._3HOUR, rain.get3h());
            database.update(DBConstants.RainEntity.TABLE_NAME, contentValues, DBConstants.RainEntity._ID + " = ?", new String[]{String.valueOf(id)});
        }
    }

    private void updateMain(long id, MainWeatherInfo mainWeatherInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.MainWeatherInfoEntity.HUMIDITY, mainWeatherInfo.getHumidity());
        contentValues.put(DBConstants.MainWeatherInfoEntity.PRESSURE, mainWeatherInfo.getPressure());
        contentValues.put(DBConstants.MainWeatherInfoEntity.TEMP, mainWeatherInfo.getTemp());
        contentValues.put(DBConstants.MainWeatherInfoEntity.TEMP_MAX, mainWeatherInfo.getTempMax());
        contentValues.put(DBConstants.MainWeatherInfoEntity.TEMP_MIN, mainWeatherInfo.getTempMin());
        database.update(DBConstants.MainWeatherInfoEntity.TABLE_NAME, contentValues, DBConstants.MainWeatherInfoEntity._ID + " = ?", new String[]{String.valueOf(id)});
    }

    private void updateClouds(long id, Clouds clouds) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.CloudsEntity.CLOUDINESS, clouds.getCloudiness());
        database.update(DBConstants.CloudsEntity.TABLE_NAME, contentValues, DBConstants.CloudsEntity._ID + " = ?", new String[]{String.valueOf(id)});
    }

    private void updateCoord(long id, Coordinates coordinates) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.CoordinateEntity.LAT, coordinates.getLat());
        contentValues.put(DBConstants.CoordinateEntity.LON, coordinates.getLon());
        database.update(DBConstants.CoordinateEntity.TABLE_NAME, contentValues, DBConstants.CoordinateEntity._ID + " = ?", new String[]{String.valueOf(id)});

    }


}
