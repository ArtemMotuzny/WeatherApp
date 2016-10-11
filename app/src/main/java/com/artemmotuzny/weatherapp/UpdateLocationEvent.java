package com.artemmotuzny.weatherapp;

import android.location.Location;

/**
 * Created by tema_ on 11.10.2016.
 */

public class UpdateLocationEvent {
    private Location location;

    public UpdateLocationEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
