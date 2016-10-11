package com.artemmotuzny.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by tema_ on 10.10.2016.
 */

public class LocationUtil implements LocationListener {

    //дистанция в метрах - 1 км
    private static final int DISTANCE = 1000;
    //Время в милисикундах - 1 час
    private static final int TIME_HOUR = 3600000;


    private boolean networkProvide = false;
    private boolean gpsProvide = false;
    private LocationManager locationManager;
    private Context context;

    LocationUtil(Context context) {
        this.context = context;
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }

    //Получаем новую локацию
    Location getLocation() {
        Location location = null;
        if (isCanProvideLocation()) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                EventBus.getDefault().post(new PermissionEvent(context.getString(R.string.event_check)));
                return null;
            }
            if (networkProvide) {

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_HOUR, DISTANCE, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            }
            if (gpsProvide) {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_HOUR, DISTANCE, this);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
        return location;
    }

    //Проверка доступности провейдеров
    boolean isCanProvideLocation() {
        gpsProvide = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkProvide = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return !(!gpsProvide && !networkProvide);
    }

    //удаляем слушатель
    void stopProvide() {
        if(locationManager!=null){
            int locationPermission = ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION);
            if(locationPermission == PackageManager.PERMISSION_GRANTED){
                locationManager.removeUpdates(this);
            }

        }
    }

    //При изминении координат будет отправляться сообщение об этом
    @Override
    public void onLocationChanged(Location location) {
        EventBus.getDefault().post(new UpdateLocationEvent(location));
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
}
