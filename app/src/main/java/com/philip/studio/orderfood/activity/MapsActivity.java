package com.philip.studio.orderfood.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.util.LocationUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    GoogleMap mMap;
    Geocoder geocoder;
    String nameAddress;
    Address address;
    LocationUtil locationUtil;
    private boolean isLocationPermissionGranted = false;
    static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    static final float DEFAULT_ROOM = 15f;

    Button btnChoose;
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initView();

        getLocationPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapClickListener(this);

        btnChoose.setOnClickListener(v -> {
            String result = materialSearchBar.getText();
            if (TextUtils.isEmpty(result)){
                Toast.makeText(this, "Location is null", Toast.LENGTH_SHORT).show();
            }
            else{
                locationUtil.setNameUserLocation(result);
                Intent intent = new Intent();
                intent.putExtra("address", result);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        address = addressList.get(0);
        nameAddress = address.getAddressLine(0);
        materialSearchBar.setText(nameAddress);

        LatLng latLng1 = new LatLng(address.getLatitude(), address.getLongitude());
        showMarkerInGoogleMap(latLng1, nameAddress);
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        isLocationPermissionGranted = false;
    }

    private void getCurrentLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            Task<Location> locationTask = mFusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(location -> {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                locationUtil.setLocationUtil(lat, lng);
                LatLng latLng = new LatLng(lat, lng);
                showCameraToPosition(latLng, DEFAULT_ROOM);
                showMarkerInGoogleMap(latLng, "My current location");
            }).addOnFailureListener(e -> {
                Log.d("error", e.getLocalizedMessage());
            });
        } catch (SecurityException securityException) {
            Log.d("phuc", securityException.getMessage());
        }
    }

    private void showMarkerInGoogleMap(LatLng latLng, String my_current_location) {
        mMap.addMarker(new MarkerOptions().position(latLng).title(my_current_location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }

    private void showCameraToPosition(LatLng latLng, float defaultRoom) {
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(defaultRoom)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
        }
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (isLocationPermissionGranted){
            getCurrentLocation();
        }
    }

    private void initView(){
        btnChoose = findViewById(R.id.button_choose);
        materialSearchBar = findViewById(R.id.search_bar);

        geocoder = new Geocoder(this, Locale.getDefault());
        locationUtil = new LocationUtil(MapsActivity.this);
    }
}