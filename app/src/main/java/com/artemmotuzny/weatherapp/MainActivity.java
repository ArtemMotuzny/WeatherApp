package com.artemmotuzny.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.artemmotuzny.weatherapp.model.MyWeather;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tema_ on 10.10.2016.
 */

public class MainActivity extends AppCompatActivity{
    private static final int REQ_PER_CODE = 101;

    private LocationUtil locationUtil;
    private ImageView view;
    private TextView info;
    private boolean permissionCheck = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLocationPermission();
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void myCheckPermission() {
        int perm = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if(perm != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQ_PER_CODE);
        }else {
            permissionCheck = true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQ_PER_CODE:
                if(grantResults.length==0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    showToast(getString(R.string.error_permission));
                    finish();
                }else {
                    permissionCheck = true;
                    getLocation();
                }
        }
    }

    private void initView() {
        view = (ImageView) findViewById(R.id.icon);
        info = (TextView) findViewById(R.id.info);
    }

    private void getWeather(double lat, double lon) {
        WeatherApi weatherApi = RetrofitUtils.getApi();


        Map<String, String> locMap = new HashMap<>();
        locMap.put("lat", String.valueOf(lat));
        locMap.put("lon", String.valueOf(lon));
        locMap.put("appid", getString(R.string.appid));
        locMap.put("units", getString(R.string.unit_value));
        locMap.put("lang", getString(R.string.lang_value));

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
        String imagePath = getString(R.string.icon_path) + weather.getWeather().get(0).getIcon() + getString(R.string.png);
        Glide.with(this).load(imagePath).into(view);
        info.setText(weather.toString());
    }

    private void getLocation() {
        if(!permissionCheck){return;}
        if (locationUtil == null){locationUtil = new LocationUtil(this);}
        Location location = locationUtil.getLocation();
        if(location !=null){
            setLocation(location);
        }else {
            showToast(getString(R.string.input_error));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        getLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        if(locationUtil !=null){
            locationUtil.stopProvide();
            locationUtil = null;
        }
    }

    private void setLocation(Location location) {
        getWeather(location.getLatitude(),location.getLongitude());
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Subscribe
    public void onUpdateLocationEvent(UpdateLocationEvent event){
        getWeather(event.getLocation().getLatitude(),event.getLocation().getLongitude());
    }

    @Subscribe
    public void onPermissionEvent(PermissionEvent event){
        getLocationPermission();
    }

    public void getLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myCheckPermission();
        }else {
            permissionCheck = true;
        }
    }
}
