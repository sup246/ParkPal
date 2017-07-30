package com.psu.sweng500.team4.parkpal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.psu.sweng500.team4.parkpal.Models.Activity;
import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Models.ParkAlert;
import com.psu.sweng500.team4.parkpal.Models.ParkNote;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
import com.psu.sweng500.team4.parkpal.Queries.ParkAlertsQueryTask;
import com.psu.sweng500.team4.parkpal.Queries.ParkNotesQueryTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ParkDetails extends AppCompatActivity {
    private Location mLocation;
    private User mCurrentUser;
    private LayoutInflater layoutInflator;
    private RelativeLayout layout;
    private ListView alertListV;

    private ArrayList<ParkAlert> parkAlerts;
    private int currImage=0;
    Context context=this.getApplicationContext();
    private String userChoosenTask="";

    ArrayList<String> alertListItems;
    ArrayAdapter<String> alertAdapter;

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
                Intent intent = new Intent(ParkDetails.this, AddParkAlertActivity.class);
                intent.putExtra("Location", mLocation);
                intent.putExtra("User", mCurrentUser);
                startActivityForResult(intent, 999);
            }
        });

        Button mAddReview = (Button) findViewById(R.id.addReview);
        layout = (RelativeLayout)findViewById(R.id.rLayout);

        mAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent= new Intent(ParkDetails.this, AddParkReviewActivity.class);
                intent.putExtra("Location", mLocation);
                intent.putExtra("User", mCurrentUser);
                startActivityForResult(intent, 666);

            };
        });

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
       // Address addr = AddressFinder(latLng);

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


        final String images[] = {"https://www.nps.gov/common/uploads/grid_builder/pwr/crop1_1/324D8FBC-1DD8-B71B-0BBE6CB1E8B2D003.jpg?width=640&quality=90&mode=crop",
                           "http://www.mesaaz.gov/Home/ShowImage?id=12832&amp;t=636131724775470000",
                           "https://cityofwinterpark.org/wp-content/uploads/2014/05/DinkyDockPark_Field.jpg",
                           "https://www.arkansasstateparks.com/!userfiles/subheads/asp_int_sub-img_parks_davidsonville.jpg",
                           "http://www.vhw.org/images/pages/N92/park.jpg"};


        /////////////////////////////////////////////////////////
        ImageView mPhotoImage = (ImageView) findViewById(R.id.imageButton);
        mPhotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        ImageView parkView = (ImageView) findViewById(R.id.parkView);
        Picasso.with(this.getApplicationContext()).load(images[currImage]).into(parkView);
        /////////////////////////////////////////////////////////


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Refresh the park notes after one has been inserted

        if (requestCode == 666)
        {
            RelativeLayout layout = (RelativeLayout)findViewById(R.id.ratingId);
            layout.setVisibility(View.VISIBLE);

            String comment = data.getStringExtra("Comment");
            float stars = data.getFloatExtra("Stars", 0);

            TextView userName = (TextView)findViewById(R.id.tvProfileName);
            MainActivity ma = new MainActivity();
            userName.setText(mCurrentUser.getUsername());

            RatingBar userRating = (RatingBar)findViewById(R.id.userRatingBar);
            userRating.setRating(stars);

            TextView userComment = (TextView)findViewById(R.id.tvRatingComment);
            userComment.setText(comment.toString());

            Button reviewButton = (Button)findViewById(R.id.addReview);
            reviewButton.setText("EDIT REVIEW");

        }else
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


     /*   try {
            //Initialization of the AzureServiceAdapter to make it usable in the app.
            AzureServiceAdapter.Initialize(getBaseContext());
            Log.d("INFO", "AzureServiceAdapter initialized");

        } catch (Exception e) {
            Log.e("ParkPal", "exception", e);
        }
*/
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
    ////////////////////////////////////////////////////////////////////
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ParkDetails.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = checkPermission(ParkDetails.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1337);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1337);
    }
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    ////////////////////////////////////////////////////////////////////
}
