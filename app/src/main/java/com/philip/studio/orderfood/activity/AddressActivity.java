package com.philip.studio.orderfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.fragment.AddressFragment;

public class AddressActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        frameLayout = findViewById(R.id.frame_layout_container);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_container, new AddressFragment())
                .commit();
    }
}