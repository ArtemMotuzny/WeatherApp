package com.artemmotuzny.weatherapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.artemmotuzny.weatherapp.R;
import com.artemmotuzny.weatherapp.contract.WeatherContract;
import com.artemmotuzny.weatherapp.presenter.WeatherPresenter;
import com.bumptech.glide.Glide;

/**
 * Created by tema_ on 12.10.2016.
 */

public class WeatherFragment extends Fragment implements WeatherContract.View{
    private WeatherContract.Presenter presenter;
    private TextView info;
    private ImageView icon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragmenr_weather,container,false);

        info = (TextView)root.findViewById(R.id.info);
        icon = (ImageView)root.findViewById(R.id.icon);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void setPresenter(WeatherPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setWeatherText(String weather) {
        info.setText(weather);
    }

    @Override
    public void setIcon(String pathToIcon) {
        Glide.with(getContext()).load(getString(R.string.icon_path)+pathToIcon+getString(R.string.png)).into(icon);
    }
}
