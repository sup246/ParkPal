package com.psu.sweng500.team4.parkpal.Queries;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.ParkNote;
import com.psu.sweng500.team4.parkpal.Models.ParkRating;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.util.HashMap;

/**
 * Created by brhoads on 7/30/2017.
 */

public class ParkNoteRatingQueryTask extends DBQueryTask {
    private long parkId;
    private MobileServiceList<ParkRating> ratingResults;
    private MobileServiceList<ParkNote> noteResults;
    private HashMap<String, MobileServiceList> map;

    public ParkNoteRatingQueryTask(AsyncResponse delegate, long parkId){
        super();

        this.delegate = delegate;
        this.parkId = parkId;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            //Get a MobileServiceTable of the PARK_RATINGS table from the AzureServiceAdapter
            final MobileServiceTable<ParkRating> ratingsTable =
                    AzureServiceAdapter.getInstance().getClient().getTable("PARK_RATINGS", ParkRating.class);

            ratingResults = ratingsTable
                    .where()
                    .field("park_id").eq(parkId)
                    .execute().get();

            //Get a MobileServiceTable of the PARK_RATINGS table from the AzureServiceAdapter
            final MobileServiceTable<ParkNote> notesTable =
                    AzureServiceAdapter.getInstance().getClient().getTable("PARK_NOTES", ParkNote.class);

            noteResults = notesTable
                    .where()
                    .field("park_id").eq(parkId)
                    .execute().get();

            map = new HashMap<String, MobileServiceList>();
            map.put("ratings", ratingResults);
            map.put("notes", noteResults);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}