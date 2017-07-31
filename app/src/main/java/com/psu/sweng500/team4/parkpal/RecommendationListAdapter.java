package com.psu.sweng500.team4.parkpal;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.psu.sweng500.team4.parkpal.Models.Location;
import com.psu.sweng500.team4.parkpal.Models.User;
import com.psu.sweng500.team4.parkpal.Models.Weather.Weather;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
import com.psu.sweng500.team4.parkpal.Services.WeatherService;

import java.util.List;

/**
 * Created by ShansMcB on 7/25/2017.
 */

public class RecommendationListAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private User mCurrentUser;
    private List<Location> mRecommendedParks;
    private static LayoutInflater layoutInflater = null;

    public RecommendationListAdapter(Context context, List<Location> recommendedParks, User currentUser) {
        this.context = context;
        this.mRecommendedParks = recommendedParks;
        this.mCurrentUser = currentUser;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mRecommendedParks.size();
    }

    @Override
    public Object getItem(int position) {
        //return mRecommendedParks.get(position);
        return position;
    }

    @Override
    public long getItemId(int position) {
//        return mRecommendedParks.get(position).getLocId();
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Location park = mRecommendedParks.get(position);
        View rowView;

        rowView = layoutInflater.inflate(R.layout.park_recommendation_layout, null);

        TextView tvLocation = (TextView) rowView.findViewById(R.id.tvLocation);
        TextView tvSnippet = (TextView) rowView.findViewById(R.id.tvSnippet);
        TextView tvAmenities = (TextView) rowView.findViewById(R.id.tvAmenities);
        TextView tvSeason = (TextView) rowView.findViewById(R.id.tvSeason);
        TextView tvAlerts = (TextView) rowView.findViewById(R.id.tvAlerts);

        //sets the variables created above to the current information
        tvLocation.setText(park.getName());
        // Snippet returns city and state
        tvSnippet.setText(park.getTown() + ", " + park.getState());
        //sets date open
        tvSeason.setText("Dates Open: " + park.getDatesOpen());
        //TODO - Add icons to represent the various amenities
        tvAmenities.setText(park.getAmenities());

        final ImageView iv = (ImageView) rowView.findViewById(R.id.ivWeatherIcon);
        final TextView tvCurrentTemp = (TextView) rowView.findViewById(R.id.tvCurrentTemp);

        WeatherService weatherService = new WeatherService(context, new AsyncResponse() {
            @Override
            public void processFinish(Object result) {
                WeatherService weatherService = (WeatherService) result;
                Weather weather = weatherService.getWeather();

                iv.setImageDrawable(weatherService.getWeatherIcon());

                tvCurrentTemp.setText(weather.getPrettyTempstring());
            }
        });

        weatherService.execute(park.getLatitude(), park.getLongitude());

        //TODO - Get location alerts

        // TODO - Set background image from custom images

        // Return the completed view to render on screen
        return rowView;
    }

    @Override
    public void onClick(View v) {

    }
}