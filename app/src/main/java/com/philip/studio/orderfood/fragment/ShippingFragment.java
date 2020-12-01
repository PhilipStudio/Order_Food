package com.philip.studio.orderfood.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.callback.DirectionFinderListener;
import com.philip.studio.orderfood.direction.DirectionFinder;
import com.philip.studio.orderfood.model.Route;

import java.util.ArrayList;
import java.util.List;

public class ShippingFragment extends Fragment implements OnMapReadyCallback, DirectionFinderListener {

    GoogleMap googleMap;
    LatLng latLng = new LatLng(18.659160, 105.694847);
    ArrayList<Marker> originMarkers;
    ArrayList<Marker> destinationMarkers;
    ArrayList<Polyline> polylineArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipping, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sendRequest();
        return view;
    }

    private void sendRequest() {
        String origin = "Trường Đại học Vinh";
        String destination = "Bưu Điện Trung Tâm Tp Vinh";
        new DirectionFinder((DirectionFinderListener) getContext(), origin, destination).execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Vinh University"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onDirectionStart() {
        if (originMarkers != null) {
            for (Marker marker :
                    originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null){
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylineArrayList != null){
            for (Polyline polyline: polylineArrayList){
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionSuccess(List<Route> routeList) {
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        polylineArrayList = new ArrayList<>();

        for (Route route: routeList) {
            originMarkers.add(googleMap.addMarker(new MarkerOptions()
                    .title(route.startAddress)
                    .position(route.startLocation)));

            destinationMarkers.add(googleMap.addMarker(new MarkerOptions()
            .title(route.endAddress)
            .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions()
                    .geodesic(true)
                    .color(Color.BLUE)
                    .width(10);

            for (int i=0; i<routeList.size(); i++){
                polylineOptions.add(route.points.get(i));
                polylineArrayList.add(googleMap.addPolyline(polylineOptions));
            }
        }
    }
}
