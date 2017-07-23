package com.psu.sweng500.team4.parkpal.Queries;

import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.ParkAlert;
import com.psu.sweng500.team4.parkpal.Models.ParkNote;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.util.Date;

/**
 * Created by ShansMcB on 7/8/2017.
 */

public class ParkAlertsInsertTask extends DBQueryTask {
    private ParkAlert parkAlert;

    private static final int SUCCESS = 200;

    public ParkAlertsInsertTask(AsyncResponse delegate, long parkId, String username, String message, Date date){
        super();

        this.delegate = delegate;

        parkAlert = new ParkAlert(parkId, username, message, date);

    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            //Get a MobileServiceTable of the PARK_NOTES table from the AzureServiceAdapter
            final MobileServiceTable<ParkAlert> table =
                    AzureServiceAdapter.getInstance().getClient().getTable("PARK_ALERTS", ParkAlert.class);

            table.insert(parkAlert).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
}
