package com.artemmotuzny.weatherapp.data.remote;



import java.util.Map;
import com.artemmotuzny.weatherapp.data.model.MyWeather;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by tema_ on 10.10.2016.
 */


public interface WeatherApi {
    @GET("/data/2.5/weather")
    Observable<MyWeather> getWeatherByLocation(@QueryMap Map<String, String> map);
}
