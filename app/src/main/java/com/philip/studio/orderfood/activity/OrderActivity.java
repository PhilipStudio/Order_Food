package com.philip.studio.orderfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.philip.studio.orderfood.R;

public class OrderActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initView();

        toolbar.setNavigationOnClickListener(v -> finish());

    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
    }
}