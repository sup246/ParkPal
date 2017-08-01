package com.psu.sweng500.team4.parkpal.Models;

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
    private int user_id;
    @com.google.gson.annotations.SerializedName("comment")
    private String review_comment;

    private String id;

    public String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public ParkRating() {
    }

    public ParkRating(long parkId, String username, int user_id, int rating, String comment) {
        this.park_id = parkId;
        this.username = username;
        this.rating = rating;
        this.user_id = user_id;
        this.review_comment = comment;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getReview_comment() {
        return review_comment;
    }

    public void setReview_comment(String review_comment) {
        this.review_comment = review_comment;
    }
}