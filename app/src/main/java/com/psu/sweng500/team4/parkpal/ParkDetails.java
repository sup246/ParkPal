package com.psu.sweng500.team4.parkpal;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Models.ParkAlert;
import com.psu.sweng500.team4.parkpal.Models.ParkNote;
import com.psu.sweng500.team4.parkpal.Models.ParkRating;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Models.Weather.Weather;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
import com.psu.sweng500.team4.parkpal.Queries.ParkAlertsQueryTask;
import com.psu.sweng500.team4.parkpal.Queries.ParkMyRatingQueryTask;
import com.psu.sweng500.team4.parkpal.Queries.ParkNoteRatingQueryTask;
import com.psu.sweng500.team4.parkpal.Services.WeatherService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class ParkDetails extends AppCompatActivity {
    private Location mLocation;
    private User mCurrentUser;
    private ListView alertListV;
    private Button reviewButton;

    private ArrayList<ParkNote> parkNotes;
    private ArrayList<ParkRating> parkRatings;
    private ArrayList<ParkAlert> parkAlerts;

    ArrayList<String> alertListItems;
    ArrayAdapter<String> alertAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_details);

        mLocation = (Location) getIntent().getSerializableExtra("Location");
        mCurrentUser = (User) getIntent().getSerializableExtra("User");

        getListInfo(mLocation.getLocId());

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
                Intent intent = new Intent(ParkDetails.this, AddParkAlertActivity.class);
                intent.putExtra("Location", mLocation);
                intent.putExtra("User", mCurrentUser);
                startActivityForResult(intent, 999);
            }
        });

        Button mAddReview = (Button) findViewById(R.id.addReview);

        mAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent= new Intent(ParkDetails.this, AddParkReviewActivity.class);
                intent.putExtra("Location", mLocation);
                intent.putExtra("User", mCurrentUser);
                startActivityForResult(intent, 999);

            }
        });

       // Location clickedLocation = (Location)marker.getTag();
        TextView tvLocation = (TextView) this.findViewById(R.id.tvLocation);
        TextView tvSnippet = (TextView) this.findViewById(R.id.tvSnippet);
        TextView tvAddress = (TextView) this.findViewById(R.id.tvAddress);
        TextView tvAmenities = (TextView) this.findViewById(R.id.tvAmenities);
        TextView tvSeason = (TextView) this.findViewById(R.id.tvSeason);
        TextView tvPhone = (TextView) this.findViewById(R.id.tvPhone);
        reviewButton = (Button) this.findViewById(R.id.addReview);

        //get marker current location
        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        //get street address from geocoder
       // Address addr = AddressFinder(latLng);

        //sets the variables created above to the current information
        tvLocation.setText(mLocation.getName());
        // Snippet returns city and state
        tvSnippet.setText(mLocation.getTown() + mLocation.getState());
        //sets dates open
        tvSeason.setText("Dates Open: " + mLocation.getDatesOpen());
        //TODO - Add icons to represent the various amenities
        tvAmenities.setText(mLocation.getAmenities());

        final ImageView iv = (ImageView) this.findViewById(R.id.ivWeatherIcon);
        final TextView tvCurrentTemp = (TextView) this.findViewById(R.id.tvCurrentTemp);

        WeatherService weatherService = new WeatherService(this, new AsyncResponse() {
            @Override
            public void processFinish(Object result) {
                WeatherService weatherService = (WeatherService) result;
                Weather weather = weatherService.getWeather();

                iv.setImageDrawable(weatherService.getWeatherIcon());

                if (weather != null) {
                    tvCurrentTemp.setText(weather.getPrettyTempstring());
                }
            }
        });

        weatherService.execute(mLocation.getLatitude(), mLocation.getLongitude());

        //set Phone Number from database
        if (mLocation.getPhone() == ""){
            tvPhone.setText("Phone Number N/A");
        }else{
            tvPhone.setText(mLocation.getPhone());
        }

        //sets address from the Lat/Lng from geocoder conversion

