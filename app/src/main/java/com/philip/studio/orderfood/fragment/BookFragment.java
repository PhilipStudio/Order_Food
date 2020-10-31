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

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikelau.views.shimmer.ShimmerRecyclerViewX;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.adapter.RestaurantBookAdapter;
import com.philip.studio.orderfood.model.Book;
import com.philip.studio.orderfood.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class BookFragment extends Fragment {

    ImageView imgBack;
    ShimmerRecyclerViewX sRVListBook;
    TabLayout tabLayout;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        initView(view);

        imgBack.setOnClickListener(v -> getActivity().finish());

        setUpShimmerRecyclerView("Nhà hàng");
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        setUpShimmerRecyclerView("Nhà hàng");
                        break;
                    case 1:
                        setUpShimmerRecyclerView("Quán ăn");
                        break;
                    case 2:
                        setUpShimmerRecyclerView("Cafe/Dessert");
                        break;
                    case 3:
                        setUpShimmerRecyclerView("Buffer");
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

    private void setUpShimmerRecyclerView(String data) {
        sRVListBook.showShimmerAdapter();
        sRVListBook.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), ShimmerRecyclerViewX.VERTICAL, false);
        sRVListBook.setLayoutManager(layoutManager);

        loadListCategory(data);
    }

    private void loadListCategory(String text) {
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Restaurant> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    if (restaurant.getLocationCategory().equals(text) && restaurant.getIsBook() == 1){
                        arrayList.add(restaurant);
                    }
                }

                RestaurantBookAdapter adapter = new RestaurantBookAdapter(arrayList, getContext());
                sRVListBook.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView(View view) {
        imgBack = view.findViewById(R.id.image_view_back);
        sRVListBook = view.findViewById(R.id.shimmer_recycler_view_list_book);
        tabLayout = view.findViewById(R.id.tab_layout);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("Restaurant");
    }
}
