package com.mrig.mrignayni.weatherwow;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Mrignayni on 2/6/2015.
 */
public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    // If the user has not chosen a city yet, return
    // Sydney as the default city
    String getCity(){
        return prefs.getString("city", "Las Vegas");
    }

    void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }
}
