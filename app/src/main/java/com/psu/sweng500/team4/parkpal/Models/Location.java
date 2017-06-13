package com.psu.sweng500.team4.parkpal.Models;

/**
 * Created by brhoads on 6/10/2017.
 */

public class Location {

    @com.google.gson.annotations.SerializedName("LOC_ID")
    private long locId;
    @com.google.gson.annotations.SerializedName("LAT")
    private float latitude;
    @com.google.gson.annotations.SerializedName("LNG")
    private float longitude;
    @com.google.gson.annotations.SerializedName("CAMPGROUND_CODE")
    private String campgroundCode;
    @com.google.gson.annotations.SerializedName("NAME")
    private String name;
    @com.google.gson.annotations.SerializedName("LOC_TYPE")
    private String locType;
    @com.google.gson.annotations.SerializedName("PHONE")
    private String phone;
    @com.google.gson.annotations.SerializedName("DATES_OPEN")
    private String datesOpen;
    @com.google.gson.annotations.SerializedName("ELEVATION")
    private int elevation;
    @com.google.gson.annotations.SerializedName("AMENITIES")
    private String amenities;
    @com.google.gson.annotations.SerializedName("STATE")
    private String state;
    @com.google.gson.annotations.SerializedName("TOWN")
    private String town;

    private String id;
    public String getId() { return id; }
    public final void setId(String id) { this.id = id; }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCampgroundCode() {
        return campgroundCode;
    }

    public void setCampgroundCode(String campgroundCode) {
        this.campgroundCode = campgroundCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDatesOpen() {
        return datesOpen;
    }

    public void setDatesOpen(String datesOpen) {
        this.datesOpen = datesOpen;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public long getLocId() {
        return locId;
    }

    public void setLocId(long locId) {
        this.locId = locId;
    }
}
