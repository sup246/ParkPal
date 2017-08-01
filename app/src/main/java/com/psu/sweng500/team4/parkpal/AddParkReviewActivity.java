package com.psu.sweng500.team4.parkpal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
import com.psu.sweng500.team4.parkpal.Queries.ParkRatingInsertTask;

/**
 * Created by Stefanie Gjorven on 7/29/2017.
 */

public class AddParkReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_park_review);

        final Location location = (Location) getIntent().getSerializableExtra("Location");
        final User currentUser = (User) getIntent().getSerializableExtra("User");

        final RatingBar ratingBar = (RatingBar) findViewById(R.id.addRatingBar);
        final EditText editText = (EditText) findViewById(R.id.addReviewTxt);

        Button submitButton = (Button) findViewById(R.id.btSubmitReview);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float stars = ratingBar.getRating();
                String comment = editText.getText().toString();

                addReview(location.getLocId(), currentUser.getUsername(), 1, comment, stars);
                Intent intent = getIntent();
                intent.putExtra("Stars", stars);
                intent.putExtra("Comment", comment);
                setResult(666, intent);

                finish();
            }
        });
    }

    private void addReview(long locId, String username, int user_id, String review, float rating) {
        ParkRatingInsertTask insertAlert = new ParkRatingInsertTask(new AsyncResponse() {
            @Override
            public void processFinish(Object result) {
            }
        },
                locId,
                username,
                user_id,
                (int) rating);

        insertAlert.execute();
    }
}
