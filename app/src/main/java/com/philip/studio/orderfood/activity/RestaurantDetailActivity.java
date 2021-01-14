package com.philip.studio.orderfood.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.abdularis.civ.AvatarImageView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.adapter.CartAdapter;
import com.philip.studio.orderfood.fragment.CommentFragment;
import com.philip.studio.orderfood.fragment.DeliveryFragment;
import com.philip.studio.orderfood.fragment.InformationRestaurantFragment;
import com.philip.studio.orderfood.model.Cart;
import com.philip.studio.orderfood.model.Comment;
import com.philip.studio.orderfood.model.Menu;
import com.philip.studio.orderfood.model.Restaurant;
import com.philip.studio.orderfood.model.User;
import com.philip.studio.orderfood.util.UserUtil;

import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.nikartm.support.ImageBadgeView;

public class RestaurantDetailActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    TextView txtName, txtAddress, txtStar, txtCategory, txtPriceRange;
    BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView imgLogo, imgEvaluate, imgMenu;
    ImageBadgeView imageBadgeView;


    Restaurant restaurant;
    String time, total;
    UserUtil userUtil;
    Realm realm;
    RealmResults<Cart> realmResults;
    int stars = 0, dem = 0;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataCommentRef, dataResRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        Realm.init(this);

        initView();

        Intent intent = getIntent();
        if (intent != null) {
            restaurant = intent.getParcelableExtra("restaurant");
            txtName.setText(restaurant.getName());
            txtAddress.setText(restaurant.getAddress());

            time = restaurant.getBusinessHours().getOpenTime() + " AM" + " - " + restaurant.getBusinessHours().getCloseTime() + " PM";
            txtCategory.setText(restaurant.getCategory());

            Glide.with(this).load(restaurant.getLogo()).into(imgLogo);
            txtStar.setText(String.valueOf(restaurant.getStar()));

            NumberFormat formatter = new DecimalFormat("#,###");
            double starPrice = restaurant.getPriceRange().getStartPrice();
            String formattedStartPrice = formatter.format(starPrice);

            double endPrice = restaurant.getPriceRange().getEndPrice();
            String formattedEndPrice = formatter.format(endPrice);

            String priceRange = "Giá : " + formattedStartPrice + " - " + formattedEndPrice;
            txtPriceRange.setText(priceRange);
        }


        User user = userUtil.getUser();
        imgEvaluate.setOnClickListener(v -> showDialogFavorite(restaurant, user));

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String currentTime = format.format(new Date());
            Date currentDateTime = format.parse(currentTime);
            Date startDateTime = format.parse(restaurant.getBusinessHours().getOpenTime());
            Date endDateTime = format.parse(restaurant.getBusinessHours().getCloseTime());

            long current = currentDateTime.getTime();
            long start = startDateTime.getTime();
            long end = endDateTime.getTime();

            if (current < start || current > end) {
                showAlertDialogNotification();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Realm realm = Realm.getDefaultInstance();
        realmResults = realm.where(Cart.class).equalTo("restaurantID", restaurant.getIdRes()).findAll();
        imageBadgeView.setBadgeValue(realmResults.size());

        imageBadgeView.setOnClickListener(v -> {
            if (realmResults.size() != 0) {
                showDialogListCart(RestaurantDetailActivity.this, realmResults);
            } else {
                Toast.makeText(this, "Bạn chưa thêm gì vào trong giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
        imgEvaluate.setOnClickListener(v -> showDialogFavorite(restaurant, user));

        imgMenu.setOnClickListener(v -> showListMenu(restaurant.getIdRes(), imgMenu));
    }

    @Override
    protected void onPause() {
        super.onPause();
        imageBadgeView.setBadgeValue(dem);
    }

    @Override
    protected void onResume() {
        super.onResume();

        imageBadgeView.setBadgeValue(dem);
    }

    private void showListMenu(String idRes, View view) {
        DatabaseReference dataRef = firebaseDatabase.getReference().child("Menu");
        dataRef.child(idRes).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> listName = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Menu menu = dataSnapshot.getValue(Menu.class);
                    listName.add(menu.getName() + " (" + menu.getFoods().size() + ")");
                }

                createPopupMenu(RestaurantDetailActivity.this, view, listName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createPopupMenu(Context context, View anchorView, ArrayList<String> arrayList) {
        PopupMenu popupMenu = new PopupMenu(context, anchorView);
        android.view.Menu menu = popupMenu.getMenu();

        for (int i = 0; i < arrayList.size(); i++) {
            menu.add(i, i, i, arrayList.get(i));
        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Subscribe
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    private void showAlertDialogNotification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông báo từ cửa hàng");
        builder.setMessage("Cửa hàng đã không còn phục vụ tại thời điểm này. Nếu muốn bạn có thể quay lại sớm nhất vào ngày mai");

        builder.setPositiveButton("OK", (dialog, which) -> finish());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialogListCart(Context context, RealmResults<Cart> realmResults) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_cart_dialog);

        dialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);

        ImageView imgClose = dialog.findViewById(R.id.image_view_close);
        RecyclerView rvListCart = dialog.findViewById(R.id.recycler_view_list_cart);
        TextView txtRemoveAll, txtTotal;
        txtRemoveAll = dialog.findViewById(R.id.text_view_remove_all);
        txtTotal = dialog.findViewById(R.id.text_view_total);
        Button btnDelivery;
        btnDelivery = dialog.findViewById(R.id.button_delivery);

        imgClose.setOnClickListener(v -> dialog.cancel());

        rvListCart.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvListCart.setLayoutManager(layoutManager);

        CartAdapter cartAdapter = new CartAdapter(realmResults, context);
        rvListCart.setAdapter(cartAdapter);

        realm = Realm.getDefaultInstance();
        cartAdapter.setUpdateItemCartListener((pos, num) -> realm.executeTransaction(realm -> {
            RealmResults<Cart> cartRealmResults = realm.where(Cart.class)
                    .equalTo("productID", realmResults.get(pos).getProductID())
                    .findAll();
            cartRealmResults.get(0).setQuantity(num);
            cartAdapter.notifyDataSetChanged();
            total = displayTotalOrder(realmResults);
            txtTotal.setText(total);
        }));

        total = displayTotalOrder(realmResults);
        txtTotal.setText(total);

        txtRemoveAll.setOnClickListener(v -> {
            Realm realm1 = Realm.getDefaultInstance();
            realm1.executeTransaction(realm -> {
                RealmResults<Cart> results = realm.where(Cart.class).equalTo("restaurantID", restaurant.getIdRes()).findAll();
                results.deleteAllFromRealm();
                cartAdapter.notifyDataSetChanged();
                total = displayTotalOrder(realmResults);
                txtTotal.setText(total);
            });
            displayTotalOrder(realmResults);
            dialog.cancel();
            imageBadgeView.setBadgeValue(0);
        });

        ArrayList<Cart> arrayList = new ArrayList<>(realmResults);

        btnDelivery.setOnClickListener(v -> {
            Intent intent = new Intent(RestaurantDetailActivity.this, OrderActivity.class);
            intent.putExtra("restaurant", restaurant);
            intent.putExtra("list", arrayList);
            startActivity(intent);
            dialog.cancel();
        });

        dialog.show();
    }

    private void showDialogFavorite(Restaurant restaurant, User user) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog_favorite);

        ImageView imgRes, imgSend;
        EditText edtComment;
        AvatarImageView avatarImageView;
        RatingBar ratingBar;
        TextView txtStatus;

        imgRes = dialog.findViewById(R.id.image_view_restaurant);
        edtComment = dialog.findViewById(R.id.edit_text_comment);
        imgSend = dialog.findViewById(R.id.image_view_send);
        avatarImageView = dialog.findViewById(R.id.avatar_image_view);
        ratingBar = dialog.findViewById(R.id.rating_bar);
        txtStatus = dialog.findViewById(R.id.text_view_status);

        dataCommentRef.child(restaurant.getIdRes()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Comment comment = snapshot.getValue(Comment.class);
                if (comment != null) {
                    ratingBar.setRating((float) comment.getStar());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        txtStatus.setVisibility(View.GONE);

        if (TextUtils.isEmpty(user.getAvatar()) || user.getAvatar().equals(" ")) {
            avatarImageView.setState(AvatarImageView.SHOW_INITIAL);
            avatarImageView.setText(user.getName());
        } else {
            avatarImageView.setState(AvatarImageView.SHOW_IMAGE);
            Glide.with(this).load(user.getAvatar()).into(avatarImageView);
        }

        Glide.with(this).load(restaurant.getLogo()).into(imgRes);

        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            switch ((int) ratingBar1.getRating()) {
                case 1:
                    stars = 1;
                    txtStatus.setVisibility(View.VISIBLE);
                    txtStatus.setText("Rất tệ");
                    break;
                case 2:
                    stars = 2;
                    txtStatus.setVisibility(View.VISIBLE);
                    txtStatus.setText("Tệ");
                    break;
                case 3:
                    stars = 3;
                    txtStatus.setVisibility(View.VISIBLE);
                    txtStatus.setText("Trung bình");
                    break;
                case 4:
                    stars = 4;
                    txtStatus.setVisibility(View.VISIBLE);
                    txtStatus.setText("Tốt");
                    break;
                case 5:
                    stars = 5;
                    txtStatus.setVisibility(View.VISIBLE);
                    txtStatus.setText("Tuyệt vời");
                    break;
            }
        });

        imgSend.setOnClickListener(v -> {
            String content = edtComment.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(RestaurantDetailActivity.this, "Content comment is null", Toast.LENGTH_SHORT).show();
            } else {
                String dateTime = new SimpleDateFormat("HH:mm dd/MM/yyyy").format(new Date());
                String idComment = String.valueOf(System.currentTimeMillis());
                Comment comment = new Comment("1", user.getAvatar(), user.getName(), content, dateTime, stars);
                writeCommentForRestaurant(restaurant.getIdRes(), idComment, comment);
                dialog.cancel();
            }
        });

        dialog.show();
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
                    return new DeliveryFragment(restaurant.getIdRes(), restaurant.getBusinessHours().getOpenTime(), restaurant.getBusinessHours().getCloseTime());
                case 1:
                    return new CommentFragment(restaurant.getIdRes(), restaurant.getStar());
                case 2:
                    return new InformationRestaurantFragment(restaurant.getAddress(), restaurant.getLocation());
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Giao hàng";
                case 1:
                    return "Bình luận";
                case 2:
                    return "Thông tin";
            }
            return null;
        }
    }

    private void writeCommentForRestaurant(String idRes, String idComment, Comment comment) {
        dataCommentRef.child(idRes).child(idComment).setValue(comment);
    }

    private String displayTotalOrder(RealmResults<Cart> realmResults) {
        double total = 0;
        for (int i = 0; i < realmResults.size(); i++) {
            double a = Double.parseDouble(realmResults.get(i).getPrice()) * Double.parseDouble(realmResults.get(i).getQuantity());
            total = total + a;
        }

        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(total) + "đ";
    }

    private void initView() {
        linearLayout = findViewById(R.id.linear_layout);
        txtName = findViewById(R.id.text_view_name_restaurant);
        txtAddress = findViewById(R.id.text_view_address_restaurant);
        txtCategory = findViewById(R.id.text_view_category_restaurant);
        txtStar = findViewById(R.id.text_view_stars_restaurant);
        imgLogo = findViewById(R.id.image_view_logo);
        viewPager = findViewById(R.id.view_pager_container);
        tabLayout = findViewById(R.id.tab_layout);
        txtPriceRange = findViewById(R.id.text_view_price_range);
        imageBadgeView = findViewById(R.id.image_badge_view_shopping_cart);
        imgEvaluate = findViewById(R.id.image_view_evaluate);
        imgMenu = findViewById(R.id.image_view_menu);

        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataCommentRef = firebaseDatabase.getReference().child("Comment");
        dataResRef = firebaseDatabase.getReference().child("Restaurant");

        userUtil = new UserUtil(this);
    }
}