package com.psu.sweng500.team4.parkpal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
import com.psu.sweng500.team4.parkpal.Queries.ParkAlertsInsertTask;
import com.psu.sweng500.team4.parkpal.Queries.ParkNotesInsertTask;

import java.util.Date;

public class AddParkAlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_park_alert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final Location location = (Location) getIntent().getSerializableExtra("Location");
        final User currentUser = (User) getIntent().getSerializableExtra("User");
        final EditText alertMessageTextView = (EditText) findViewById(R.id.parkAlert_message);

        Button mSubmitButton = (Button) findViewById(R.id.submitAlert);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date now = new Date();

                // Do Add
                ParkAlertsInsertTask insertAlert = new ParkAlertsInsertTask(new AsyncResponse() {
                    @Override
                    public void processFinish(Object result) {
                    }
                },
                        location.getLocId(),
                        currentUser.getUsername(),
                        alertMessageTextView.getText().toString(),
                        new Date());

                insertAlert.execute();

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                final String url = "http://parkpalapp.azurewebsites.net/api/notification/" + alertMessageTextView.getText().toString();

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.d("INFO", response.substring(0, 500));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Error sending request to : " + url + " : " + error);
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

                finish();
            }
        });
    }

}
