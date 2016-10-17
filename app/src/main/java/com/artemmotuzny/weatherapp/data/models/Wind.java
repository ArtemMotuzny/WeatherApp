
package com.artemmotuzny.weatherapp.data.models;

import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    private Double speed;
    @SerializedName("deg")
    private Double degrees;

    /**
     * 
     * @return
     *     The speed
     */
    public Double getSpeed() {
        return speed;
    }

    /**
     * 
     * @param speed
     *     The speed
     */
    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    /**
     * 
     * @return
     *     The degrees
     */
    public Double getDegrees() {
        return degrees;
    }

    /**
     * 
     * @param degrees
     *     The degrees
     */
    public void setDegrees(Double degrees) {
        this.degrees = degrees;
    }

}
