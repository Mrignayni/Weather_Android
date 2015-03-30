/**
 * Created by Mrignayni on 2/6/2015.
 */

package com.mrig.mrignayni.weatherwow;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

//import java.util.logging.Handler;


public class WeatherFragment extends Fragment {
    Typeface weatherFont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    public double temperatureC;
    public double temperatureF;
    Handler handler;
    OnDataPass dataPasser;

    public interface OnDataPass {
        public void onDataPass(double tempC);
    }
    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        dataPasser = (OnDataPass) a;
    }

    public void passData(double data) {
        dataPasser.onDataPass(data);
    }

    public WeatherFragment(){
        handler = new Handler();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");

        updateWeatherData(new CityPreference(getActivity()).getCity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        cityField = (TextView)rootView.findViewById(R.id.city_field);
        updatedField = (TextView)rootView.findViewById(R.id.updated_field);
        detailsField = (TextView)rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)rootView.findViewById(R.id.weather_icon);

        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    private void renderWeather(JSONObject json){
        try {
            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");


            temperatureC = main.getDouble("temp");
            //temperatureF= 32.00 + (temperatureC* 9.00 / 5.00);
            currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " °C");


            //dataPasser.onDataPass(temperatureC);
            passData(temperatureC);
            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("WeatherWow", "One or more fields not found in the JSON data");
        }
    }

   // private void setWeatherIcon(int id, long l, long l1) {
    //}
    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        //Toast weatherJoke ;
        if(actualId == 800){
            long currentTime = new Date().getTime();

            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);

            } else {
                icon = getActivity().getString(R.string.weather_clear_night);

            }
        } else {

            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;

                case 6 : icon = getActivity().getString(R.string.weather_snowy);
//
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
//
                    break;
                default:
                    break;

            }
        }
        weatherIcon.setText(icon);
    }

    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    public void changeCity(String city){
        updateWeatherData(city);
    }
}