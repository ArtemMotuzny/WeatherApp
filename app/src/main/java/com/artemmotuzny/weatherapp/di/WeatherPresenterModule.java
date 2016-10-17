package com.artemmotuzny.weatherapp.di;

import android.content.Context;

import com.artemmotuzny.weatherapp.data.WeatherRepository;
import com.artemmotuzny.weatherapp.data.WeatherRepositoryImpl;
import com.artemmotuzny.weatherapp.data.device.LocationApi;
import com.artemmotuzny.weatherapp.data.device.LocationService;
import com.artemmotuzny.weatherapp.data.device.NetworkConnectService;
import com.artemmotuzny.weatherapp.data.local.LocalDataSource;
import com.artemmotuzny.weatherapp.data.remote.RemoteDataSource;
import com.artemmotuzny.weatherapp.data.remote.RetrofitService;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tema_ on 17.10.2016.
 */


@Module
public class WeatherPresenterModule {
    private Context context;

    public WeatherPresenterModule(Context context){
        this.context = context;
    }

    @Provides
    public RemoteDataSource provideRemoteDataSource(){
        return new RemoteDataSource(RetrofitService.getApi());
    }

    @Provides
    public LocalDataSource provideLocalDataSource(){
        return new LocalDataSource(context);
    }

    @Provides
    public NetworkConnectService provideNetwork(){
        return new NetworkConnectService(context);
    }

    @Provides
    public LocationApi provaideLocation(){
        return new LocationService(context);
    }

    @Provides
    public WeatherRepository provideRepository(RemoteDataSource remote, LocalDataSource local, NetworkConnectService networkConnectService){
        return new WeatherRepositoryImpl(remote,local,networkConnectService);
    }


}
