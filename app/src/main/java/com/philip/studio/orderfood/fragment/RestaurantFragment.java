package com.philip.studio.orderfood.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikelau.views.shimmer.ShimmerRecyclerViewX;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.adapter.RestaurantCategoryAdapter;
import com.philip.studio.orderfood.model.Restaurant;

import java.util.ArrayList;

public class RestaurantFragment extends Fragment {

    ShimmerRecyclerViewX sRVListRestaurant;
    TabLayout tabLayout;
    ImageView imgBack;
    TextView txtNameLocationCategory;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataResRef;

    double latitude = 18.664577;
    double longitude = 105.686241;
    String category;

    public RestaurantFragment(String category) {
        this.category = category;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        sRVListRestaurant = view.findViewById(R.id.shimmer_recycler_view_list_restaurant);
        tabLayout = view.findViewById(R.id.tab_layout);
        imgBack = view.findViewById(R.id.image_view_back);
        txtNameLocationCategory = view.findViewById(R.id.text_view_name_category);

        txtNameLocationCategory.setText(category);
        imgBack.setOnClickListener(v -> getActivity().finish());

        sRVListRestaurant.showShimmerAdapter();
        sRVListRestaurant.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), ShimmerRecyclerViewX.VERTICAL, false);
        sRVListRestaurant.setLayoutManager(layoutManager);

        loadListRestaurantNearByYou(category);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadListRestaurantNearByYou(category);
                        break;
                    case 1:
                        loadListRestaurantFavorite(category);
                        break;
                    case 2:
                        loadListRestaurantCheapest(category);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void loadListRestaurantNearByYou(String category) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataResRef = firebaseDatabase.getReference().child("Restaurant");
        dataResRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Restaurant> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    if (restaurant.getLocationCategory().equals(category)) {
                        if (distanceCalculation(latitude, longitude,
                                restaurant.getLocation().getLatitude(), restaurant.getLocation().getLongitude()) <= 1) {
                            arrayList.add(restaurant);
                        }
                    }
                }

                RestaurantCategoryAdapter categoryAdapter = new RestaurantCategoryAdapter(arrayList, getContext());
                sRVListRestaurant.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadListRestaurantFavorite(String category) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataResRef = firebaseDatabase.getReference().child("Restaurant");
        dataResRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Restaurant> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    if (restaurant.getLocationCategory().equals(category)) {
                        if (restaurant.getStar() >= 6.5) {
                            arrayList.add(restaurant);
                        }
                    }
                }

                RestaurantCategoryAdapter categoryAdapter = new RestaurantCategoryAdapter(arrayList, getContext());
                sRVListRestaurant.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadListRestaurantCheapest(String category) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataResRef = firebaseDatabase.getReference().child("Restaurant");
        dataResRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Restaurant> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    if (restaurant.getLocationCategory().equals(category)) {
                        if (restaurant.getPriceRange().getStartPrice() > 10000 && restaurant.getPriceRange().getEndPrice() < 100000) {
                            arrayList.add(restaurant);
                        }
                    }
                }

                RestaurantCategoryAdapter categoryAdapter = new RestaurantCategoryAdapter(arrayList, getContext());
                sRVListRestaurant.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private double distanceCalculation(double a, double b, double c, double d) {
        double result;
        result = Math.sqrt((a - c) * (a - c) + (b - d) * (b - d));
        Log.i("result", "" + result);
        return result;
    }
}
