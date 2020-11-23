package com.philip.studio.orderfood.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Book;
import com.philip.studio.orderfood.model.Restaurant;
import com.philip.studio.orderfood.util.UserUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView txtNameUser, txtNameRestaurant, txtEvent, txtDiscount,
            txtContentPromotion, txtTime;
    EditText edtNote;
    GoogleMap googleMap;
    Button btnBook, btnEat, btnBirthday, btnDating, btnLunch, btnDinner, btnHappyLife;

    Restaurant restaurant;
    String time, event;
    UserUtil userUtil;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initView();

        Intent intent = getIntent();
        if (intent != null){
            restaurant = intent.getParcelableExtra("data");
            txtNameRestaurant.setText(restaurant.getName());
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        time = format.format(new Date());
        txtTime.setText(time);

        btnHappyLife.setOnClickListener(listener);
        btnEat.setOnClickListener(listener);
        btnDinner.setOnClickListener(listener);
        btnDating.setOnClickListener(listener);
        btnLunch.setOnClickListener(listener);
        btnBirthday.setOnClickListener(listener);

        btnBook.setOnClickListener(v -> {
            String note = edtNote.getText().toString();
            String restaurantID = restaurant.getIdRes();
            String idUser = "0926471468";
            String idBook = restaurant.getIdRes() + idUser;
            Book book = new Book(idBook, restaurantID, idUser, time, 3, event, note);
            Intent intentBook = new Intent(BookDetailActivity.this, PaymentActivity.class);
            startActivity(intentBook);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        edtNote = findViewById(R.id.edit_text_note);
        txtNameUser = findViewById(R.id.text_view_name_user);
        txtNameRestaurant = findViewById(R.id.text_view_name_restaurant);
        txtEvent = findViewById(R.id.text_view_event);
        txtDiscount = findViewById(R.id.text_view_discount);
        txtContentPromotion = findViewById(R.id.text_view_content_promotion);
        txtTime = findViewById(R.id.text_view_time);
        btnBook = findViewById(R.id.button_book);
        btnBirthday = findViewById(R.id.button_birthday);
        btnDating = findViewById(R.id.button_dating);
        btnLunch = findViewById(R.id.button_lunch);
        btnDinner = findViewById(R.id.button_dinner);
        btnEat = findViewById(R.id.button_eat);
        btnHappyLife = findViewById(R.id.button_happy_life);

        userUtil = new UserUtil(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("Book");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        double latitude = restaurant.getLocation().getLatitude();
        double longitude = restaurant.getLocation().getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(new MarkerOptions().position(latLng));
    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_birthday:
                    event = btnBirthday.getText().toString();
                    break;
                case R.id.button_dating:
                    event = btnDating.getText().toString();
                    break;
                case R.id.button_dinner:
                    event = btnDinner.getText().toString();
                    break;
                case R.id.button_lunch:
                    event = btnLunch.getText().toString();
                    break;
                case R.id.button_happy_life:
                    event = btnHappyLife.getText().toString();
                    break;
                case R.id.button_eat:
                    event = btnEat.getText().toString();
                    break;
            }
        }
    };

    public void onBack(View view){
        finish();
    }
}