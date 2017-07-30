package com.psu.sweng500.team4.parkpal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
import com.psu.sweng500.team4.parkpal.Queries.LocationQueryTask;
import com.psu.sweng500.team4.parkpal.Queries.UserQueryTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class RecommendationsFragment extends Fragment {

    private User mCurrentUser;
    private View mView;
    private FragmentActivity fragmentActivity;
    private String ip = "192.168.1.192";
    private String recURL = "http://" + ip + ":8080/recommendations/user/";

    public RecommendationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mCurrentUser = (User) getArguments().getSerializable("User");
        fragmentActivity = this.getActivity();
        new HttpRequestTask().execute();
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_recommendations, container, false);
        return mView;
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                final String url = recURL + mCurrentUser.getId();
                int timeoutSec = 10;

                HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory();
                rf.setReadTimeout(timeoutSec * 1000);
                rf.setConnectTimeout(timeoutSec * 1000);

                RestTemplate restTemplate = new RestTemplate(rf);
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

                return response.getBody();
            } catch (Exception e) {
                Log.e("RecommendationsFragment", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            showRecommendations(response);
        }

    }

    private void initRecommendationsList(final List<Location> parkList){
        final ListView listView = (ListView) mView.findViewById(R.id.recommendations);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(fragmentActivity, ParkDetails.class);
                intent.putExtra("Location", parkList.get(position));
                intent.putExtra("User", mCurrentUser);
                fragmentActivity.startActivity(intent);
            }
        });
        // If there are parks in the list, add them to the adapter
        if (!parkList.isEmpty()){
            listView.setAdapter(new RecommendationListAdapter(getContext(), parkList, mCurrentUser));
        }
    }

    private void showRecommendations(String parkIds) {
        long loc_id;
        final ArrayList<Location> recommendationList = new ArrayList<Location>();

        if (parkIds == null || parkIds.isEmpty()) {
            ViewGroup v = (ViewGroup) mView.findViewById(R.id.recommendations).getParent();
            v.removeView(mView.findViewById(R.id.recommendations));

            TextView textView = new TextView(mView.getContext());
            textView.setText("You have no recommendations at this time");
            v.addView(textView);

            return; // no recommendations to show
        }

        for (String locIdString : parkIds.split(" ")) {
            loc_id = Long.parseLong(locIdString);

            // Check if user has ParkPal account
            LocationQueryTask asyncQuery = new LocationQueryTask(new AsyncResponse() {
                @Override
                public void processFinish(Object result) {
                    recommendationList.addAll((List<Location>) result);

                    if(recommendationList.size() == 5){
                        initRecommendationsList(recommendationList);
                    }
                }
            }, loc_id);

            asyncQuery.execute();
        }
    }
}
