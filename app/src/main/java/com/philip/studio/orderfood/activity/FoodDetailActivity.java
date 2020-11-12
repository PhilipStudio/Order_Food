package com.philip.studio.orderfood.activity;

import androidx.appcompat.app.AppCompatActivity;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import io.realm.Realm;

public class FoodDetailActivity extends AppCompatActivity {
    ImageView imageView;
    FloatingActionButton fabFavorite;
    TextView txtName, txtPrice, txtLike;
    RecyclerView rVListSuggestion;

    ArrayList<Food> arrayList;
    Food food;
    int position;
    Realm realm;
    boolean isClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        Realm.init(this);
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
                    txtLike.setText(String.valueOf(food.getLike()));
                }

                setUpRecyclerViewListSuggestion(arrayList);
            }
        }

        fabFavorite.setOnClickListener(v -> {
            isClick = true;
            if (isClick){
                fabFavorite.setImageResource(R.drawable.ic_baseline_favorite);
            }
            else{
                fabFavorite.setImageResource(R.drawable.ic_baseline_favorite_border);
            }
        });
    }

    private void setUpRecyclerViewListSuggestion(ArrayList<Food> arrayList) {
        rVListSuggestion.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rVListSuggestion.setLayoutManager(layoutManager);

        SuggestionAdapter adapter = new SuggestionAdapter(arrayList, this);
        rVListSuggestion.setAdapter(adapter);

        adapter.setOnItemSuggestionClickListener(data -> {
            if (data != null) {
                Glide.with(FoodDetailActivity.this).load(data.getImage()).into(imageView);
                txtName.setText(data.getName());
                NumberFormat formatter = new DecimalFormat("#,###");
                double price = data.getPrice();
                String formattedPrice = formatter.format(price);
                txtPrice.setText(formattedPrice + "đ");
                txtLike.setText(String.valueOf(data.getLike()));
            }
        });
    }

    public void onBack(View view){
        finish();
    }

    private void initView() {
        imageView = findViewById(R.id.image_view_food);
        txtName = findViewById(R.id.text_view_name_food);
        txtPrice = findViewById(R.id.text_view_price_food);
        fabFavorite = findViewById(R.id.fab_favorite);
        rVListSuggestion = findViewById(R.id.recycler_view_list_suggestions);
        txtLike = findViewById(R.id.text_view_like_food);

        realm = Realm.getDefaultInstance();
    }
}