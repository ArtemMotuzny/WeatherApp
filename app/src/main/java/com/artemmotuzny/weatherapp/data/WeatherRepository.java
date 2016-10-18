package com.artemmotuzny.weatherapp.data;

import android.location.Location;

import com.artemmotuzny.weatherapp.data.models.Weather;

import rx.Observable;

/**
 * Created by tema_ on 13.10.2016.
 */

public interface WeatherRepository{
    Observable<Weather> getWeatherByLocation(Location location);

    void saveOrUpdateWeather(Weather weather);
}
