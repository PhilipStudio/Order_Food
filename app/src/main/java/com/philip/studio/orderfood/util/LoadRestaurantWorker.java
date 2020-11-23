package com.philip.studio.orderfood.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philip.studio.orderfood.model.Category;
import com.philip.studio.orderfood.model.Restaurant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LoadRestaurantWorker extends Worker {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dateRef = firebaseDatabase.getReference().child("Restaurant");

    ArrayList<Category> categories = new ArrayList<>();

    public LoadRestaurantWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String currentTime = format.format(new Date());
        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Restaurant> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    if (currentTime.equals("7:30")){
                        if (restaurant.getBusinessHours().getOpenTime().equals(currentTime) || restaurant.getBusinessHours().getCloseTime().equals(currentTime)){
                            arrayList.add(restaurant);
                        }
                        if (categories.size() != 0){
                            categories.clear();
                        }
                        categories.add(new Category("Dành cho bữa sáng", arrayList, 0));
                    }
                    else if (currentTime.equals("11:30")){
                        if (restaurant.getBusinessHours().getOpenTime().equals(currentTime) || restaurant.getBusinessHours().getCloseTime().equals(currentTime)){
                            arrayList.add(restaurant);
                        }
                        if (categories.size() != 0){
                            categories.clear();
                        }
                        categories.add(new Category("Dành cho bữa trưa", arrayList, 0));
                    }
                    else if (currentTime.equals("15:30")){
                        if (restaurant.getBusinessHours().getOpenTime().equals(currentTime) || restaurant.getBusinessHours().getCloseTime().equals(currentTime)){
                            arrayList.add(restaurant);
                        }
                        if (categories.size() != 0){
                            categories.clear();
                        }
                        categories.add(new Category("Dành cho bữa chiều", arrayList, 0));
                    }
                    else if (currentTime.equals("19:30")){
                        if (restaurant.getBusinessHours().getOpenTime().equals(currentTime) || restaurant.getBusinessHours().getCloseTime().equals(currentTime)){
                            arrayList.add(restaurant);
                        }
                        if (categories.size() != 0){
                            categories.clear();
                        }
                        categories.add(new Category("Dành cho bữa tối", arrayList, 0));
                    }
                    else if (currentTime.equals("23:30")){
                        if (restaurant.getBusinessHours().getOpenTime().equals(currentTime) || restaurant.getBusinessHours().getCloseTime().equals(currentTime)){
                            arrayList.add(restaurant);
                        }
                        if (categories.size() != 0){
                            categories.clear();
                        }
                        categories.add(new Category("Dành cho bữa khuya", arrayList, 0));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return Result.success();
    }
}