/*
        if (addr == null || addr.getAddressLine(0) == " ") {
            tvAddress.setText("Address N/A");
        }else{
            tvAddress.setText(addr.getAddressLine(0));
        }
*/

        alertListV = (ListView) this.findViewById(R.id.parkAlerts);

        alertListItems = new ArrayList<String>();
        alertAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                alertListItems);
        alertListV.setAdapter(alertAdapter);

        getAlerts(mLocation.getLocId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Refresh things after making an update
        getAlerts(mLocation.getLocId());
        getListInfo(mLocation.getLocId());
    }

    private void addAlertToList(String alert){

        alertListItems.add(alert);
        alertAdapter.notifyDataSetChanged();

    }

    private void getAlerts(long locId){

        ParkAlertsQueryTask asyncQuery = new ParkAlertsQueryTask(new AsyncResponse(){
            @Override
            public void processFinish(Object result){
                if (result == null) {
                    parkAlerts = new ArrayList<ParkAlert>();
                }
                else {
                    parkAlerts = (ArrayList<ParkAlert>)result;
                }

                Collections.sort(parkAlerts, new Comparator<ParkAlert>(){
                    public int compare(ParkAlert obj1, ParkAlert obj2) {
                        // ## Descending order
                        return obj2.getDate().compareTo(obj1.getDate());
                    }
                });

                for(ParkAlert alert : parkAlerts){
                    if (alert.getAlertMessage() != null && !alert.getAlertMessage().isEmpty()) {
                        addAlertToList(alert.getDate() + ": " + alert.getAlertMessage());
                    }
                }
            }
        }, locId);

        asyncQuery.execute();
    }

    private void getListInfo(long locId)  {
        ParkNoteRatingQueryTask getParkNotesReviews = new ParkNoteRatingQueryTask(new AsyncResponse(){

            @Override
            public void processFinish(Object result){
                if (result == null){
                    parkRatings = new ArrayList<ParkRating>();
                }
                else {
                    HashMap map = (HashMap<String, MobileServiceList>) result;
                    parkRatings = (ArrayList<ParkRating>) map.get("ratings");
                    parkNotes = (ArrayList<ParkNote>) map.get("notes");
                }

                initExpandableList();
                initRatingStars();
            }
        }, locId);

        getParkNotesReviews.execute();

        ParkMyRatingQueryTask haveIRated = new ParkMyRatingQueryTask(new AsyncResponse(){

            @Override
            public void processFinish(Object result) {
                if ((boolean) result == true) {
                    Drawable img = getResources().getDrawable(android.R.drawable.ic_menu_edit);
                    reviewButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
                }
            }
        }, locId, mCurrentUser.getUsername());

        haveIRated.execute();
    }

    private void initRatingStars() {
        int ratingsCount = parkRatings.size();
        int ratingsTotal = 0;

        for (ParkRating rating : parkRatings) {
            ratingsTotal += rating.getRating();
        }

        RatingBar ratingBar = (RatingBar) this.findViewById(R.id.ratingBar);
        if (ratingBar != null) {
            if (ratingsCount > 0) {
                ratingBar.setRating(ratingsTotal / ratingsCount);
            }
            else {
                ratingBar.setRating(0);
            }
        }

        TextView tvRatNum = (TextView) this.findViewById(R.id.tvRatNum);
        if (tvRatNum != null) {
            tvRatNum.setText("(" + ratingsCount + ")");
        }
    }

    private void initExpandableList() {
        final ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.parkNotes);
        ExpandableListAdapter expandableListAdapter;
        final List<String> expandableListTitle = new ArrayList<String>();
        final HashMap<String, List<Object>> expandableListDetail = new HashMap<String, List<Object>>();

        Object reviews = parkRatings;
        Object notes = parkNotes;
        expandableListDetail.put("Reviews", (List<Object>) reviews);
        expandableListDetail.put("Comments", (List<Object>) notes);

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

    private Address AddressFinder(LatLng latLong){
//        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            addresses = geocoder.getFromLocation(latLong.latitude, latLong.longitude, 1);
//        }
//        catch (IOException exception){
//            Log.e("ParkPal", "exception", exception);
//        }
//        return addresses.get(0);
        return null;
    }
}
