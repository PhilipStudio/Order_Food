package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Restaurant;

import java.util.ArrayList;

import io.realm.RealmResults;

public class RestaurantSavedAdapter extends RecyclerView.Adapter<RestaurantSavedAdapter.ViewHolder> {

    ArrayList<Restaurant> arrayList;
    Context context;

    public RestaurantSavedAdapter(ArrayList<Restaurant> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant_saved, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtNameRes.setText(arrayList.get(position).getName());
        holder.txtAddress.setText(arrayList.get(position).getAddress());
        holder.txtTime.setText("Giở mở cửa: " +
                arrayList.get(position).getBusinessHours().getOpenTime() + " - " + arrayList.get(position).getBusinessHours().getCloseTime());
        float number = arrayList.get(position).getStar();
        holder.txtStars.setText(String.valueOf(number));

        Glide.with(context).load(arrayList.get(position).getLogo()).into(holder.imgFavorite);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgImage, imgFavorite;
        TextView txtNameRes, txtAddress, txtStars, txtTime, txtFavorite, txtNotification;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgImage = itemView.findViewById(R.id.item_logo);
            txtNameRes = itemView.findViewById(R.id.item_name_restaurant);
            txtAddress = itemView.findViewById(R.id.item_address);
            txtTime = itemView.findViewById(R.id.item_business_hours);
            txtStars = itemView.findViewById(R.id.item_stars);
            imgFavorite = itemView.findViewById(R.id.item_favorites);
            txtFavorite = itemView.findViewById(R.id.item_favorite_restaurant);
        }
    }
}
