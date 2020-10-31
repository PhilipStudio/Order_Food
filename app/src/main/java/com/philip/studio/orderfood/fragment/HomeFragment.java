package com.philip.studio.orderfood.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sliderviewlibrary.SliderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikelau.views.shimmer.ShimmerRecyclerViewX;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.AddressActivity;
import com.philip.studio.orderfood.activity.CategoryActivity;
import com.philip.studio.orderfood.adapter.CategoryAdapter;
import com.philip.studio.orderfood.callback.OnItemCategoryClickListener;
import com.philip.studio.orderfood.model.Category;
import com.philip.studio.orderfood.model.Restaurant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    ShimmerRecyclerViewX sRVListCategory;
    TextView txtAddress;
    Button btnFavorite, btnBook;
    Spinner spinner;
    SliderView sliderView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;

    OnItemCategoryClickListener onItemCategoryClickListener;
    String[] locationCategory = {"Tất cả", "Nhà hàng", "Quán ăn", "Khách sạn", "Ăn vặt/vỉa hè"};
    ArrayList<Integer> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);

        arrayList.add(R.drawable.food_banner);
        arrayList.add(R.drawable.food_banner_543);
        sliderView.setImages(arrayList);

        TimerTask task = sliderView.getTimerTask();
        Timer timer = new Timer();
        timer.schedule(task, 1000, 1000);

        setUpSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        if (onItemCategoryClickListener != null) {
                            onItemCategoryClickListener.onItemClick("Nhà hàng");
                        }
                        break;
                    case 2:
                        if (onItemCategoryClickListener != null) {
                            onItemCategoryClickListener.onItemClick("Quán ăn");
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sRVListCategory.showShimmerAdapter();
        sRVListCategory.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        sRVListCategory.setLayoutManager(layoutManager);

        loadMenu();

        txtAddress.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddressActivity.class);
            startActivity(intent);
        });

        btnBook.setOnClickListener(listener);
        btnFavorite.setOnClickListener(listener);

        return view;
    }

    private void setUpSpinner() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, locationCategory);
        spinner.setAdapter(arrayAdapter);
    }

    private void loadMenu() {
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Restaurant> arrayList1 = new ArrayList<>();
                ArrayList<Restaurant> arrayList2 = new ArrayList<>();
                ArrayList<Restaurant> arrayList3 = new ArrayList<>();
                ArrayList<Category> categories = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    arrayList3.add(restaurant);

                    if (restaurant.getPromotion().getIsPromotion() == 1) {
                        arrayList1.add(restaurant);
                    } else {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            String currentTime = format.format(new Date());
                            Date currentDateTime = format.parse(currentTime);
                            Date startDateTime = format.parse(restaurant.getBusinessHours().getOpenTime());
                            Date endDateTime = format.parse(restaurant.getBusinessHours().getCloseTime());

                            long current = currentDateTime.getTime();
                            long start = startDateTime.getTime();
                            long end = endDateTime.getTime();

                            if (current > start && current < end) {
                                arrayList2.add(restaurant);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                categories.add(new Category("Khuyến mãi", arrayList1, 0));
                categories.add(new Category("Dành cho bạn", arrayList2, 0));
                categories.add(new Category("Thể loại", arrayList3, 1));

                CategoryAdapter adapter = new CategoryAdapter(categories, getContext());
                sRVListCategory.setAdapter(adapter);

                adapter.setOnItemCategoryClickListener(category -> {
                    Intent intent = new Intent(getContext(), CategoryActivity.class);
                    intent.putExtra("data", category);
                    startActivity(intent);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private View.OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.button_book:
                if (onItemCategoryClickListener != null) {
                    onItemCategoryClickListener.onItemClick("Đặt bàn");
                }
                break;
            case R.id.button_favorite:
                if (onItemCategoryClickListener != null) {
                    onItemCategoryClickListener.onItemClick("Yêu thích");
                }
                break;
        }
    };

    private void initView(View view) {
        sRVListCategory = view.findViewById(R.id.shimmer_recycler_view_list_category);
        txtAddress = view.findViewById(R.id.text_view_address);
        spinner = view.findViewById(R.id.spinner);
        btnBook = view.findViewById(R.id.button_book);
        btnFavorite = view.findViewById(R.id.button_favorite);
        sliderView = view.findViewById(R.id.slider_view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("Restaurant");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        onItemCategoryClickListener = (OnItemCategoryClickListener) context;
    }
}
