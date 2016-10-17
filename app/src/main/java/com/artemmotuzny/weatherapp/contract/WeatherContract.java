package com.artemmotuzny.weatherapp.contract;

import android.graphics.Bitmap;

import com.artemmotuzny.weatherapp.presenter.WeatherPresenter;

/**
 * Created by tema_ on 12.10.2016.
 */

public class WeatherContract {
    public interface View{
        void setPresenter(WeatherPresenter presenter);

        void setWeatherText(String weather);

        void setIcon(Bitmap pathToIcon);
    }

    public interface Presenter{
        void loadWeather();

        void subscribe();

        void unsubscribe();
    }
}
