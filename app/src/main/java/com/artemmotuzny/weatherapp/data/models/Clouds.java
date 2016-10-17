
package com.artemmotuzny.weatherapp.data.models;

import com.google.gson.annotations.SerializedName;

public class Clouds {

    @SerializedName("all")
    private Integer cloudiness;

    /**
     * 
     * @return
     *     The cloudiness
     */
    public Integer getCloudiness() {
        return cloudiness;
    }

    /**
     * 
     * @param cloudiness
     *     The cloudiness
     */
    public void setCloudiness(Integer cloudiness) {
        this.cloudiness = cloudiness;
    }

}
