package com.psu.sweng500.team4.parkpal.Queries;

import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.UserPrefs;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.util.Date;

/**
 * Created by ShansMcB on 7/8/2017.
 */

public class UserPrefsInsertTask extends DBQueryTask {
    private UserPrefs userPrefs;

    private static final int SUCCESS = 200;

    public UserPrefsInsertTask(AsyncResponse delegate, UserPrefs userPrefs){
        super();

        this.delegate = delegate;
        this.userPrefs = userPrefs;

    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            //Get a MobileServiceTable of the PARK_NOTES table from the AzureServiceAdapter
            final MobileServiceTable<UserPrefs> table =
                    AzureServiceAdapter.getInstance().getClient().getTable("USER_PREFS", UserPrefs.class);

            table.insert(userPrefs).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
}
