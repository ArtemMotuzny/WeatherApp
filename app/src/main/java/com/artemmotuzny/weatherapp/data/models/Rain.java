
package com.artemmotuzny.weatherapp.data.models;

import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("3h")
    private Double _3hour;

    public Double get3h() {
        return _3hour;
    }

    public void set3h(Double _3h) {
        this._3hour = _3h;
    }

}
