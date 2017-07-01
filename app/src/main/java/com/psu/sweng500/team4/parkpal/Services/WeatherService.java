package com.psu.sweng500.team4.parkpal.Services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.psu.sweng500.team4.parkpal.Models.Weather.Weather;
import com.psu.sweng500.team4.parkpal.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
    Created by brhoads on 7/1/2017.

    Example usage:

        WeatherService weatherService = new WeatherService(this);
        weatherService.execute(40f, -77f);

    Modify the postExecute method to update the UI with the relevant weather components
    API Documentation:  http://openweathermap.org/current
 */

public class WeatherService extends AsyncTask<Float, Void, Weather> {

    private Context context;
    private String apiKey;
    private Weather weather;

    public WeatherService(Context context) {
        this.context = context;
        apiKey = context.getString(R.string.OpenWeatherApiKey);
    }

    protected Weather doInBackground(Float... coords) {
        HttpURLConnection urlConnection = null;
        URL url;
        InputStream inStream = null;

        String urlString =
                String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=imperial&appid=%s",
                        coords[0], coords[1], apiKey);

        try {
            url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inStream = urlConnection.getInputStream();

            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }
            Gson gson = new GsonBuilder().create();
            weather = gson.fromJson(response, Weather.class);
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return weather;
    }

    protected void onPostExecute(Weather weather) {
        // TODO: do something with the weather.  Update the info window on the map with relevant temperature, conditions, etc.

        Log.d("INFO", "Weather retrieved : " + weather.getMain().getTemp() + " : " + weather.getClouds().getAll());
    }
}
