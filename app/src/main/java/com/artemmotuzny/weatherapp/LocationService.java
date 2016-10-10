package com.artemmotuzny.weatherapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * Created by tema_ on 10.10.2016.
 */

public class LocationService extends Service implements LocationListener {
    private Context context;
    private boolean isCanProvideLocation = false;
    private boolean networkProvide = false;
    private boolean gpsProvide = false;
    private Location location;
    private LocationManager locationManager;

    public LocationService(Context context) {
        this.context = context;
        location = getNewLocation();
    }

    private Location getNewLocation() {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        gpsProvide = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkProvide = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!gpsProvide && !networkProvide) {
            isCanProvideLocation = false;
        } else {
            isCanProvideLocation = true;
            if(Build.VERSION.SDK_INT>=23){
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
            }
            if (gpsProvide) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (networkProvide) {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, this);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
        }
        return location;
    }

    public Location getLocation() {
        return this.location;
    }

    public boolean isCanProvideLocation() {
        return isCanProvideLocation;
    }

    public void stopProvide() {
        if(locationManager!=null){
            if(Build.VERSION.SDK_INT>=23){
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            locationManager.removeUpdates(this);
            isCanProvideLocation = false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

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
