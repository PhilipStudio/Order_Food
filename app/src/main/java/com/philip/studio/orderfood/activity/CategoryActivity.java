package com.philip.studio.orderfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.fragment.BookFragment;
import com.philip.studio.orderfood.fragment.RestaurantFavoriteFragment;
import com.philip.studio.orderfood.fragment.RestaurantFragment;

public class CategoryActivity extends AppCompatActivity {

    FrameLayout frameLayout;

    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        frameLayout = findViewById(R.id.frame_layout_container);

        Intent intent = getIntent();
        if (intent != null) {
            category = intent.getStringExtra("data");
            if (category.equals("Đặt bàn")){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout_container, new BookFragment())
                        .commit();
            }
            else if (category.equals("Yêu thích")){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout_container, new RestaurantFavoriteFragment())
                        .commit();
            }
            else{
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout_container, new RestaurantFragment(category))
                        .commit();
            }
        }
    }
}