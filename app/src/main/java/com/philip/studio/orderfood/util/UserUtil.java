package com.philip.studio.orderfood.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.philip.studio.orderfood.model.User;

public class UserUtil {
    private SharedPreferences preferences;

    public UserUtil(Context context) {
        preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public void setUser(User user){
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", userJson);
        editor.apply();
    }

    public User getUser(){
        String userObject = preferences.getString("user", null);
        Gson gson = new Gson();
        return gson.fromJson(userObject, User.class);
    }

}
