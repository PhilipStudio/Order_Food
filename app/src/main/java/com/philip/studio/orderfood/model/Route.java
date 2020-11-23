package com.philip.studio.orderfood.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {
    public Distance distance;
    public Duration duration;
    public String startAddress;
    public String endAddress;
    public LatLng startLocation;
    public LatLng endLocation;
    public List<LatLng> points;
}
