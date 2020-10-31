package com.philip.studio.orderfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.callback.OnItemCategoryClickListener;
import com.philip.studio.orderfood.fragment.HomeFragment;
import com.philip.studio.orderfood.fragment.TrackingOrderFragment;
import com.philip.studio.orderfood.fragment.SavedFragment;
import com.philip.studio.orderfood.fragment.UserFragment;
import com.philip.studio.orderfood.model.User;
import com.philip.studio.orderfood.util.UserUtil;

public class MainActivity extends AppCompatActivity implements OnItemCategoryClickListener {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    UserUtil userUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        frameLayout = findViewById(R.id.frame_layout_container);

        userUtil = new UserUtil(this);

        Intent intent = getIntent();
        if (intent != null){
            User user = intent.getParcelableExtra("user");
            userUtil.setUser(user);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = item -> {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home:
                fragment = new HomeFragment();
                break;
            case R.id.order:
                fragment = new TrackingOrderFragment();
                break;
            case R.id.saved:
                fragment = new SavedFragment();
                break;
            case R.id.me:
                fragment = new UserFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_container, fragment).commit();
        return true;
    };

    @Override
    public void onItemClick(String category) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("data", category);
        startActivity(intent);
    }
}