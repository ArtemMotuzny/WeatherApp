package com.artemmotuzny.weatherapp.data;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.artemmotuzny.weatherapp.device_services.NetworkConnectService;
import com.artemmotuzny.weatherapp.data.local.LocalDataSource;
import com.artemmotuzny.weatherapp.data.models.ExpandedWeatherInfo;
import com.artemmotuzny.weatherapp.data.models.Weather;
import com.artemmotuzny.weatherapp.data.remote.RemoteDataSource;
import com.artemmotuzny.weatherapp.utils.BitmapUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by tema_ on 13.10.2016.
 */

public class WeatherRepositoryImpl implements WeatherRepository {
    private WeatherRepository remoteDataSource;
    private WeatherRepository localDataSource;
    private NetworkConnectService networkConnect;

    public WeatherRepositoryImpl(RemoteDataSource remoteDataSource, LocalDataSource localDataSource, NetworkConnectService connectService) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
        this.networkConnect = connectService;
    }

    @Override
    public Observable<Weather> getWeatherByLocation(final Location location) {
        if (!networkConnect.isConnected()) {
            return localDataSource.getWeatherByLocation(location);
        } else {
            return remoteDataSource.getWeatherByLocation(location).doOnNext(weather -> {
                //Convert icon to base64
                List<ExpandedWeatherInfo> expandedWeatherInfoList = weather.getExpandedWeatherInfo();
                if (expandedWeatherInfoList != null && !expandedWeatherInfoList.isEmpty()) {
                    ExpandedWeatherInfo expandedWeatherInfo = expandedWeatherInfoList.get(0);
                    Bitmap icon = BitmapUtils.getBitmapFromUrl(expandedWeatherInfo.getIcon());
                    expandedWeatherInfo.setBitmapIcon(icon);
                }

                ////change coordinates received from the server on the local coordinates because we get coordinates nearest town.
                weather.getCoord().setLat(location.getLatitude());
                weather.getCoord().setLon(location.getLongitude());

                //save weather to db
                localDataSource.saveWeather(weather);
            });
        }
    }

    @Override
    public void saveWeather(Weather weather) {

    }

}
