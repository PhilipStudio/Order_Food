package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.FoodDetailActivity;
import com.philip.studio.orderfood.callback.OnItemFoodClickListener;
import com.philip.studio.orderfood.model.Cart;
import com.philip.studio.orderfood.model.Food;
import com.philip.studio.orderfood.model.Restaurant;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    ArrayList<Food> arrayList;
    Context context;
    String idRes;

    OnItemFoodClickListener onItemFoodClickListener;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dataRef = firebaseDatabase.getReference().child("Restaurant");

    public FoodAdapter(ArrayList<Food> arrayList, Context context, String idRes) {
        this.arrayList = arrayList;
        this.context = context;
        this.idRes = idRes;
    }

    public void setOnItemFoodClickListener(OnItemFoodClickListener onItemFoodClickListener) {
        this.onItemFoodClickListener = onItemFoodClickListener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position).getImage()).into(holder.imgImage);
        holder.txtName.setText(arrayList.get(position).getName());
        NumberFormat formatter = new DecimalFormat("#,###");
        double price = arrayList.get(position).getPrice();
        String formattedPrice = formatter.format(price);
        holder.txtPrice.setText(formattedPrice + "đ");
        holder.numberButton.setOnClickListener((ElegantNumberButton.OnClickListener) view -> {
            if (onItemFoodClickListener != null) {
                dataRef.child(idRes).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Restaurant restaurant = snapshot.getValue(Restaurant.class);
                        if (restaurant != null) {
                            Cart cart = new Cart(arrayList.get(position).getId(),
                                    idRes,
                                    restaurant.getName(),
                                    restaurant.getAddress(),
                                    arrayList.get(position).getName(),
                                    arrayList.get(position).getImage(),
                                    holder.numberButton.getNumber(),
                                    String.valueOf(arrayList.get(position).getPrice())
                            );
                            onItemFoodClickListener.onItemClick(cart);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPrice;
        ImageView imgImage;
        ElegantNumberButton numberButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.item_food_name);
            imgImage = itemView.findViewById(R.id.item_food_image);
            txtPrice = itemView.findViewById(R.id.item_food_price);
            numberButton = itemView.findViewById(R.id.item_number_button);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                Intent intent = new Intent(context, FoodDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putInt("position", pos);
                bundle.putParcelableArrayList("foods", arrayList);
                intent.putExtra("data", bundle);
                context.startActivity(intent);
            });
        }
    }
}
