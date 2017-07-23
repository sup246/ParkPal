package com.psu.sweng500.team4.parkpal.Models;

import java.util.Date;

/**
 * Created by sperkal on 7/8/2017.
 */

public class ParkAlert {

    @com.google.gson.annotations.SerializedName("park_id")
    private long alert_park_id;
    @com.google.gson.annotations.SerializedName("username")
    private String alert_username;
    @com.google.gson.annotations.SerializedName("message")
    private String alert_message;
    @com.google.gson.annotations.SerializedName("posted_on")
    private Date alert_date;

    private String id;
    public String getId() { return id; }
    public final void setId(String id) { this.id = id; }

    public ParkAlert() {}

    public ParkAlert(long parkId, String username, String message, Date date){
        this.alert_park_id = parkId;
        this.alert_username = username;
        this.alert_message = message;
        this.alert_date = date;
    }

    public String getUsername() {
        return alert_username;
    }

    public void setUsername(String username) {
        this.alert_username = username;
    }

    public String getAlertMessage() {
        return alert_message;
    }

    public void setAlertMessage(String message) {
        this.alert_message = message;
    }

    public long getParkId() {
        return alert_park_id;
    }

    public void setParkId(long parkId) {
        this.alert_park_id = parkId;
    }

    public Date getDate() { return alert_date; }

    public void setDate(Date date) {
        this.alert_date = date;
    }
}
