package com.philip.studio.orderfood.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.callback.OnButtonPaymentClickListener;
import com.philip.studio.orderfood.fragment.InformationOrderFragment;
import com.philip.studio.orderfood.fragment.ListFoodFragment;
import com.philip.studio.orderfood.model.Cart;
import com.philip.studio.orderfood.model.Order;
import com.philip.studio.orderfood.model.Restaurant;

import java.util.ArrayList;

import vn.momo.momo_partner.AppMoMoLib;

public class OrderActivity extends AppCompatActivity implements OnButtonPaymentClickListener {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    ArrayList<Cart> arrayList;
    Restaurant restaurant;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dataRef = firebaseDatabase.getReference().child("Order");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        initView();

        toolbar.setNavigationOnClickListener(v -> finish());
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        if (intent != null){
            arrayList = intent.getParcelableArrayListExtra("list");
            restaurant = intent.getParcelableExtra("restaurant");
        }
    }

    @Override
    public void onPaymentSuccess(Order order) {
        showDialogPaymentSuccess(order);
    }

    @Override
    public void onPaymentFailure() {
        Toast.makeText(this, "Your payment failure :(((", Toast.LENGTH_SHORT).show();
    }



    class PagerAdapter extends FragmentPagerAdapter{

        public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new InformationOrderFragment(restaurant, arrayList);
                case 1:
                    return new ListFoodFragment(arrayList);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0: return "Thông tin";
                case 1: return "Món";

            }
            return null;
        }
    }

    private void showDialogPaymentSuccess(Order order) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog_paypal);

        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtAmount, txtPaymentID, txtStatus, txtNameRestaurant, txtAddress;
        txtAddress = dialog.findViewById(R.id.text_view_address);
        txtNameRestaurant = dialog.findViewById(R.id.text_view_name_restaurant);
        txtAmount = dialog.findViewById(R.id.text_view_amount);
        txtPaymentID = dialog.findViewById(R.id.text_view_payment_id);
        txtStatus = dialog.findViewById(R.id.text_view_status);

        txtNameRestaurant.setText(restaurant.getName());
        txtAddress.setText(restaurant.getAddress());
        String paymentID = order.getPaymentId();
        txtPaymentID.setText(paymentID);
        txtAmount.setText("$" + order.getTotal());
        txtStatus.setText(order.getStatus());

        dataRef.child(order.getIdOrder()).setValue(order);
        dialog.show();
    }

    private void initView(){
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        arrayList = new ArrayList<>();
    }
}