package com.psu.sweng500.team4.parkpal.Queries;

import android.os.AsyncTask;

/**
 * Created by ShansMcB on 7/8/2017.
 */

public class DBQueryTask extends AsyncTask {
    public AsyncResponse delegate = null;

    public DBQueryTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    public DBQueryTask() { }

    @Override
    protected Object doInBackground(Object[] params) {
        // Run query here
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {

        delegate.processFinish(result);
    }
}
