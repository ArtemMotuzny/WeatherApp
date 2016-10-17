package com.artemmotuzny.weatherapp.data;

import android.location.Location;

import com.artemmotuzny.weatherapp.data.models.ExpandedWeatherInfo;
import com.artemmotuzny.weatherapp.utils.BitmapUtils;
import com.artemmotuzny.weatherapp.data.device.LocationApi;
import com.artemmotuzny.weatherapp.data.device.NetworkConnectService;
import com.artemmotuzny.weatherapp.data.local.DataBase;
import com.artemmotuzny.weatherapp.data.models.Weather;
import com.artemmotuzny.weatherapp.data.remote.RetrofitService;
import com.artemmotuzny.weatherapp.data.remote.WeatherApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tema_ on 13.10.2016.
 */

public class WeatherRepositoryImpl implements WeatherRepository {
    private LocationApi locationApi;
    private WeatherApi weatherApi;
    private NetworkConnectService connectService;
    private DataBase dataBase;
    private Location coordinates;

    public WeatherRepositoryImpl(LocationApi locationApi, WeatherApi weatherApi, DataBase dataBase, NetworkConnectService connectService) {
        this.locationApi = locationApi;
        this.weatherApi = weatherApi;
        this.dataBase = dataBase;
        this.connectService = connectService;
    }

    @Override
    public Observable<Weather> getWeather() {
        return locationApi.getLocation().flatMap(new Func1<Location, Observable<Weather>>() {
            @Override
            public Observable<Weather> call(Location location) {
                if (!connectService.getConnectState()) {
                    return dataBase.getWeatherByLocation(location.getLatitude(),location.getLongitude());
                }else {
                    coordinates = location;
                    Map<String, String> qMap = new HashMap<>();
                    qMap.put("lat", String.valueOf(location.getLatitude()));
                    qMap.put("lon", String.valueOf(location.getLongitude()));
                    qMap.put("appid", RetrofitService.APP_ID);
                    qMap.put("lang", "ru");
                    qMap.put("units", "metric");
                    return weatherApi.getWeatherByLocation(qMap);
                }
            }
        }).doOnNext(new Action1<Weather>() {
            @Override
            public void call(Weather weather) {
                if (connectService.getConnectState()) {
                    //convert icon to base64
                    ExpandedWeatherInfo expandedWeatherInfo = weather.getExpandedWeatherInfo().get(0);
                    expandedWeatherInfo.setBitmapIcon(BitmapUtils.getBitmapFromUrl(expandedWeatherInfo.getIcon()));

                    //change coordinates received from the server on the local coordinates because we get coordinates nearest town.
                    weather.getCoord().setLat(coordinates.getLatitude());
                    weather.getCoord().setLon(coordinates.getLongitude());

                    //save weather with ours coordinates
                    dataBase.saveWeatherData(weather);
                }
            }
        });
    }
}
