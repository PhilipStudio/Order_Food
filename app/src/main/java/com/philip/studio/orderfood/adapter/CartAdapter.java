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
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Cart;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import io.realm.RealmResults;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    RealmResults<Cart> realmResults;
    Context context;
    OnCartItemClickListener clickListener;
    OnUpdateItemCartListener updateItemCartListener;

    public CartAdapter(RealmResults<Cart> realmResults, Context context) {
        this.realmResults = realmResults;
        this.context = context;
    }

    public void setClickListener(OnCartItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setUpdateItemCartListener(OnUpdateItemCartListener updateItemCartListener) {
        this.updateItemCartListener = updateItemCartListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        holder.txtFoodName.setText(realmResults.get(position).getProductName());
        holder.numberButton.setNumber(realmResults.get(position).getQuantity());
        Glide.with(context).load(realmResults.get(position).getProductImage()).into(holder.imageView);

        String total = displayTotal(realmResults.get(position).getPrice(), realmResults.get(position).getQuantity()) + "đ";
        holder.txtFoodPrice.setText(total);

        holder.numberButton.setOnClickListener((ElegantNumberButton.OnClickListener) view -> {
            String data = holder.numberButton.getNumber();
            if (updateItemCartListener != null) {
                updateItemCartListener.onUpdateItem(position, data);
            }
        });
    }

    public int getItemCount() {
        return realmResults.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView txtFoodName, txtFoodPrice;
        ElegantNumberButton numberButton;
        ImageView imageView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFoodName = itemView.findViewById(R.id.item_food_name);
            txtFoodPrice = itemView.findViewById(R.id.item_total);
            numberButton = itemView.findViewById(R.id.item_number_button);
            imageView = itemView.findViewById(R.id.item_food_image);
        }
    }

    private String displayTotal(String str1, String str2) {
        NumberFormat formatter = new DecimalFormat("#,###");
        double price = Double.parseDouble(str1) * Double.parseDouble(str2);
        return formatter.format(price);
    }

    public interface OnCartItemClickListener {
        void onItemClick(int pos);
    }

    public interface OnUpdateItemCartListener {
        void onUpdateItem(int pos, String num);
    }
}
