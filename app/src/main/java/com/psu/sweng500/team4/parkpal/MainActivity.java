package com.psu.sweng500.team4.parkpal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_ITEM = "opened_fragment";
    private BottomNavigationView mBottomNavigationView;
    ArrayList<Location> mLocations;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            Fragment fragment = null;

            if (id == R.id.navigation_map) {
                fragment = GMapFragment.newInstance(mLocations);
            } else if (id == R.id.navigation_search) {
                fragment = new SearchFragment();
            } else if (id == R.id.navigation_recommendations) {
                fragment = new RecommendationsFragment();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, fragment);
            transaction.commit();

            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new GMapFragment();
        transaction.replace(R.id.content, fragment);
        transaction.commit();

        try {
            //Initialization of the AzureServiceAdapter to make it usable in the app.
            AzureServiceAdapter.Initialize(this);
            Log.d("INFO", "AzureServiceAdapter initialized");

            mLocations = new ArrayList<Location>();
            pullLocationExample((GMapFragment) fragment);
        } catch (Exception e) {
            Log.e("ParkPal", "exception", e);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, 0);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.option_edit_profile:
                // TODO
                return true;
            case R.id.option_logout:
                clearLoginState();

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void pullLocationExample(final GMapFragment fragment) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //Get a MobileServiceTable of the LOCATIONS table from the AzureServiceAdapter
                final MobileServiceTable<Location> table =
                        AzureServiceAdapter.getInstance().getClient().getTable("LOCATIONS", Location.class);

                try {
                    //Get a ListenableFuture<MobileServiceList<Location>> from the MobileServiceTable,
                    //iterable like a regular list
                    final MobileServiceList<Location> results = table.where().execute().get();

                    mLocations.addAll(results);

                    for (int i = 0; i < 10; i++) {
                        Log.d("INFO", "Result : " + results.get(i).getName() +
                                " | " + results.get(i).getLatitude() +
                                " , " + results.get(i).getLongitude());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // If we're just starting up we are waiting on getting locations so
                            // set locations and after create the markers
                            fragment.setLocations(mLocations);
                            fragment.createLocationMarkers();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void clearLoginState(){
        SharedPreferences sharedpreferences = getSharedPreferences("PARKPAL", Context.MODE_PRIVATE);
        sharedpreferences.edit().clear();
    }
}
