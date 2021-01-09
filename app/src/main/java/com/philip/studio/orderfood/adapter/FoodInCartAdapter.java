package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.FoodDetailActivity;
import com.philip.studio.orderfood.model.Cart;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class FoodInCartAdapter extends RecyclerView.Adapter<FoodInCartAdapter.ViewHolder> {

    ArrayList<Cart> arrayList;
    Context context;

    Realm realm;
    public FoodInCartAdapter(ArrayList<Cart> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_in_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Realm.init(context);
        Glide.with(context).load(arrayList.get(position).getProductImage()).into(holder.imageView);
        holder.txtName.setText(arrayList.get(position).getProductName());
        holder.numberButton.setNumber(arrayList.get(position).getQuantity());
        NumberFormat formatter = new DecimalFormat("#,###");
        double price = Double.parseDouble(arrayList.get(position).getPrice());
        String formattedPrice = formatter.format(price);
        holder.txtPrice.setText(formattedPrice + "đ");

        realm = Realm.getDefaultInstance();
        holder.numberButton.setOnClickListener((ElegantNumberButton.OnClickListener) view -> {
            realm.executeTransaction(realm -> {
                Cart cart = realm.where(Cart.class).equalTo("productID", arrayList.get(position).getProductID()).findFirst();
                cart.setQuantity(holder.numberButton.getNumber());
                realm.insertOrUpdate(cart);
            });
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView txtName, txtPrice;
        ElegantNumberButton numberButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_image);
            txtName = itemView.findViewById(R.id.item_name);
            txtPrice = itemView.findViewById(R.id.item_price);
            numberButton = itemView.findViewById(R.id.item_number_button);
        }
    }

}
