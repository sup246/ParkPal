package com.psu.sweng500.team4.parkpal.Models;

/**
 * Created by brhoads on 6/14/2017.
 */

public class User {

    @com.google.gson.annotations.SerializedName("username")
    private String username;
    @com.google.gson.annotations.SerializedName("email")
    private String email;
    @com.google.gson.annotations.SerializedName("birthdate")
    private String birthdate;
    @com.google.gson.annotations.SerializedName("firstname")
    private String firstname;
    @com.google.gson.annotations.SerializedName("lastname")
    private String lastname;
    @com.google.gson.annotations.SerializedName("password")
    private String password;
    @com.google.gson.annotations.SerializedName("zipCode")
    private String zipCode;

    private String id;
    public String getId() { return id; }
    public final void setId(String id) { this.id = id; }
}


