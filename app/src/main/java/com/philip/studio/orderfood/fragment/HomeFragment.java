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
import com.philip.studio.orderfood.util.LocationUtil;

import java.text.ParseException;
import java.text.ParsePosition;
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
    String[] locationCategory = {"Tất cả", "Nhà hàng", "Quán ăn", "Cafe/Dessert", "Ăn vặt/vỉa hè"};
    ArrayList<Integer> arrayList = new ArrayList<>();
    LocationUtil locationUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);

        arrayList.add(R.drawable.food_banner);
        arrayList.add(R.drawable.food_banner_543);
        sliderView.setImages(arrayList);

        if (locationUtil.getNameUserLocation() != null){
            txtAddress.setText(locationUtil.getNameUserLocation());
        }

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
                    case 3:
                        if (onItemCategoryClickListener != null) {
                            onItemCategoryClickListener.onItemClick("Cafe/Dessert");
                        }
                        break;
                    case 4:
                        if (onItemCategoryClickListener != null) {
                            onItemCategoryClickListener.onItemClick("Ăn vặt/vỉa hè");
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

        try {
            loadMenu();
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

    private void loadMenu() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String currentTime = format.format(new Date());

        Date currentDateTime = format.parse(currentTime);
        Date dateTimeStartMorning = stringToDate("07:30", "HH:mm"); //buoi sang
        Date dateTimeEndMorning = stringToDate("9:00", "HH:mm");

        Date dateTimeStartNoon = stringToDate("10:00", "HH:mm"); //buoi trua
        Date dateTimeEndNoon = stringToDate("12:30", "HH:mm");

        Date dateTimeStartAfternoon = stringToDate("14:00", "HH:mm"); //buoi chieu
        Date dateTimeEndAfternoon = stringToDate("17:00", "HH:mm");

        Date dateTimeStartNight = stringToDate("18:00", "HH:mm"); //buoi toi
        Date dateTimeEndNight = stringToDate("19:45", "HH:mm");

        long current = currentDateTime.getTime(); // gio hien tai
        long timeStartMorning = dateTimeStartMorning.getTime();
        long timeEndMorning = dateTimeEndMorning.getTime();

        long timeStartNoon = dateTimeStartNoon.getTime();
        long timeEndNoon = dateTimeEndNoon.getTime();

        long timeStartAfternoon = dateTimeStartAfternoon.getTime();
        long timeEndAfternoon = dateTimeEndAfternoon.getTime();

        long timeStartNight = dateTimeStartNight.getTime();
        long timeEndNight = dateTimeEndNight.getTime();

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
                            Date openTime = format.parse(restaurant.getBusinessHours().getOpenTime());
                            Date closeTime = format.parse(restaurant.getBusinessHours().getCloseTime());

                            long start = openTime.getTime(); // gio mo cua
                            long end = closeTime.getTime(); // gio dong cua

                            if (timeStartMorning <= current && current <= timeEndMorning){
                                if(timeStartMorning <= start && start <= timeEndMorning){
                                    arrayList2.add(restaurant);
                                }
                            }
                            else if (timeStartNoon <= current && current <= timeEndNoon){
                                if (timeStartMorning <= start && start <= timeEndNoon){
                                    arrayList2.add(restaurant);
                                }
                            }
                            else if (timeStartAfternoon <= current && current <= timeEndAfternoon){
                                if (timeStartMorning <= start || start <= timeEndAfternoon){
                                    arrayList2.add(restaurant);
                                }
                            }
                            else if (timeStartNight <= current && current <= timeEndNight){
                                if (timeStartMorning <= start || start <= timeEndNight){
                                    arrayList2.add(restaurant);
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                categories.add(new Category("Giỏ hàng của bạn", null, 2));
                categories.add(new Category("Khuyến mãi", arrayList1, 0));
                if (timeStartMorning <= current && current <= timeEndMorning) {
                    categories.add(new Category("Dành cho bữa sáng", arrayList2, 0));
                } else if (timeStartNoon <= current && current <= timeEndNoon) {
                    categories.add(new Category("Dành cho bữa trưa", arrayList2, 0));
                } else if (timeStartAfternoon <= current && current <= timeEndAfternoon) {
                    categories.add(new Category("Dành cho bữa chiều", arrayList2, 0));
                } else if(timeStartNight <= current && current <= timeEndNight){
                    categories.add(new Category("Dành cho bữa tối", arrayList2, 0));
                }
                categories.add(new Category("Dành cho bạn", arrayList3, 0));
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

    private void loadRestaurantRealtime(long time1, long time2){
        
    }
    private Date stringToDate(String aDate, String aFormat) {
        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;
    }

    private void initView(View view) {
        sRVListCategory = view.findViewById(R.id.shimmer_recycler_view_list_category);
        txtAddress = view.findViewById(R.id.text_view_address);
        spinner = view.findViewById(R.id.spinner);
        btnBook = view.findViewById(R.id.button_book);
        btnFavorite = view.findViewById(R.id.button_favorite);
        sliderView = view.findViewById(R.id.slider_view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("Restaurant");

        locationUtil = new LocationUtil(getContext());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        onItemCategoryClickListener = (OnItemCategoryClickListener) context;
    }
}
