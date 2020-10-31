package com.philip.studio.orderfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Book;
import com.philip.studio.orderfood.model.Restaurant;
import com.philip.studio.orderfood.model.User;
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
            User user = userUtil.getUser();
            String note = edtNote.getText().toString();
            Book book = new Book(restaurant.getIdRes(), "0926471468", time, 3, event, note);
            Toast.makeText(this, "Book Detail:" + book.toString(), Toast.LENGTH_SHORT).show();
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
        btnBirthday = findViewById(R.id.button_birthday);
        btnDating = findViewById(R.id.button_dating);
        btnLunch = findViewById(R.id.button_lunch);
        btnDinner = findViewById(R.id.button_dinner);
        btnEat = findViewById(R.id.button_eat);
        btnHappyLife = findViewById(R.id.button_happy_life);

        userUtil = new UserUtil(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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