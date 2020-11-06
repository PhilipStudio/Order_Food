package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.abdularis.civ.AvatarImageView;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Cart;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import io.realm.RealmResults;

public class YourCartAdapter extends RecyclerView.Adapter<YourCartAdapter.ViewHolder> {

    RealmResults<Cart> realmResults;
    Context context;

    public YourCartAdapter(RealmResults<Cart> realmResults, Context context) {
        this.realmResults = realmResults;
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
        Glide.with(context).load(realmResults.get(position).getProductImage()).into(holder.avatarImageView1);
        Glide.with(context).load(realmResults.get(position).getProductImage()).into(holder.avatarImageView2);
        Glide.with(context).load(realmResults.get(position).getProductImage()).into(holder.avatarImageView3);

        String total = displayTotalOrder(realmResults);
        holder.txtTotal.setText(total);
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
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
