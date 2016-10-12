package com.artemmotuzny.weatherapp.presenter;

import android.location.Location;

import com.artemmotuzny.weatherapp.contract.WeatherContract;
import com.artemmotuzny.weatherapp.data.device.LocationApi;
import com.artemmotuzny.weatherapp.data.model.MyWeather;
import com.artemmotuzny.weatherapp.data.remote.RetrofitService;
import com.artemmotuzny.weatherapp.data.remote.WeatherApi;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tema_ on 12.10.2016.
 */
public class WeatherPresenter implements WeatherContract.Presenter{
    private WeatherApi weatherApi;
    private LocationApi locationApi;
    private WeatherContract.View view;
    private CompositeSubscription compositeSubscription;

    public WeatherPresenter(LocationApi locationApi, WeatherApi weatherApi, WeatherContract.View view) {
        this.locationApi = locationApi;
        this.weatherApi = weatherApi;
        this.view = view;

        compositeSubscription = new CompositeSubscription();
        view.setPresenter(this);
    }


    @Override
    public void loadWeather() {
        Subscription subscription = locationApi.getLocation().flatMap(new Func1<Location, Observable<MyWeather>>() {
            @Override
            public Observable<MyWeather> call(Location location) {
                Map<String, String > qMap = new HashMap<>();
                qMap.put("lat", String.valueOf(location.getLatitude()));
                qMap.put("lon", String.valueOf(location.getLongitude()));
                qMap.put("appid",RetrofitService.APP_ID);
                qMap.put("lang","ru");
                qMap.put("units", "metric");
                return weatherApi.getWeatherByLocation(qMap);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyWeather>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setWeatherText(e.toString());
                    }

                    @Override
                    public void onNext(MyWeather myWeather) {
                        view.setWeatherText(myWeather.toString());
                        view.setIcon(myWeather.getWeather().get(0).getIcon());
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
