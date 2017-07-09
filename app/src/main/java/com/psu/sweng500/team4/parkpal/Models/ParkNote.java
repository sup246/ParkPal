package com.psu.sweng500.team4.parkpal.Models;

import java.util.Date;

/**
 * Created by sperkal on 7/8/2017.
 */

public class ParkNote {

    @com.google.gson.annotations.SerializedName("park_id")
    private long note_park_id;
    @com.google.gson.annotations.SerializedName("username")
    private String note_username;
    @com.google.gson.annotations.SerializedName("message")
    private String note_message;
    @com.google.gson.annotations.SerializedName("posted_on")
    private Date note_date;

    private String id;
    public String getId() { return id; }
    public final void setId(String id) { this.id = id; }

    public ParkNote () {}

    public ParkNote(long parkId, String username, String message, Date date){
        this.note_park_id = parkId;
        this.note_username = username;
        this.note_message = message;
        this.note_date = date;
    }

    public String getUsername() {
        return note_username;
    }

    public void setUsername(String username) {
        this.note_username = username;
    }

    public String getNote_message() {
        return note_message;
    }

    public void setNote_message(String message) {
        this.note_message = message;
    }

    public long getParkId() {
        return note_park_id;
    }

    public void setParkId(long parkId) {
        this.note_park_id = parkId;
    }

    public Date getDate() { return note_date; }

    public void setDate(Date date) {
        this.note_date = date;
    }
}
