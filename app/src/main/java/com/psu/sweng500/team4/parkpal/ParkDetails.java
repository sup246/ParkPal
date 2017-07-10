package com.psu.sweng500.team4.parkpal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Models.ParkNote;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
import com.psu.sweng500.team4.parkpal.Queries.ParkNotesQueryTask;
import com.psu.sweng500.team4.parkpal.Services.AzureServiceAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParkDetails extends AppCompatActivity {
    private Location mLocation;
    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_park_details);

        mLocation = (Location) getIntent().getSerializableExtra("Location");
        getListInfo(mLocation.getLocId());
        mCurrentUser = (User) getIntent().getSerializableExtra("User");

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
}
