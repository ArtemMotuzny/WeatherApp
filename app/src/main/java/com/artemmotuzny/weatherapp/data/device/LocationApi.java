package com.artemmotuzny.weatherapp.data.device;

import android.location.Location;

import rx.Observable;

/**
 * Created by tema_ on 12.10.2016.
 */

public interface LocationApi {
    Observable<Location> getLocation();
}
