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
import com.philip.studio.orderfood.activity.BookDetailActivity;
import com.philip.studio.orderfood.model.Restaurant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RestaurantBookAdapter extends RecyclerView.Adapter<RestaurantBookAdapter.ViewHolder>{

    ArrayList<Restaurant> arrayList;
    Context context;

    public RestaurantBookAdapter(ArrayList<Restaurant> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position).getLogo()).into(holder.imgImage);
        holder.txtAddress.setText(arrayList.get(position).getAddress());
        holder.txtName.setText(arrayList.get(position).getName());
        holder.txtCategory.setText(arrayList.get(position).getLocationCategory());
        if (arrayList.get(position).getPromotion().getIsPromotion() == 1){
            holder.txtFree.setText(arrayList.get(position).getPromotion().getContent());
        }
        else{
            holder.txtFree.setVisibility(View.GONE);
        }

        if (arrayList.get(position).getStar() >= 6.5){
            holder.txtFavorite.setVisibility(View.VISIBLE);
        }
        else{
            holder.txtFavorite.setVisibility(View.GONE);
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgImage;
        TextView txtName, txtAddress, txtCategory, txtTime, txtFree, txtFavorite,
                txtNotification;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgImage = itemView.findViewById(R.id.item_image);
            txtAddress = itemView.findViewById(R.id.item_address);
            txtName = itemView.findViewById(R.id.item_name_restaurant);
            txtCategory = itemView.findViewById(R.id.item_category);
            txtTime = itemView.findViewById(R.id.item_time);
            txtFree = itemView.findViewById(R.id.item_free);
            txtFavorite = itemView.findViewById(R.id.item_favorite_restaurant);
            linearLayout = itemView.findViewById(R.id.item_layout);
            txtNotification = itemView.findViewById(R.id.item_notification);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data", arrayList.get(pos));
                context.startActivity(intent);
            });
        }
    }
}
