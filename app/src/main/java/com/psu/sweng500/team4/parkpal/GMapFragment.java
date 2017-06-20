package com.psu.sweng500.team4.parkpal;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;
    private ArrayList<Location> mLocations;
    private LayoutInflater mInflater;
    LatLng mHomeLocation = new LatLng(39.9526, -75.1652); // Philly

    public GMapFragment() {}

    public static GMapFragment newInstance() {
        GMapFragment fragment = new GMapFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        return mInflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Prompt the user once explanation has been shown
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    99);
            mMap.setMyLocationEnabled(true);
        } else {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener()
        {
            @Override
            public boolean onMyLocationButtonClick()
            {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mHomeLocation, 7));
                return true;
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mHomeLocation, 7));

        if (mLocations == null) {
            mLocations = new ArrayList<Location>();

            pullLocationExample();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void test() {}

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup=mInflater.inflate(R.layout.map_popup, null);

        TextView tv=(TextView)popup.findViewById(R.id.title);

        tv.setText(marker.getTitle());
        tv=(TextView)popup.findViewById(R.id.snippet);
        tv.setText(marker.getSnippet());

        return(popup);
    }

    public void createLocationMarkers() {
        for (Location l: mLocations) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(l.getLatitude(),
                    l.getLongitude())).title(l.getName())
                    .snippet(l.getPhone()));
        }
    }

    private void pullLocationExample() {

        try {
            //Initialization of the AzureServiceAdapter to make it usable in the app.
            AzureServiceAdapter.Initialize(this.getContext());
            Log.d("INFO", "AzureServiceAdapter initialized");

        } catch (Exception e) {
            Log.e("ParkPal", "exception", e);
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    //Get a MobileServiceTable of the LOCATIONS table from the AzureServiceAdapter
                    final MobileServiceTable<Location> table =
                        AzureServiceAdapter.getInstance().getClient().getTable("LOCATIONS", Location.class);


                    //Get a ListenableFuture<MobileServiceList<Location>> from the MobileServiceTable,
                    //iterable like a regular list
                    final MobileServiceList<Location> results = table.where().execute().get();

                    mLocations.addAll(results);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // If we're just starting up we are waiting on getting locations so
                            // set locations and after create the markers
                            createLocationMarkers();
                        }
                    });

                    for (int i = 0; i < 10; i++) {
                        Log.d("INFO", "Result : " + results.get(i).getName() +
                                " | " + results.get(i).getLatitude() +
                                " , " + results.get(i).getLongitude());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}