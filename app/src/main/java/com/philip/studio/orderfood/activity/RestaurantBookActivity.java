package com.philip.studio.orderfood.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.adapter.BookAdapter;
import com.philip.studio.orderfood.callback.OnItemBookClickListener;
import com.philip.studio.orderfood.model.Book;
import com.philip.studio.orderfood.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantBookActivity extends AppCompatActivity {

    ImageView imgImage;
    RecyclerView rVListBook;
    TextView txtNameRes, txtAddressRes;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_book);

        initView();

        Intent intent = getIntent();
        if (intent != null){
            Restaurant restaurant = intent.getParcelableExtra("data");
            Glide.with(this).load(restaurant.getLogo()).into(imgImage);
            txtNameRes.setText(restaurant.getName());

            setUpRecyclerViewListBook();
        }
    }

    private void setUpRecyclerViewListBook(){
        rVListBook.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rVListBook.setLayoutManager(layoutManager);

        dataRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Book> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    arrayList.add(new Book(restaurant, "Cả ngày", " ",
                            3,
                            restaurant.getPromotion().getIsPromotion(),
                            restaurant.getPromotion().getDiscount(),
                            "Cả ngày",
                            restaurant.getPromotion().getContent()
                    ));
                }

                BookAdapter bookAdapter = new BookAdapter(arrayList, RestaurantBookActivity.this);
                rVListBook.setAdapter(bookAdapter);

                bookAdapter.setOnItemBookClickListener((isClick, button) -> {
                    if (isClick){
                        showDatePickerDialog(button);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDatePickerDialog(Button button) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> button.setText(dayOfMonth + "/" + (month + 1) + "/" + year));
        datePickerDialog.show();
    }

    private void initView(){
        imgImage = findViewById(R.id.image_view_image);
        rVListBook = findViewById(R.id.recycler_view_list_book);
        txtNameRes = findViewById(R.id.text_view_name_restaurant);
        txtAddressRes = findViewById(R.id.text_view_address_restaurant);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("Restaurant");
    }

    public void onBack(View view){
        finish();
    }
}