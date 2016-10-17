package com.artemmotuzny.weatherapp.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.artemmotuzny.weatherapp.R;
import com.artemmotuzny.weatherapp.event.PermissionEvent;
import com.artemmotuzny.weatherapp.presenter.WeatherPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by tema_ on 10.10.2016.
 */

public class WeatherActivity extends AppCompatActivity {
    private static final int REQ_PER_CODE = 101;
    private boolean updateViewOnResume = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        myCheckPermission();
    }


    private void myCheckPermission() {
        if(Build.VERSION.SDK_INT>=23){
            int perm = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (perm != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PER_CODE);
                return;
            }
        }
        initView();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PER_CODE:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast(getString(R.string.error_permission));
                    finish();
                }else {
                    updateViewOnResume = true;
                }
        }
    }

    private void initView() {
        WeatherFragment weatherFragment = new WeatherFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.container,weatherFragment,getString(R.string.weather_fragment_tag)).commit();

        new WeatherPresenter(this,weatherFragment);
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        if(updateViewOnResume){
            initView();
            updateViewOnResume = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    @Subscribe
    public void onPermissionEvent(PermissionEvent event) {
        myCheckPermission();
    }
}
