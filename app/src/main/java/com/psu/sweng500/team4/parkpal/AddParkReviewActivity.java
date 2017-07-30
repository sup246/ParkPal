package com.psu.sweng500.team4.parkpal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;

/**
 * Created by Stefanie Gjorven on 7/29/2017.
 */

public class AddParkReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.park_review_layout);


        final RatingBar ratingBar = (RatingBar) findViewById(R.id.addRatingBar);
        final EditText editText = (EditText) findViewById(R.id.addReviewTxt);

        Button submitButton = (Button) findViewById(R.id.btSubmitReview);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float stars = ratingBar.getRating();
                String comment = editText.getText().toString();

                Intent intent = getIntent();
                intent.putExtra("Stars", stars);
                intent.putExtra("Comment", comment);
                setResult(666, intent);

                finish();
            }
        });
    }
}
