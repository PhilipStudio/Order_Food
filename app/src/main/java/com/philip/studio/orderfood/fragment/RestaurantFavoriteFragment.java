package com.philip.studio.orderfood.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class RestaurantFavoriteFragment extends Fragment {

    RecyclerView rVListRestaurant;
    ImageView imgBack;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataResRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_favorite, container, false);
        rVListRestaurant = view.findViewById(R.id.recycler_view_list_restaurant);
        imgBack = view.findViewById(R.id.image_view_back);

        imgBack.setOnClickListener(v -> getActivity().finish());

        rVListRestaurant.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), ShimmerRecyclerViewX.VERTICAL, false);
        rVListRestaurant.setLayoutManager(layoutManager);

        loadListRestaurantFavorite();

        return view;
    }

    private void loadListRestaurantFavorite() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataResRef = firebaseDatabase.getReference().child("Restaurant");
        dataResRef.orderByChild("star").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Restaurant> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    if (restaurant.getStar() >= 6.5) {
                        arrayList.add(restaurant);
                    }
                }

                RestaurantCategoryAdapter categoryAdapter = new RestaurantCategoryAdapter(arrayList, getContext());
                rVListRestaurant.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
