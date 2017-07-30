package com.psu.sweng500.team4.parkpal.Models;

import java.util.Date;

/**
 * Created by brhoads on 7/30/2017.
 */

public class ParkRating {

    @com.google.gson.annotations.SerializedName("park_id")
    private long park_id;
    @com.google.gson.annotations.SerializedName("username")
    private String username;
    @com.google.gson.annotations.SerializedName("rating")
    private int rating;
    @com.google.gson.annotations.SerializedName("user_id")
    private Date user_id;

    private String id;

    public String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public ParkRating() {
    }

    public ParkRating(long parkId, String username, int rating) {
        this.park_id = parkId;
        this.username = username;
        this.rating = rating;
    }

    public long getPark_id() {
        return park_id;
    }

    public void setPark_id(long park_id) {
        this.park_id = park_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getUser_id() {
        return user_id;
    }

    public void setUser_id(Date user_id) {
        this.user_id = user_id;
    }
}