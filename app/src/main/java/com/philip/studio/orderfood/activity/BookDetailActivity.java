package com.philip.studio.orderfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Book;

public class BookDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView txtNameUser, txtNameRestaurant, txtEvent, txtDiscount,
            txtContentPromotion, txtTime;
    EditText edtNote;
    GoogleMap googleMap;
    Button btnBook;

    Book data;
    String time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initView();

        Intent intent = getIntent();
        if (intent != null){
            Bundle bundle = intent.getBundleExtra("data");
            data = bundle.getParcelable("book_detail");
            time = bundle.getString("time");
            txtNameRestaurant.setText(data.getRestaurant().getName());
            txtTime.setText(time);
        }

        btnBook.setOnClickListener(v -> {
            Book book = new Book(data.getRestaurant(), time, data.getImage(), data.getNumberPerson(),
                    data.getIsPromotion(), data.getDiscount(), data.getEvent(), data.getContent());
        });

    }

    private void initView(){
        edtNote = findViewById(R.id.edit_text_note);
        txtNameUser = findViewById(R.id.text_view_name_user);
        txtNameRestaurant = findViewById(R.id.text_view_name_restaurant);
        txtEvent = findViewById(R.id.text_view_event);
        txtDiscount = findViewById(R.id.text_view_discount);
        txtContentPromotion = findViewById(R.id.text_view_content_promotion);
        txtTime = findViewById(R.id.text_view_time);
        btnBook = findViewById(R.id.button_book);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        double latitude = data.getRestaurant().getLocation().getLatitude();
        double longitude = data.getRestaurant().getLocation().getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(new MarkerOptions().position(latLng));
    }

    public void onBack(View view){
        finish();
    }
}