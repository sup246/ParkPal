package com.psu.sweng500.team4.parkpal;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.google.android.gms.maps.model.LatLng;
import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Models.ParkNote;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
import com.psu.sweng500.team4.parkpal.Queries.ParkNotesQueryTask;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;
import com.psu.sweng500.team4.parkpal.Services.WeatherService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ParkDetails extends AppCompatActivity {
    private Location mLocation;
    private User mCurrentUser;
    private LayoutInflater layoutInflator;
    private RelativeLayout layout;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_details);

        mLocation = (Location) getIntent().getSerializableExtra("Location");
        getListInfo(mLocation.getLocId());
        mCurrentUser = (User) getIntent().getSerializableExtra("User");
        //WeatherService mWeatherService = new WeatherService(this);

        Button mAddNoteButton = (Button) findViewById(R.id.addNote);
        mAddNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParkDetails.this, AddParkNoteActivity.class);
                intent.putExtra("Location", mLocation);
                intent.putExtra("User", mCurrentUser);
                startActivityForResult(intent, 999);
            }
        });

        Button mAddAlertButton = (Button) findViewById(R.id.addAlert);
        mAddAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addAlert();
            }
        });

        Button mAddReview = (Button) findViewById(R.id.addReview);
        layout = (RelativeLayout)findViewById(R.id.rLayout);

        mAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflator = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup userReview = (ViewGroup) layoutInflator.inflate(R.layout.park_review_layout, null);
                popupWindow = new PopupWindow(userReview, 1000, 500, true);
                popupWindow.showAtLocation(layout, Gravity.NO_GRAVITY, 20, 500);

                //close window - TODO - Add Save button to save back to the database
                userReview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });
        //PopupWindow mUserReview =

       // Location clickedLocation = (Location)marker.getTag();
        TextView tvLocation = (TextView) this.findViewById(R.id.tvLocation);
        TextView tvSnippet = (TextView) this.findViewById(R.id.tvSnippet);
        TextView tvAddress = (TextView) this.findViewById(R.id.tvAddress);
        TextView tvAmenities = (TextView) this.findViewById(R.id.tvAmenities);
        TextView tvSeason = (TextView) this.findViewById(R.id.tvSeason);
        TextView tvPhone = (TextView) this.findViewById(R.id.tvPhone);
        //TextView tvCurrentTemp = (TextView) this.findViewById(R.id.tvCurrentTemp);

        //get marker current location
        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        //get street address from geocoder
        Address addr = AddressFinder(latLng);

        //sets the variables created above to the current information
        tvLocation.setText(mLocation.getName());
        // Snippet returns city and state
        tvSnippet.setText(mLocation.getTown() + mLocation.getState());
        //sets dates open
        tvSeason.setText("Dates Open: " + mLocation.getDatesOpen());
        //TODO - Add icons to represent the various amenities
        tvAmenities.setText(mLocation.getAmenities());
        //TODO - Add weather info
       // mWeatherService.execute(mLocation.getLatitude(), mLocation.getLongitude());

        //set Phone Number from database
        if (mLocation.getPhone() == ""){
            tvPhone.setText("Phone Number N/A");
        }else{
            tvPhone.setText(mLocation.getPhone());
        }

        //sets address from the Lat/Lng from geocoder conversion
        if (addr.getAddressLine(0) == " ") {
            tvAddress.setText("Address N/A");
        }else{
            tvAddress.setText(addr.getAddressLine(0));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Refresh the park notes after one has been inserted
        getListInfo(mLocation.getLocId());
    }

    private void getListInfo(long locId)  {

        try {
            //Initialization of the AzureServiceAdapter to make it usable in the app.
            AzureServiceAdapter.Initialize(getBaseContext());
            Log.d("INFO", "AzureServiceAdapter initialized");

        } catch (Exception e) {
            Log.e("ParkPal", "exception", e);
        }

        // Get park notes
        ParkNotesQueryTask asyncQuery = new ParkNotesQueryTask(new AsyncResponse(){

            @Override
            public void processFinish(Object result){
                final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.parkNotes);
                ExpandableListAdapter expandableListAdapter;
                final List<String> expandableListTitle = new ArrayList<String>();
                final HashMap<String, List<Object>> expandableListDetail = new HashMap<String, List<Object>>();

                // If no results just show an empty list
                if (result == null){
                    result = new ArrayList<ParkNote>();
                }

                expandableListDetail.put("Park Notes", (List<Object>) result);

                expandableListTitle.addAll(expandableListDetail.keySet());

                expandableListAdapter = new ExpandableListAdapter(getBaseContext(), expandableListTitle, expandableListDetail);
                expandableListView.setAdapter(expandableListAdapter);
                expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    @Override
                    public void onGroupExpand(int groupPosition) { }
                });

                expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                    @Override
                    public void onGroupCollapse(int groupPosition) { }
                });

                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) { return false; }
                });
            }
        }, locId);

        asyncQuery.execute();
    }

    private Address AddressFinder(LatLng latLong){
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLong.latitude, latLong.longitude, 1);
        }
        catch (IOException exception){
        }
        return addresses.get(0);
    }
}
