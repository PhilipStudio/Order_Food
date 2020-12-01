package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.abdularis.civ.AvatarImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.RestaurantDetailActivity;
import com.philip.studio.orderfood.model.Cart;
import com.philip.studio.orderfood.model.Restaurant;
import com.philip.studio.orderfood.model.Saved;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import io.realm.RealmResults;

public class YourCartAdapter extends RecyclerView.Adapter<YourCartAdapter.ViewHolder> {

    ArrayList<Saved> arrayList;
    Context context;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dataRef = firebaseDatabase.getReference().child("Restaurant");

    public YourCartAdapter(ArrayList<Saved> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_your_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(arrayList.get(position).getName());

        switch (arrayList.get(position).getRealmResults().size()){
            case 1:
                Glide.with(context).load(arrayList.get(position).getRealmResults().get(0).getProductImage()).into(holder.avatarImageView1);
                holder.avatarImageView2.setVisibility(View.GONE);
                holder.avatarImageView3.setVisibility(View.GONE);
                break;
            case 2:
                Glide.with(context).load(arrayList.get(position).getRealmResults().get(0).getProductImage()).into(holder.avatarImageView1);
                Glide.with(context).load(arrayList.get(position).getRealmResults().get(1).getProductImage()).into(holder.avatarImageView2);
                holder.avatarImageView3.setVisibility(View.GONE);
                break;
            default:
                Glide.with(context).load(arrayList.get(position).getRealmResults().get(0).getProductImage()).into(holder.avatarImageView1);
                Glide.with(context).load(arrayList.get(position).getRealmResults().get(1).getProductImage()).into(holder.avatarImageView2);
                Glide.with(context).load(arrayList.get(position).getRealmResults().get(2).getProductImage()).into(holder.avatarImageView3);
                break;
        }

        String total = displayTotalOrder(arrayList.get(position).getRealmResults());
        holder.txtTotal.setText(total);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtName, txtAddress, txtTotal;
        AvatarImageView avatarImageView1, avatarImageView2, avatarImageView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.item_name_restaurant);
            txtAddress = itemView.findViewById(R.id.item_address_restaurant);
            txtTotal = itemView.findViewById(R.id.item_total);
            avatarImageView1 = itemView.findViewById(R.id.item_avatar_image_view_one);
            avatarImageView2 = itemView.findViewById(R.id.item_avatar_image_view_two);
            avatarImageView3 = itemView.findViewById(R.id.item_avatar_image_view_three);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                dataRef.child(arrayList.get(pos).getRealmResults().get(0).getRestaurantID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Restaurant restaurant = snapshot.getValue(Restaurant.class);
                        Intent intent = new Intent(context, RestaurantDetailActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("restaurant", restaurant);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });
        }
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
}
