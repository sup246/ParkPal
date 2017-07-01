package com.psu.sweng500.team4.parkpal.Models.Weather;

/**
 * Created by brhoads on 7/1/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord {

    @SerializedName("lon")
    @Expose
    private Integer lon;
    @SerializedName("lat")
    @Expose
    private Integer lat;

    public Integer getLon() {
        return lon;
    }

    public void setLon(Integer lon) {
        this.lon = lon;
    }

    public Integer getLat() {
        return lat;
    }

    public void setLat(Integer lat) {
        this.lat = lat;
    }


}