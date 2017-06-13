package com.psu.sweng500.team4.parkpal.Services;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.TableQueryCallback;
import com.psu.sweng500.team4.parkpal.Models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brhoads on 6/13/2017.
 */

public class LocationService {

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    public ArrayList<Location> getAllLocations() {

        MobileServiceTable<Location> mLocationsTable = AzureServiceAdapter.getInstance().getClient().getTable("LOCATIONS", Location.class);
        final ArrayList<Location> locations = new ArrayList<>();

        try {
            mLocationsTable
                    .where()
                    .execute(new TableQueryCallback<Location>() {
                        public void onCompleted(List<Location> results, int count, Exception error, ServiceFilterResponse response) {
                            Log.d("INFO", "Locations returned : " + count);
                            locations.addAll(results);

                            for(int i = 0; i < 10; i++){
                                Log.d("INFO", "Location : " + " | " + locations.get(i).getLatitude());
                            }

                            for(int i = 0; i < 10; i++){
                                Log.d("INFO", "Result : " +  " | " + results.get(i).getLatitude());
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("ParkPal", "exception", e);
        }
        Log.d("INFO", "User table pulled");

        return locations;
    }
}
