package com.artemmotuzny.weatherapp.presenter;

import android.util.Log;

import com.artemmotuzny.weatherapp.contract.WeatherContract;
import com.artemmotuzny.weatherapp.data.WeatherRepository;
import com.artemmotuzny.weatherapp.data.models.Weather;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tema_ on 12.10.2016.
 */
public class WeatherPresenter implements WeatherContract.Presenter{
    private WeatherRepository weatherRepository;
    private WeatherContract.View view;
    private CompositeSubscription compositeSubscription;

    public WeatherPresenter(WeatherContract.View view, WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
        this.view = view;

        compositeSubscription = new CompositeSubscription();
        view.setPresenter(this);
    }


    @Override
    public void loadWeather() {
        Subscription subscription = weatherRepository.getWeather()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Weather>() {
                    @Override
                    public void onCompleted() {
                        Log.d("ASE","ASE");
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setWeatherText(e.toString());
                    }

                    @Override
                    public void onNext(Weather weatherData) {
                        view.setWeatherText(weatherData.getName());
                        view.setIcon(weatherData.getExpandedWeatherInfo().get(0).getBitmapIcon());
                    }
                });

        compositeSubscription.add(subscription);
    }

    @Override
    public void subscribe() {
        loadWeather();
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.clear();
    }
}
