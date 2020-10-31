package com.philip.studio.orderfood.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikelau.views.shimmer.ShimmerRecyclerViewX;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.adapter.MenuAdapter;
import com.philip.studio.orderfood.model.Menu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DeliveryFragment extends Fragment {

    ShimmerRecyclerViewX sRVListMenu;
    TextView txtBusinessHours, txtTotal, txtStatus;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataMenuRef;

    String idRes, startTime, endTime, businessHours;

    public DeliveryFragment(String idRes, String startTime, String endTime) {
        this.idRes = idRes;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);

        initView(view);

        businessHours = startTime + " - " + endTime;
        txtBusinessHours.setText(businessHours);

        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String currentTime = format.format(new Date());
            Date currentDateTime = format.parse(currentTime);
            Date startDateTime = format.parse(startTime);
            Date endDateTime = format.parse(endTime);

            long current = currentDateTime.getTime();
            long start = startDateTime.getTime();
            long end = endDateTime.getTime();

            if (start < current && current < end){
                txtStatus.setTextColor(Color.GREEN);
                txtStatus.setText("Đang mở cửa");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setUpRecyclerViewListFood();
        return view;
    }

    private void setUpRecyclerViewListFood() {
        sRVListMenu.showShimmerAdapter();
        sRVListMenu.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), ShimmerRecyclerViewX.VERTICAL, false);
        sRVListMenu.setLayoutManager(layoutManager);
        loadMenu();
    }

    private void loadMenu() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataMenuRef = firebaseDatabase.getReference().child("Menu");
        dataMenuRef.child(idRes).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Menu> menuArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Menu menu = dataSnapshot.getValue(Menu.class);
                    menuArrayList.add(menu);
                }

                MenuAdapter menuAdapter = new MenuAdapter(menuArrayList, getContext(), idRes);
                sRVListMenu.setAdapter(menuAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView(View view) {
        sRVListMenu = view.findViewById(R.id.shimmer_recycler_view_list_menu);
        txtBusinessHours = view.findViewById(R.id.text_view_business_hours);
        txtTotal = view.findViewById(R.id.text_view_total);
        txtStatus = view.findViewById(R.id.text_view_status);
    }
//
//    private void setUpRecyclerView(final Realm realm) {
//        rVListOrder.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        rVListOrder.setLayoutManager(layoutManager);
//
//        results = realm.where(Cart.class).findAll();
//        adapter = new CartAdapter(results, getContext());
//        rVListOrder.setAdapter(adapter);
//
//        adapter.setClickListener(pos -> realm.executeTransaction(realm1 -> {
//            RealmResults<Cart> realmResults = realm1.where(Cart.class)
//                    .equalTo("productID", results.get(pos).getProductID()).findAll();
//            realmResults.deleteAllFromRealm();
//            adapter.notifyDataSetChanged();
//            displayTotal(results);
//        }));
//
//        adapter.setUpdateItemCartListener((pos, num) -> realm.executeTransaction(realm12 -> {
//            RealmResults<Cart> cartRealmResults = realm12.where(Cart.class)
//                    .equalTo("productID", results.get(pos).getProductID())
//                    .findAll();
//            cartRealmResults.get(0).setQuantity(num);
//            adapter.notifyDataSetChanged();
//            displayTotal(results);
//        }));
//    }
//
//    private void displayTotal(RealmResults<Cart> cartRealmResults) {
//        int total = 0;
//        Locale locale = new Locale("en", "US");
//        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
//        for (Cart cart : cartRealmResults) {
//            total += Integer.parseInt(cart.getPrice()) * Integer.parseInt(cart.getQuantity());
//        }
//        txtTotal.setText(numberFormat.format(total));
//    }
}
