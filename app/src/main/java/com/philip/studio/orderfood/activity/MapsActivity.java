package com.philip.studio.orderfood.activity;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.philip.studio.orderfood.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private String nameAddress;
    Button btnChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnChoose = findViewById(R.id.button_choose);

        geocoder = new Geocoder(this, Locale.getDefault());

        btnChoose.setOnClickListener(v -> {

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        LatLng postOffice = new LatLng(18.671223, 105.684085);
        mMap.addMarker(new MarkerOptions().position(postOffice).title("Bưu điện trung tâm Vinh"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(postOffice, 15.0f));

        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            List<Address> addressList = new ArrayList<>();
            try {
                addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address = addressList.get(0);
            nameAddress = address.getAddressLine(0);

            LatLng latLng1 = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng1).title(nameAddress));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 15.0f));
        });
    }
}