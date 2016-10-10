package com.artemmotuzny.weatherapp;

import com.artemmotuzny.weatherapp.model.MyWeather;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by tema_ on 10.10.2016.
 */


public interface WeatherApi {
    @GET("/data/2.5/weather")
    Call<MyWeather> getWeatherByLocation(@QueryMap Map<String, String> map);
}
