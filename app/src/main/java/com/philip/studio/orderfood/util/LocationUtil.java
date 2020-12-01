package com.philip.studio.orderfood.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;

public class LocationUtil {
    private SharedPreferences sharedPreferences;

    public LocationUtil(Context context) {
        sharedPreferences = context.getSharedPreferences("location", Context.MODE_PRIVATE);
    }

    public void setLocationUtil(double lat, double lng){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lat", String.valueOf(lat));
        editor.putString("lng", String.valueOf(lng));
        editor.apply();
    }

    public LatLng getLocationUtil(){
        double lat = 0;
        double lng = 0;

        String str1 = sharedPreferences.getString("lat", null);
        String str2 = sharedPreferences.getString("lng", null);

        if (!TextUtils.isEmpty(str1) && !TextUtils.isEmpty(str2)){
            lat = Double.parseDouble(str1);
            lng = Double.parseDouble(str2);
        }
        return new LatLng(lat, lng);
    }

    public void setNameUserLocation(String str){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name_location", str);
        editor.apply();
    }

    public String getNameUserLocation(){
        String name = sharedPreferences.getString("name_location", null);
        return name;
    }
}
