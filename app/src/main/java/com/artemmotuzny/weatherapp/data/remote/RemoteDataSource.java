package com.artemmotuzny.weatherapp.data.remote;

import android.location.Location;

import com.artemmotuzny.weatherapp.data.WeatherRepository;
import com.artemmotuzny.weatherapp.data.models.Weather;
import com.artemmotuzny.weatherapp.data.remote.api.WeatherApi;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by tema_ on 17.10.2016.
 */

public class RemoteDataSource implements WeatherRepository {
    private WeatherApi weatherApi;

    public RemoteDataSource(WeatherApi weatherApi) {
        this.weatherApi = weatherApi;
    }

    @Override
    public Observable<Weather> getWeatherByLocation(Location location) {
        Map<String, String > qMap = new HashMap<>();
        qMap.put("lat", String.valueOf(location.getLatitude()));
        qMap.put("lon", String.valueOf(location.getLongitude()));
        qMap.put("appid", RetrofitService.APP_ID);
        qMap.put("lang", "ru");
        qMap.put("units", "metric");
        return weatherApi.getWeatherByLocation(qMap);
    }

    @Override
    public void saveWeather(Weather weather) {
    }
}
