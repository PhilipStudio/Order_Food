package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Food;

import io.realm.RealmResults;

public class FoodSavedAdapter extends RecyclerView.Adapter<FoodSavedAdapter.ViewHolder> {

    RealmResults<Food> realmResults;
    Context context;

    public FoodSavedAdapter(RealmResults<Food> realmResults, Context context) {
        this.realmResults = realmResults;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_saved, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgImage, imgAdd;
        TextView txtNameFood, txtNameRes, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgImage = itemView.findViewById(R.id.item_image_food);
            imgAdd = itemView.findViewById(R.id.item_add);
            txtNameFood = itemView.findViewById(R.id.item_name_food);
            txtPrice = itemView.findViewById(R.id.item_food_price);
            txtNameRes = itemView.findViewById(R.id.item_name_restaurant);
        }
    }
}
