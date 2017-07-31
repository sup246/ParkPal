package com.psu.sweng500.team4.parkpal.Queries;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.ParkNote;
import com.psu.sweng500.team4.parkpal.Models.ParkRating;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.util.HashMap;

/**
 * Created by brhoads on 7/30/2017.
 */

public class ParkMyRatingQueryTask extends DBQueryTask {
    private long parkId;
    private String username;
    private MobileServiceList<ParkRating> ratingResults;

    public ParkMyRatingQueryTask(AsyncResponse delegate, long parkId, String username){
        super();

        this.delegate = delegate;
        this.parkId = parkId;
        this.username = username;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            //Get a MobileServiceTable of the PARK_RATINGS table from the AzureServiceAdapter
            final MobileServiceTable<ParkRating> table =
                    AzureServiceAdapter.getInstance().getClient().getTable("PARK_RATINGS", ParkRating.class);

            ratingResults = table
                    .where()
                    .field("username").eq(username)
                    .and()
                    .field("park_id").eq(parkId)
                    .execute()
                    .get();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ratingResults.size() > 0;
    }
}