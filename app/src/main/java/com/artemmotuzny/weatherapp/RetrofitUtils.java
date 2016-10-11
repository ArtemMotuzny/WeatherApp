package com.artemmotuzny.weatherapp;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by tema_ on 10.10.2016.
 */

public class RetrofitUtils {
    private static final String BASE_URL = "http://api.openweathermap.org";
    private static WeatherApi weatherApi;

    public static WeatherApi getApi(){
        if(weatherApi == null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
            weatherApi  = retrofit.create(WeatherApi.class);
        }
        return weatherApi;
    }

}
