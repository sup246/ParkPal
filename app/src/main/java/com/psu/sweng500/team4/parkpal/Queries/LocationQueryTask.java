package com.psu.sweng500.team4.parkpal.Queries;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

/**
 * Created by ShansMcB on 7/8/2017.
 */

public class LocationQueryTask extends DBQueryTask {
    private long loc_id;
    private MobileServiceList<Location> queryResults;

    public LocationQueryTask(AsyncResponse delegate, long id){
        super();

        this.delegate = delegate;
        this.loc_id = id;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            //Get a MobileServiceTable of the PARK_NOTES table from the AzureServiceAdapter
            final MobileServiceTable<Location> table =
                    AzureServiceAdapter.getInstance().getClient().getTable("LOCATIONS", Location.class);

            queryResults = table.where().field("LOC_ID").eq(loc_id).execute().get();

            // User not found
            if (queryResults == null || queryResults.isEmpty()) {
                return null;
            }

            Log.d("INFO", "Result : " + queryResults.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResults;
    }
}
