package com.philip.studio.orderfood.direction;

import com.philip.studio.orderfood.callback.DirectionFinderListener;
import com.philip.studio.orderfood.util.LoadData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_MAP_API_KEY = "AIzaSyCS83TQJLe820Qd56JC1QsXQJfSdburLsQ";
    private DirectionFinderListener listener;
    private String origin, destination;

    public DirectionFinder(DirectionFinderListener listener, String origin, String destination) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
    }

    public String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");

        return DIRECTION_URL_API + "origin" + urlOrigin + "&destination" + urlDestination + "&key" + GOOGLE_MAP_API_KEY;
    }

    public void execute(){
        listener.onDirectionStart();
        try {
            new LoadData().execute(createUrl());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
