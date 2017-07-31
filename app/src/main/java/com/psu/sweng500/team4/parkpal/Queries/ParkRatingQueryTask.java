package com.psu.sweng500.team4.parkpal.Queries;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.ParkRating;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

/**
 * Created by brhoads on 7/30/2017.
 */

public class ParkRatingQueryTask extends DBQueryTask {
    private long parkId;
    private MobileServiceList<ParkRating> queryResults;

    public ParkRatingQueryTask(AsyncResponse delegate, long parkId){
        super();

        this.delegate = delegate;
        this.parkId = parkId;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            //Get a MobileServiceTable of the PARK_RATINGS table from the AzureServiceAdapter
            final MobileServiceTable<ParkRating> table =
                    AzureServiceAdapter.getInstance().getClient().getTable("PARK_RATINGS", ParkRating.class);

            queryResults = table
                    .where()
                    .field("park_id").eq(parkId)
                    .execute().get();

            for (int i = 0; i < queryResults.size() - 1; i++) {
                Log.d("INFO", "Result (" + parkId + ")" + queryResults.get(i).getUsername() +
                        " | " + queryResults.get(i).getRating());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResults;
    }
}