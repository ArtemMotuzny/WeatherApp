package com.artemmotuzny.weatherapp.data.device;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

import com.artemmotuzny.weatherapp.R;
import com.artemmotuzny.weatherapp.event.PermissionEvent;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by tema_ on 10.10.2016.
 */

public class LocationService implements LocationApi {
    private boolean networkProvide = false;
    private boolean gpsProvide = false;
    private String typeProvider;
    private LocationManager locationManager;
    private Context context;

    public LocationService(Context context) {
        this.context = context;
        providerType();
    }

    private void providerType() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (isCanProvideLocation()) {
            if (gpsProvide) {
                typeProvider = LocationManager.GPS_PROVIDER;
            }
            if (networkProvide&& typeProvider.isEmpty()) {
                typeProvider = LocationManager.NETWORK_PROVIDER;
            }
        }
    }

    private boolean isCanProvideLocation() {
        gpsProvide = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkProvide = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return !(!gpsProvide && !networkProvide);
    }


    @Override
    public Observable<Location> getLocation() {
        return Observable.create(new Observable.OnSubscribe<Location>() {
            @Override
            public void call(final Subscriber<? super Location> subscriber) {
                if (!isCanProvideLocation()) {
                    subscriber.onError(new Throwable("Включите gps"));
                }
                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        subscriber.onNext(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    EventBus.getDefault().post(new PermissionEvent(context.getString(R.string.event_check)));
                    subscriber.onError(new Throwable("Permission error"));
                    return;
                }

                //Without looper prepare we have error(can't create handler inside thread that has not called looper.prepare())
                Looper.prepare();
                locationManager.requestLocationUpdates(typeProvider, 30000, 20, locationListener, Looper.myLooper());
                Location location = locationManager.getLastKnownLocation(typeProvider);
                if (location == null) {
                    subscriber.onError(new Throwable("Локация не доступна"));
                    return;
                }
                subscriber.onNext(location);
                Looper.loop();

            }
        });
    }
}