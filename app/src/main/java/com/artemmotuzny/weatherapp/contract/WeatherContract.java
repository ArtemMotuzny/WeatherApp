package com.artemmotuzny.weatherapp.contract;

import android.graphics.Bitmap;

import com.artemmotuzny.weatherapp.presenter.WeatherPresenter;

/**
 * Created by tema_ on 12.10.2016.
 */

public class WeatherContract {
    public interface View{
        void setPresenter(WeatherPresenter presenter);

        void setWeatherText(String country, String cityName, Double temp, Integer cloudiness, String description);

        void setIcon(Bitmap pathToIcon);

        void setErrorText();

        void setErrorText(String message);
    }

    public interface Presenter{
        void loadWeather();

        void subscribe();

        void unsubscribe();
    }
}
