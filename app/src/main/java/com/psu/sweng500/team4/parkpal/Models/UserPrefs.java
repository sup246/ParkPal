package com.psu.sweng500.team4.parkpal.Models;

import java.io.Serializable;

/**
 * Created by sperkal on 7/8/2017.
 */

public class UserPrefs implements Serializable{

    @com.google.gson.annotations.SerializedName("username")
    private String prefs_username;
//    @com.google.gson.annotations.SerializedName("kids")
//    private int prefs_kids;
//    @com.google.gson.annotations.SerializedName("dogs")
//    private int prefs_dogs;
//    @com.google.gson.annotations.SerializedName("watersports")
//    private int prefs_watersports;
//    @com.google.gson.annotations.SerializedName("hiking")
//    private int prefs_hiking;
//    @com.google.gson.annotations.SerializedName("camping")
//    private int prefs_camping;
//    @com.google.gson.annotations.SerializedName("forest")
//    private int prefs_forest;
//    @com.google.gson.annotations.SerializedName("mountain")
//    private int prefs_mountain;
//    @com.google.gson.annotations.SerializedName("desert")
//    private int prefs_desert;
//    @com.google.gson.annotations.SerializedName("beach")
//    private int prefs_beach;

    private String id;
    public String getId() { return id; }
    public final void setId(String id) { this.id = id; }

    public UserPrefs() {}

    public UserPrefs(String username){
        this.prefs_username = username;
//        this.prefs_kids = 0;
//        this.prefs_dogs = 0;
//        this.prefs_watersports = 0;
//        this.prefs_hiking = 0;
//        this.prefs_camping = 0;
//        this.prefs_forest = 0;
//        this.prefs_mountain = 0;
//        this.prefs_desert = 0;
//        this.prefs_beach = 0;
    }

//    public UserPrefs(String username,
//                     int kids,
//                     int dogs,
//                     int watersports,
//                     int hiking,
//                     int camping,
//                     int forest,
//                     int mountain,
//                     int desert,
//                     int beach ){
//        this.prefs_username = username;
//        this.prefs_kids = kids;
//        this.prefs_dogs = dogs;
//        this.prefs_watersports = watersports;
//        this.prefs_hiking = hiking;
//        this.prefs_camping = camping;
//        this.prefs_forest = forest;
//        this.prefs_mountain = mountain;
//        this.prefs_desert = desert;
//        this.prefs_beach = beach;
//    }

    public String getPrefs_username() {
        return prefs_username;
    }

    public void setPrefs_username(String prefs_username) {
        this.prefs_username = prefs_username;
    }

//    public int isPrefs_kids() {
//        return prefs_kids;
//    }
//
//    public void setPrefs_kids(int prefs_kids) {
//        this.prefs_kids = prefs_kids;
//    }
//
//    public int isPrefs_dogs() {
//        return prefs_dogs;
//    }
//
//    public void setPrefs_dogs(int prefs_dogs) {
//        this.prefs_dogs = prefs_dogs;
//    }
//
//    public int isPrefs_watersports() {
//        return prefs_watersports;
//    }
//
//    public void setPrefs_watersports(int prefs_watersports) {
//        this.prefs_watersports = prefs_watersports;
//    }
//
//    public int isPrefs_hiking() {
//        return prefs_hiking;
//    }
//
//    public void setPrefs_hiking(int prefs_hiking) {
//        this.prefs_hiking = prefs_hiking;
//    }
//
//    public int isPrefs_camping() {
//        return prefs_camping;
//    }
//
//    public void setPrefs_camping(int prefs_camping) {
//        this.prefs_camping = prefs_camping;
//    }
//
//    public int isPrefs_forest() {
//        return prefs_forest;
//    }
//
//    public void setPrefs_forest(int prefs_forest) {
//        this.prefs_forest = prefs_forest;
//    }
//
//    public int isPrefs_mountain() {
//        return prefs_mountain;
//    }
//
//    public void setPrefs_mountain(int prefs_mountain) {
//        this.prefs_mountain = prefs_mountain;
//    }
//
//    public int isPrefs_desert() {
//        return prefs_desert;
//    }
//
//    public void setPrefs_desert(int prefs_desert) {
//        this.prefs_desert = prefs_desert;
//    }
//
//    public int isPrefs_beach() {
//        return prefs_beach;
//    }
//
//    public void setPrefs_beach(int prefs_beach) {
//        this.prefs_beach = prefs_beach;
//    }
}
