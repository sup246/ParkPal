package com.psu.sweng500.team4.parkpal.Queries;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.ParkNote;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.util.List;

/**
 * Created by ShansMcB on 7/8/2017.
 */

public class ParkNotesQueryTask extends DBQueryTask {
    private long parkId;
    private MobileServiceList<ParkNote> queryResults;

    public ParkNotesQueryTask(AsyncResponse delegate, long parkId){
        super();

        this.delegate = delegate;
        this.parkId = parkId;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            //Get a MobileServiceTable of the PARK_NOTES table from the AzureServiceAdapter
            final MobileServiceTable<ParkNote> table =
                    AzureServiceAdapter.getInstance().getClient().getTable("PARK_NOTES", ParkNote.class);

            queryResults = table
                    .where()
                    .field("park_id").eq(parkId)
                    .execute().get();

            for (int i = 0; i < queryResults.size() - 1; i++) {
                Log.d("INFO", "Result (" + parkId + ")" + queryResults.get(i).getUsername() +
                        " | " + queryResults.get(i).getNote_message());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResults;
    }
}
