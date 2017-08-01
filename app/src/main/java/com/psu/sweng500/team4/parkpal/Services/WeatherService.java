package com.psu.sweng500.team4.parkpal.Services;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.psu.sweng500.team4.parkpal.Models.Weather.Weather;
import com.psu.sweng500.team4.parkpal.Queries.AsyncResponse;
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

public class WeatherService extends AsyncTask<Float, Void, WeatherService> {

    private Context context;
    private String apiKey;
    private Weather weather;
    private Drawable weatherIcon;

    public AsyncResponse delegate = null;

    public WeatherService(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        apiKey = context.getString(R.string.OpenWeatherApiKey);
    }

    protected WeatherService doInBackground(Float... coords) {
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

            weatherIcon = LoadImageFromWebOperations("http://openweathermap.org/img/w/" +
                    weather.getWeather().get(0).getIcon() + ".png");
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
        return this;
    }

    protected void onPostExecute(WeatherService weatherService) {
        //Log.d("INFO", "Weather retrieved : " + weather.getMain().getTemp() + " : " + weather.getClouds().getAll());

        delegate.processFinish(weatherService);
    }

    private static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public Drawable getWeatherIcon() {
        return weatherIcon;
    }
    public Weather getWeather() {
        return weather;
    }
}
