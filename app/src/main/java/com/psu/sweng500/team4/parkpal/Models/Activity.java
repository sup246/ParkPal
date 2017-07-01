package com.psu.sweng500.team4.parkpal.Models;

/**
 * Created by brhoads on 7/1/2017.
 * 
 * Usage:
 *      final MobileServiceTable<Activity> table =
        AzureServiceAdapter.getInstance().getClient().getTable("ACTIVITIES", Activity.class);
 */

public class Activity {

    //String id is required by Azure Mobile Apps services.  Do not remove.
    private String id;
    public String getId() { return id; }
    public final void setId(String id) { this.id = id; }

    @com.google.gson.annotations.SerializedName("ACTIVITY_ID")
    private long activityId;

    @com.google.gson.annotations.SerializedName("LOC_ID")
    private long locId;

    @com.google.gson.annotations.SerializedName("ACTIVITY_NAME")
    private String activityName;

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public long getLocId() {
        return locId;
    }

    public void setLocId(long locId) {
        this.locId = locId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
