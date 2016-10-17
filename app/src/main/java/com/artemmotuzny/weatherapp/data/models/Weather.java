
package com.artemmotuzny.weatherapp.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Weather {

    @SerializedName("coord")
    private Coordinates coord;
    @SerializedName("sys")
    private Sys sys;
    @SerializedName("weather")
    private List<ExpandedWeatherInfo> expandedWeatherInfo = new ArrayList<ExpandedWeatherInfo>();
    @SerializedName("main")
    private MainWeatherInfo mainWeatherInfo;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("rain")
    private Rain rain;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("dt")
    private Integer data;
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("cod")
    private Integer cod;

    /**
     * @return The coord
     */
    public Coordinates getCoord() {
        return coord;
    }

    /**
     * @param coordinates The coordinates
     */
    public void setCoord(Coordinates coordinates) {
        this.coord = coordinates;
    }

    /**
     * @return The sys
     */
    public Sys getSys() {
        return sys;
    }

    /**
     * @param sys The sys
     */
    public void setSys(Sys sys) {
        this.sys = sys;
    }

    /**
     * @return The expandedWeatherInfo
     */
    public List<ExpandedWeatherInfo> getExpandedWeatherInfo() {
        return expandedWeatherInfo;
    }

    /**
     * @param expandedWeatherInfo The expandedWeatherInfo
     */
    public void setExpandedWeatherInfo(List<ExpandedWeatherInfo> expandedWeatherInfo) {
        this.expandedWeatherInfo = expandedWeatherInfo;
    }

    /**
     * @return The mainWeatherInfo
     */
    public MainWeatherInfo getMainWeatherInfo() {
        return mainWeatherInfo;
    }

    /**
     * @param mainWeatherInfo The mainWeatherInfo
     */
    public void setMainWeatherInfo(MainWeatherInfo mainWeatherInfo) {
        this.mainWeatherInfo = mainWeatherInfo;
    }

    /**
     * @return The wind
     */
    public Wind getWind() {
        return wind;
    }

    /**
     * @param wind The wind
     */
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    /**
     * @return The rain
     */
    public Rain getRain() {
        return rain;
    }

    /**
     * @param rain The rain
     */
    public void setRain(Rain rain) {
        this.rain = rain;
    }

    /**
     * @return The clouds
     */
    public Clouds getClouds() {
        return clouds;
    }

    /**
     * @param clouds The clouds
     */
    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    /**
     * @return The data
     */
    public Integer getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(Integer data) {
        this.data = data;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The cod
     */
    public Integer getCod() {
        return cod;
    }

    /**
     * @param cod The cod
     */
    public void setCod(Integer cod) {
        this.cod = cod;
    }
}
