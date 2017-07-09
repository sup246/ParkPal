package com.psu.sweng500.team4.parkpal.Queries;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

/**
 * Created by ShansMcB on 7/8/2017.
 */

public class UserQueryTask extends DBQueryTask {
    private String email;
    private MobileServiceList<User> queryResults;
    private User currentUser;

    public UserQueryTask(AsyncResponse delegate, String email){
        super();

        this.delegate = delegate;
        this.email = email;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            //Get a MobileServiceTable of the PARK_NOTES table from the AzureServiceAdapter
            final MobileServiceTable<User> table =
                    AzureServiceAdapter.getInstance().getClient().getTable("USERS", User.class);

            queryResults = table
                    .where()
                    .field("email").eq(email)
                    .execute().get();

            // User not found
            if (queryResults.isEmpty()) {
                return null;
            }

            currentUser = queryResults.get(0);

            String prettyName = currentUser.getFirstName() + " " + currentUser.getLastName();
            //Log.d("INFO", "Result : " + prettyName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentUser;
    }
}
