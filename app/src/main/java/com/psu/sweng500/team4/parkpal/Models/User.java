package com.psu.sweng500.team4.parkpal.Models;

/**
 * Created by phuizar on 6/14/2017.
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

    public User () {}

     public User(String mUsername, String mEmail, String mBirthdate, String mFirstname,
                     String mLastname, String mPassword, String mZipCode){
        this.setUsername(mUsername);
        this.setEmail(mEmail);
        this.setBirthdate(mBirthdate);
        this.setFirstName(mFirstname);
        this.setLastName(mLastname);
        this.setPassword(mPassword);
        this.setZipCode(mZipCode);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}


