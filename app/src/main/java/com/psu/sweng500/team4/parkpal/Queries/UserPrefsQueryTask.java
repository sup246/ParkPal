package com.psu.sweng500.team4.parkpal.Queries;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.UserPrefs;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

/**
 * Created by ShansMcB on 7/8/2017.
 */

public class UserPrefsQueryTask extends DBQueryTask {
    private String username;
    private MobileServiceList<UserPrefs> queryResults;
    private UserPrefs userPrefs;

    public UserPrefsQueryTask(AsyncResponse delegate, String username){
        super();

        this.delegate = delegate;
        this.username = username;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            //Get a MobileServiceTable of the USER_PREFS table from the AzureServiceAdapter
            final MobileServiceTable<UserPrefs> table =
                    AzureServiceAdapter.getInstance().getClient().getTable("USER_PREFS", UserPrefs.class);

            queryResults = table
                    .where()
                    .field("username").eq(username)
                    .execute().get();

            if (queryResults == null || queryResults.isEmpty()) {
                return null;
            }

            userPrefs = queryResults.get(0);

            Log.d("INFO", "Result (" + username + "):\n" +
                    "Kids: " + userPrefs.isPrefs_kids() + "\n" +
                    "Dogs: " + userPrefs.isPrefs_dogs() + "\n" +
                    "Watersports: " + userPrefs.isPrefs_watersports() + "\n" +
                    "Camping: " + userPrefs.isPrefs_camping() + "\n" +
                    "Hiking: " + userPrefs.isPrefs_hiking() + "\n" +
                    "Forest: " + userPrefs.isPrefs_forest() + "\n" +
                    "Mountain: " + userPrefs.isPrefs_mountain() + "\n" +
                    "Desert: " + userPrefs.isPrefs_desert() + "\n" +
                    "Beach: " + userPrefs.isPrefs_beach() + "\n" );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPrefs;
    }
}
