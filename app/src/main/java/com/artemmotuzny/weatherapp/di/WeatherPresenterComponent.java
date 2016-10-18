package com.artemmotuzny.weatherapp.di;

import com.artemmotuzny.weatherapp.presenter.WeatherPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by tema_ on 17.10.2016.
 */

@Singleton
@Component(modules = WeatherPresenterModule.class)
public interface WeatherPresenterComponent {
    void inject(WeatherPresenter presenter);
}
