
package com.artemmotuzny.weatherapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord {

    @SerializedName("lon")
    @Expose
    private Double lon;


    @SerializedName("lat")
    @Expose
    private Double lat;

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLon() {
        return lon;
    }

    public Double getLat() {
        return lat;
    }
}
