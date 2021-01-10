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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.callback.OnButtonPaymentClickListener;
import com.philip.studio.orderfood.fragment.InformationOrderFragment;
import com.philip.studio.orderfood.fragment.ListFoodFragment;
import com.philip.studio.orderfood.model.Cart;
import com.philip.studio.orderfood.model.Order;
import com.philip.studio.orderfood.model.Restaurant;
import com.philip.studio.orderfood.model.User;
import com.philip.studio.orderfood.util.LocationUtil;
import com.philip.studio.orderfood.util.UserUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import vn.momo.momo_partner.AppMoMoLib;

public class OrderActivity extends AppCompatActivity implements OnButtonPaymentClickListener {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView txtName, txtAddress, txtTime;
    ImageView imgChooseAddress;
    SwitchDateTimeDialogFragment dateTimeDialogFragment;

    ArrayList<Cart> arrayList;
    Restaurant restaurant;
    UserUtil userUtil;
    LocationUtil locationUtil;
    private static final int REQUEST_CODE = 123;
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    String address;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dataRef = firebaseDatabase.getReference().child("Order");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        initView();

        if (userUtil.getUser() != null) {
            User user = userUtil.getUser();
            txtName.setText(user.getName());
        }

        address = locationUtil.getNameUserLocation();
        if (!TextUtils.isEmpty(address)) {
            txtAddress.setText(address);
        } else {
            txtAddress.setText("Mời chọn địa chỉ nhận hàng");
        }

        String time = new SimpleDateFormat("HH:mm").format(new Date());
        String dateTime = new SimpleDateFormat("dd/MM").format(new Date());
        txtTime.setText("Giao ngay " + time + " - Hôm nay " + dateTime);

        txtTime.setOnClickListener(v -> showDateTimePicker());

        toolbar.setNavigationOnClickListener(v -> finish());
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        if (intent != null) {
            arrayList = intent.getParcelableArrayListExtra("list");
            restaurant = intent.getParcelableExtra("restaurant");
        }

        imgChooseAddress.setOnClickListener(v -> {
            Intent intent1 = new Intent(OrderActivity.this, MapsActivity.class);
            startActivityForResult(intent1, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE && data != null) {
                address = data.getStringExtra("address");
                txtAddress.setText(address);
            }
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

    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
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
            switch (position) {
                case 0:
                    return "Thông tin";
                case 1:
                    return "Món ăn";

            }
            return null;
        }
    }

    private void showDateTimePicker(){
        dateTimeDialogFragment = (SwitchDateTimeDialogFragment) getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if (dateTimeDialogFragment == null){
            dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance("Chọn thời gian", "Ok", "Cancel");
        }

        dateTimeDialogFragment.setTimeZone(TimeZone.getDefault());
        final SimpleDateFormat myDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        dateTimeDialogFragment.set24HoursMode(false);
        dateTimeDialogFragment.setHighlightAMPMSelection(false);
        dateTimeDialogFragment.setMinimumDateTime(new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime());
        dateTimeDialogFragment.setMaximumDateTime(new GregorianCalendar(2021, Calendar.DECEMBER, 31).getTime());

        try {
            dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd/MM", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.d("error", e.getLocalizedMessage());
        }

        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                txtTime.setText(myDateFormat.format(new Date()));
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                dateTimeDialogFragment.dismiss();
            }

            @Override
            public void onNeutralButtonClick(Date date) {

            }
        });

        dateTimeDialogFragment.startAtCalendarView();
        dateTimeDialogFragment.setDefaultDateTime(new GregorianCalendar(2021, Calendar.MAY, 1, 3, 14).getTime());
        dateTimeDialogFragment.show(getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
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

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        txtName = findViewById(R.id.text_view_name_user);
        txtAddress = findViewById(R.id.text_view_address);
        txtTime = findViewById(R.id.text_view_time);
        imgChooseAddress = findViewById(R.id.image_view_choose_address);

        arrayList = new ArrayList<>();
        userUtil = new UserUtil(this);
        locationUtil = new LocationUtil(this);
    }
}