package com.philip.studio.orderfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.adapter.SuggestionAdapter;
import com.philip.studio.orderfood.model.Food;
import com.philip.studio.orderfood.model.Saved;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;

public class FoodDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    FloatingActionButton fabFavorite;
    TextView txtName, txtPrice;
    RecyclerView rVListSuggestion;

    ArrayList<Food> arrayList;
    Food food;
    int position;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        initView();

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("data");
            if (bundle != null) {
                arrayList = bundle.getParcelableArrayList("foods");
                position = bundle.getInt("position");
                food = arrayList.get(position);
                if (food != null) {
                    Glide.with(this).load(food.getImage()).into(imageView);
                    txtName.setText(food.getName());
                    NumberFormat formatter = new DecimalFormat("#,###");
                    double price = food.getPrice();
                    String formattedPrice = formatter.format(price);
                    txtPrice.setText(formattedPrice + "đ");
                }

                setUpRecyclerViewListSuggestion(arrayList);
            }
        }
    }

    private void setUpRecyclerViewListSuggestion(ArrayList<Food> arrayList) {
        rVListSuggestion.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rVListSuggestion.setLayoutManager(layoutManager);

        SuggestionAdapter adapter = new SuggestionAdapter(arrayList, this);
        rVListSuggestion.setAdapter(adapter);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.image_view_food);
        txtName = findViewById(R.id.text_view_name_food);
        txtPrice = findViewById(R.id.text_view_price_food);
        fabFavorite = findViewById(R.id.fab_favorite);
        rVListSuggestion = findViewById(R.id.recycler_view_list_suggestions);

        realm = Realm.getDefaultInstance();
    }
}