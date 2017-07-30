package com.psu.sweng500.team4.parkpal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
import com.psu.sweng500.team4.parkpal.Queries.ParkNotesInsertTask;

import java.util.Date;

public class AddParkNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_park_note);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        final Location location = (Location) getIntent().getSerializableExtra("Location");
        final User currentUser = (User) getIntent().getSerializableExtra("User");
        final EditText noteMessageTextView = (EditText) findViewById(R.id.parkNote_message);

        Button mSubmitButton = (Button) findViewById(R.id.submitNote);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date now = new Date();

                // Do Add
                ParkNotesInsertTask insertNote = new ParkNotesInsertTask(new AsyncResponse() {
                        @Override
                        public void processFinish(Object result) {
                        }
                    },
                    location.getLocId(),
                    currentUser.getUsername(),
                        noteMessageTextView.getText().toString(),
                    new Date());

                insertNote.execute();
                finish();
            }
        });
    }

}
