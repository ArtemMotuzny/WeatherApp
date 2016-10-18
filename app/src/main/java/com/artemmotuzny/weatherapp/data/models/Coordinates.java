
package com.artemmotuzny.weatherapp.data.models;

import com.google.gson.annotations.SerializedName;

public class Coordinates {
    transient long dbId;

    @SerializedName("lon")
    private Double lon;


    @SerializedName("lat")
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

    public long getDbId() {
        return dbId;
    }

    public void setDbId(long dbId) {
        this.dbId = dbId;
    }
}
