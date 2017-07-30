package com.psu.sweng500.team4.parkpal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Models.ParkAlert;
import com.psu.sweng500.team4.parkpal.Models.ParkNote;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Models.Weather.Weather;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
import com.psu.sweng500.team4.parkpal.Queries.ParkAlertsQueryTask;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;
import com.psu.sweng500.team4.parkpal.Services.WeatherService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class GMapFragment extends Fragment implements
        OnMapReadyCallback,
        GoogleMap.InfoWindowAdapter,
        GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private ArrayList<Location> mLocations;
    private LayoutInflater mInflater;
    Geocoder geocoder;
    LatLng mHomeLocation = new LatLng(39.9526, -75.1652); // Philly
    private User mCurrentUser;
    private TextView tvAlerts;
//    private ImageView weatherIcon;
//    private TextView curTemp;

    public GMapFragment() {}

    public static GMapFragment newInstance() {
        GMapFragment fragment = new GMapFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mCurrentUser = (User) getArguments().getSerializable("User");
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
        mMap.setOnInfoWindowClickListener(this);

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
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this.getActivity(), ParkDetails.class);
        intent.putExtra("Location", (Location)marker.getTag());
        intent.putExtra("User", mCurrentUser);
        startActivityForResult(intent, 999);
    }

    @Override
    public View getInfoContents(Marker marker)  {
        View v = getLayoutInflater(Bundle.EMPTY).inflate(R.layout.marker_info_layout, null);

        Location clickedLocation = (Location)marker.getTag();
        TextView tvLocation = (TextView) v.findViewById(R.id.tvLocation);
        TextView tvSnippet = (TextView) v.findViewById(R.id.tvSnippet);
        TextView tvAmenities = (TextView) v.findViewById(R.id.tvAmenities);
        TextView tvSeason = (TextView) v.findViewById(R.id.tvSeason);
        tvAlerts = (TextView) v.findViewById(R.id.tvAlerts);
//        weatherIcon = (ImageView) v.findViewById(R.id.markerWeatherIcon);
//        curTemp = (TextView) v.findViewById(R.id.markerCurrentTemp);

        //sets the variables created above to the current information
        tvLocation.setText(marker.getTitle());
        // Snippet returns city and state
        tvSnippet.setText(marker.getSnippet());
        //sets date open
        tvSeason.setText("Dates Open: " + clickedLocation.getDatesOpen());
        //TODO - Add icons to represent the various amenities
        tvAmenities.setText(clickedLocation.getAmenities());

//        WeatherService weatherService = null;
//        try {
//            weatherService = new WeatherService(this.getContext(), new AsyncResponse() {
//                @Override
//                public void processFinish(Object result) {
//                    final WeatherService weatherService = (WeatherService) result;
//                    final Weather weather = weatherService.getWeather();
//
//                    weatherIcon.setImageDrawable(weatherService.getWeatherIcon());
//                    curTemp.setText(weather.getPrettyTempstring());
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            weatherIcon.setImageDrawable(weatherService.getWeatherIcon());
//                            curTemp.setText(weather.getPrettyTempstring());
//                        }
//                    });
//                }
//            }).execute(clickedLocation.getLatitude(), clickedLocation.getLongitude()).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        //TODO - Get location alerts

        return v;
    }

    private void getLocationAlerts(long locId, final Marker marker){

        ParkAlertsQueryTask asyncQuery = new ParkAlertsQueryTask(new AsyncResponse(){

            @Override
            public void processFinish(Object result){
                for (Object o : (List<ParkAlert>) result)
                {
                    ParkAlert alert = (ParkAlert)o;
                    Log.d("PARK_ALERT", alert.getUsername() + " : " + alert.getAlertMessage());
                }

                final int alertCount = ((List<ParkAlert>) result).size();

                if(alertCount > 0){
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvAlerts.setText(alertCount + "");
                                Log.d("PARK_ALERT", alertCount + "");
                                marker.showInfoWindow();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, locId);

        asyncQuery.execute();

    }

    public void createLocationMarkers() {
        for (Location l: mLocations) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(l.getLatitude(),
                    l.getLongitude())).title(l.getName())
                    .snippet(l.getTown() + "," + l.getState())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.campsite_markergrsm)));
            marker.setTag(l);
        }
    }

    private Address AddressFinder(LatLng latLong){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;
       try {
           addresses = geocoder.getFromLocation(latLong.latitude, latLong.longitude, 1);
       }
       catch (IOException exception){
       }
        return addresses.get(0);
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


                    boolean more = true;
                    int totalrows = 0;
                    int skipnumber = 0;
                    int rows=0;
                    while(more) {
                        //Get a ListenableFuture<MobileServiceList<Location>> from the MobileServiceTable,
                        //iterable like a regular list
                        final MobileServiceList<Location> results  = table.orderBy("LOC_ID", QueryOrder.Ascending).skip(skipnumber).execute().get();
                        int count = results.size();
                        skipnumber += 50;
                        rows += count;
                        mLocations.addAll(results);

                        if (rows < skipnumber) {
                            more = false;
                        }

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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}