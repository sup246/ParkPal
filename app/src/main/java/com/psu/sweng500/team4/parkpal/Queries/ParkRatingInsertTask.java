package com.psu.sweng500.team4.parkpal.Queries;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.ParkRating;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.util.Date;

/**
 * Created by brhoads on 7/30/2017.
 */

public class ParkRatingInsertTask extends DBQueryTask {
    private ParkRating parkRating;

    private static final int SUCCESS = 200;

    public ParkRatingInsertTask(AsyncResponse delegate, long parkId, String username, int userId, int rating, String comment){
        super();

        this.delegate = delegate;
        parkRating = new ParkRating(parkId, username, userId, rating, comment);

    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            //Get a MobileServiceTable of the PARK_ratings table from the AzureServiceAdapter
            final MobileServiceTable<ParkRating> table =
                    AzureServiceAdapter.getInstance().getClient().getTable("PARK_RATINGS", ParkRating.class);

            MobileServiceList<ParkRating> ratingResults = table
                    .where()
                    .field("username").eq(parkRating.getUsername())
                    .and()
                    .field("park_id").eq(parkRating.getPark_id())
                    .execute()
                    .get();

            if (ratingResults.size() > 0) { // update
                ParkRating toUpdate = ratingResults.get(0);
                toUpdate.setRating(parkRating.getRating());
                toUpdate.setReview_comment(parkRating.getReview_comment());
                table.update(toUpdate).get();
            }
            else { // insert
                table.insert(parkRating).get();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
}