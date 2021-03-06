package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.RestaurantDetailActivity;
import com.philip.studio.orderfood.model.Restaurant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.FoodViewHolder> {

    ArrayList<Restaurant> arrayList;
    Context context;

    public RestaurantAdapter(ArrayList<Restaurant> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position).getLogo()).into(holder.imgImage);
        holder.txtName.setText(arrayList.get(position).getName());
        holder.txtAddress.setText(arrayList.get(position).getAddress());
        if (arrayList.get(position).getStar() >= 6.5){
            holder.txtFavorite.setVisibility(View.VISIBLE);
        }
        else{
            holder.txtFavorite.setVisibility(View.GONE);
        }

        if (arrayList.get(position).getPromotion().getIsPromotion() == 1){
            holder.txtFree.setVisibility(View.VISIBLE);
            holder.txtFree.setText(arrayList.get(position).getPromotion().getContent());
        }
        else{
            holder.txtFree.setVisibility(View.GONE);
        }

        Restaurant restaurant = arrayList.get(position);
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
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.txtNotification.setText("Hẹn giao vào lúc " + restaurant.getBusinessHours().getOpenTime());
            }
            else{
                holder.linearLayout.setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtAddress, txtFree, txtNotification, txtFavorite;
        ImageView imgImage;
        LinearLayout linearLayout;


        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.item_name);
            imgImage = itemView.findViewById(R.id.item_image);
            txtAddress = itemView.findViewById(R.id.item_address);
            txtFree = itemView.findViewById(R.id.item_free);
            linearLayout = itemView.findViewById(R.id.item_layout);
            txtNotification = itemView.findViewById(R.id.item_notification);
            txtFavorite = itemView.findViewById(R.id.item_favorite_restaurant);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                Intent intent = new Intent(context, RestaurantDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("restaurant", arrayList.get(pos));
                context.startActivity(intent);
            });
        }

    }
}
