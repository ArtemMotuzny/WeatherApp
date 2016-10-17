package com.artemmotuzny.weatherapp.data.remote.api;



import java.util.Map;
import com.artemmotuzny.weatherapp.data.models.Weather;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by tema_ on 10.10.2016.
 */


public interface WeatherApi {
    @GET("/data/2.5/weather")
    Observable<Weather> getWeatherByLocation(@QueryMap Map<String, String> map);
}
