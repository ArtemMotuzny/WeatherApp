package com.artemmotuzny.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.artemmotuzny.weatherapp.model.MyWeather;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tema_ on 10.10.2016.
 */

public class MainActivity extends AppCompatActivity {

    private double lat;
    private double lon;

    private ImageView view;
    private TextView info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getLocation();
    }

    private void initView() {
        view = (ImageView) findViewById(R.id.icon);
        info = (TextView) findViewById(R.id.info);
    }

    private void getWeather() {
        WeatherApi weatherApi = RetrofitUtils.getApi();


        Map<String, String> locMap = new HashMap<>();
        locMap.put("lat", String.valueOf(lat));
        locMap.put("lon", String.valueOf(lon));
        locMap.put("appid", RetrofitUtils.APP_ID);
        locMap.put("units", "metric");
        locMap.put("lang", "ru");

        Call<MyWeather> call = weatherApi.getWeatherByLocation(locMap);
        call.enqueue(new Callback<MyWeather>() {
            @Override
            public void onResponse(Call<MyWeather> call, Response<MyWeather> response) {
                MyWeather weather = response.body();
                setData(weather);
            }

            @Override
            public void onFailure(Call<MyWeather> call, Throwable t) {
                Log.e("failure: ", t.toString());
            }
        });
    }

    private void setData(MyWeather weather) {
        String imagePath = RetrofitUtils.ICON_URL + weather.getWeather().get(0).getIcon() + ".png";
        Glide.with(this).load(imagePath).into(view);
        info.setText(weather.toString());
    }

    private void getLocation() {
        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            showToast("Для работы приложения необходимо разрешение на определение местопложения");
            finish();
            return;
        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setLocation(location);
                getWeather();
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


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            showToast("Для работы приложения необходимо разрешение на определение местопложения");
            finish();
        }

    }

    private void setLocation(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
