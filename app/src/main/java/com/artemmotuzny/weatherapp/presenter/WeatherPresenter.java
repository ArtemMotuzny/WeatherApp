package com.artemmotuzny.weatherapp.presenter;

import android.util.Log;

import com.artemmotuzny.weatherapp.contract.WeatherContract;
import com.artemmotuzny.weatherapp.data.WeatherRepository;
import com.artemmotuzny.weatherapp.data.models.ExpandedWeatherInfo;
import com.artemmotuzny.weatherapp.data.models.Weather;

import java.util.List;

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
                        Log.d("Subscribe - onCompleted","onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e instanceof NullPointerException){
                            view.setErrorText();
                        }else {
                            view.setErrorText(e.getMessage());
                        }

                    }

                    @Override
                    public void onNext(Weather weather) {
                        view.setWeatherText(weather.getSys().getCountry(),weather.getName(),weather.getMainWeatherInfo().getTemp(),weather.getClouds().getCloudiness(),weather.getExpandedWeatherInfo().get(0).getDescription());
                        List<ExpandedWeatherInfo> expandedWeatherInfos = weather.getExpandedWeatherInfo();
                        if(expandedWeatherInfos!=null && !expandedWeatherInfos.isEmpty()){
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
}
