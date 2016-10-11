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
import android.support.v4.content.ContextCompat;

/**
 * Created by tema_ on 10.10.2016.
 */

public class LocationService extends Service implements LocationListener {
    private static final int MY_PERMISSION_REQUEST_LOCATION = 1;
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
        int locationPermission = ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION);
        if(locationPermission != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT>=23){
                ((MainActivity)context).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
            }
            return null;
        }


        if (isCanProvideLocation()){
            isCanProvideLocation = true;
            if (networkProvide) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, this);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            }
            if (gpsProvide) {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, this);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
        }
        return location;
    }

    public Location getLocation() {
        return this.location;
    }

    public boolean isCanProvideLocation() {
        gpsProvide = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkProvide = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return !(!gpsProvide && !networkProvide);
    }

    public void stopProvide() {
        if(locationManager!=null){
            int locationPermission = ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION);
            if(locationPermission == PackageManager.PERMISSION_GRANTED){
                locationManager.removeUpdates(this);
            }

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
