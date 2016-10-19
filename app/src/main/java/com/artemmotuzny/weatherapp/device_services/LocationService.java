package com.artemmotuzny.weatherapp.device_services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

import com.artemmotuzny.weatherapp.R;
import com.artemmotuzny.weatherapp.event.PermissionEvent;
import com.artemmotuzny.weatherapp.event.SubscribeEvent;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by tema_ on 10.10.2016.
 */

public class LocationService implements LocationApi {
    private LocationManager locationManager;
    private Context context;
    private LocationListener locationListener;

    private boolean isGpsEnable = false;
    private boolean isNetworkEnable = false;

    public LocationService(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    private boolean isCanProvideLocation() {
        isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return !(!isGpsEnable && !isNetworkEnable);
    }


    @Override
    public Observable<Location> getLocation() {
        return Observable.create(new Observable.OnSubscribe<Location>() {
            @Override
            public void call(final Subscriber<? super Location> subscriber) {
                if (!isCanProvideLocation()) {
                    subscriber.onError(new Throwable("Включите gps и перезапустите приложение"));
                    return;
                }
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        if(!subscriber.isUnsubscribed()){
                            subscriber.onNext(location);
                        }else {
                            EventBus.getDefault().post(new SubscribeEvent());
                        }
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
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                Location location = getProviderLocation(locationListener);
                if (location == null) {
                    subscriber.onError(new Throwable("Локация не доступна"));
                    return;
                }
                subscriber.onNext(location);
                Looper.loop();

            }
        });
    }

    private Location getProviderLocation(LocationListener locationListener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            EventBus.getDefault().post(new PermissionEvent(context.getString(R.string.event_check)));
            return null;
        }

        //if Network Enabled get lat/long using Network Provider
        Location location = null;
        if (isNetworkEnable) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 5, locationListener);
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        }
        // if GPS Enabled get lat/long using GPS Provider
        if (isGpsEnable) {
            if (location == null) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 3000, 5, locationListener);
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
        return location;
    }
}