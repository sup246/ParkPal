package com.psu.sweng500.team4.parkpal.Models;

import java.io.Serializable;

/**
 * Created by sperkal on 7/8/2017.
 */

public class UserPrefs implements Serializable{

    @com.google.gson.annotations.SerializedName("username")
    private String prefs_username;
    @com.google.gson.annotations.SerializedName("kids")
    private boolean prefs_kids;
    @com.google.gson.annotations.SerializedName("dogs")
    private boolean prefs_dogs;
    @com.google.gson.annotations.SerializedName("watersports")
    private boolean prefs_watersports;
    @com.google.gson.annotations.SerializedName("hiking")
    private boolean prefs_hiking;
    @com.google.gson.annotations.SerializedName("camping")
    private boolean prefs_camping;
    @com.google.gson.annotations.SerializedName("forest")
    private boolean prefs_forest;
    @com.google.gson.annotations.SerializedName("mountain")
    private boolean prefs_mountain;
    @com.google.gson.annotations.SerializedName("desert")
    private boolean prefs_desert;
    @com.google.gson.annotations.SerializedName("beach")
    private boolean prefs_beach;

    private String id;
    public String getId() { return id; }
    public final void setId(String id) { this.id = id; }

    public UserPrefs() {}

    public UserPrefs(String username){
        this.prefs_username = username;
        this.prefs_kids = false;
        this.prefs_dogs = false;
        this.prefs_watersports = false;
        this.prefs_hiking = false;
        this.prefs_camping = false;
        this.prefs_forest = false;
        this.prefs_mountain = false;
        this.prefs_desert = false;
        this.prefs_beach = false;
    }

    public UserPrefs(String username,
                     boolean kids,
                     boolean dogs,
                     boolean watersports,
                     boolean hiking,
                     boolean camping,
                     boolean forest,
                     boolean mountain,
                     boolean desert,
                     boolean beach ){
        this.prefs_username = username;
        this.prefs_kids = kids;
        this.prefs_dogs = dogs;
        this.prefs_watersports = watersports;
        this.prefs_hiking = hiking;
        this.prefs_camping = camping;
        this.prefs_forest = forest;
        this.prefs_mountain = mountain;
        this.prefs_desert = desert;
        this.prefs_beach = beach;
    }

    public String getPrefs_username() {
        return prefs_username;
    }

    public void setPrefs_username(String prefs_username) {
        this.prefs_username = prefs_username;
    }

    public boolean isPrefs_kids() {
        return prefs_kids;
    }

    public void setPrefs_kids(boolean prefs_kids) {
        this.prefs_kids = prefs_kids;
    }

    public boolean isPrefs_dogs() {
        return prefs_dogs;
    }

    public void setPrefs_dogs(boolean prefs_dogs) {
        this.prefs_dogs = prefs_dogs;
    }

    public boolean isPrefs_watersports() {
        return prefs_watersports;
    }

    public void setPrefs_watersports(boolean prefs_watersports) {
        this.prefs_watersports = prefs_watersports;
    }

    public boolean isPrefs_hiking() {
        return prefs_hiking;
    }

    public void setPrefs_hiking(boolean prefs_hiking) {
        this.prefs_hiking = prefs_hiking;
    }

    public boolean isPrefs_camping() {
        return prefs_camping;
    }

    public void setPrefs_camping(boolean prefs_camping) {
        this.prefs_camping = prefs_camping;
    }

    public boolean isPrefs_forest() {
        return prefs_forest;
    }

    public void setPrefs_forest(boolean prefs_forest) {
        this.prefs_forest = prefs_forest;
    }

    public boolean isPrefs_mountain() {
        return prefs_mountain;
    }

    public void setPrefs_mountain(boolean prefs_mountain) {
        this.prefs_mountain = prefs_mountain;
    }

    public boolean isPrefs_desert() {
        return prefs_desert;
    }

    public void setPrefs_desert(boolean prefs_desert) {
        this.prefs_desert = prefs_desert;
    }

    public boolean isPrefs_beach() {
        return prefs_beach;
    }

    public void setPrefs_beach(boolean prefs_beach) {
        this.prefs_beach = prefs_beach;
    }
}
