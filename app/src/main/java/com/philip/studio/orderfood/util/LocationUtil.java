package com.philip.studio.orderfood.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.philip.studio.orderfood.model.Location;

public class LocationUtil {
    private SharedPreferences preferences;

    public LocationUtil(Context context) {
        this.preferences = context.getSharedPreferences("location", Context.MODE_PRIVATE);
    }

    public void setLocation(long latitude, long longitude){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("latitude", latitude);
        editor.putLong("longitude", longitude);
        editor.apply();
    }

    public Location getLocation(){
        long latitude = preferences.getLong("latitude", 0);
        long longitude = preferences.getLong("longitude", 0);
        Location location = new Location(latitude, longitude);
        return location;
    }
}
