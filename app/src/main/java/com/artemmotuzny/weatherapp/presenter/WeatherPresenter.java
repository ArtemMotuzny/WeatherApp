package com.artemmotuzny.weatherapp.presenter;

import android.content.Context;
import android.util.Log;

import com.artemmotuzny.weatherapp.contract.WeatherContract;
import com.artemmotuzny.weatherapp.data.WeatherRepository;
import com.artemmotuzny.weatherapp.device_services.LocationApi;
import com.artemmotuzny.weatherapp.data.models.ExpandedWeatherInfo;
import com.artemmotuzny.weatherapp.data.models.Weather;
import com.artemmotuzny.weatherapp.di.DaggerWeatherPresenterComponent;
import com.artemmotuzny.weatherapp.di.WeatherPresenterModule;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tema_ on 12.10.2016.
 */
public class WeatherPresenter implements WeatherContract.Presenter {
    @Inject
    WeatherRepository weatherRepository;

    @Inject
    LocationApi locationApi;

    private WeatherContract.View view;
    private CompositeSubscription compositeSubscription;

    public WeatherPresenter(WeatherContract.View view) {
        this.view = view;
        view.setPresenter(this);

        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void inject() {
        Context context = view.getViewContext();
        injectDependencies(context);
    }

    @Override
    public void loadWeather() {
        Subscription subscription = locationApi.getLocation()
                .flatMap(location -> weatherRepository.getWeatherByLocation(location))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Weather>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Subscribe - onCompleted", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof NullPointerException) {
                            view.setErrorText();
                        } else {
                            view.setErrorText(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Weather weather) {
                        view.setWeatherText(weather.getSys().getCountry(), weather.getName(), weather.getMainWeatherInfo().getTemp(), weather.getClouds().getCloudiness(), weather.getExpandedWeatherInfo().get(0).getDescription());
                        List<ExpandedWeatherInfo> expandedWeatherInfos = weather.getExpandedWeatherInfo();
                        if (expandedWeatherInfos != null && !expandedWeatherInfos.isEmpty()) {
                            ExpandedWeatherInfo expandedWeatherInfo = expandedWeatherInfos.get(0);
                            view.setIcon(expandedWeatherInfo.getBitmapIcon());
                        }
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

    private void injectDependencies(Context context) {
        DaggerWeatherPresenterComponent.builder().weatherPresenterModule(new WeatherPresenterModule(context))
                .build().inject(this);
    }
}
