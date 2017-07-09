package com.psu.sweng500.team4.parkpal.Queries;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.ParkNote;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.util.Date;

/**
 * Created by ShansMcB on 7/8/2017.
 */

public class ParkNotesInsertTask extends DBQueryTask {
    private ParkNote parkNote;

    private static final int SUCCESS = 200;

    public ParkNotesInsertTask(AsyncResponse delegate, long parkId, String username, String message, Date date){
        super();

        this.delegate = delegate;

        parkNote = new ParkNote(parkId, username, message, date);

    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            //Get a MobileServiceTable of the PARK_NOTES table from the AzureServiceAdapter
            final MobileServiceTable<ParkNote> table =
                    AzureServiceAdapter.getInstance().getClient().getTable("PARK_NOTES", ParkNote.class);

            table.insert(parkNote).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
}
