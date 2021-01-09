package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Cart;
import com.zerobranch.layout.SwipeLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import io.realm.Realm;
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

        holder.txtFoodPrice.setText(displayPrice(realmResults.get(position).getPrice()));

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
        ImageView imageView, imgDelete;
        SwipeLayout swipeLayout;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFoodName = itemView.findViewById(R.id.item_food_name);
            txtFoodPrice = itemView.findViewById(R.id.item_total);
            numberButton = itemView.findViewById(R.id.item_number_button);
            imageView = itemView.findViewById(R.id.item_food_image);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            imgDelete = itemView.findViewById(R.id.item_delete);

            imgDelete.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                String id = realmResults.get(pos).getProductID();
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(realm1 -> {
                    RealmResults<Cart> realmResults1 = realm.where(Cart.class).equalTo("productID", id).findAll();
                    if (realmResults1.size() != 0){
                        realmResults1.deleteFirstFromRealm();
                    }
                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                });
            });

            swipeLayout.setOnActionsListener(new SwipeLayout.SwipeActionsListener() {
                @Override
                public void onOpen(int direction, boolean isContinuous) {
                    if (direction == SwipeLayout.LEFT && isContinuous){
                        Toast.makeText(context, "Swipe left", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onClose() {

                }
            });
        }
    }

    private String displayPrice(String str) {
        NumberFormat formatter = new DecimalFormat("#,###");
        double price = Double.parseDouble(str);
        return formatter.format(price) + "đ";
    }

    public interface OnCartItemClickListener {
        void onItemClick(int pos);
    }

    public interface OnUpdateItemCartListener {
        void onUpdateItem(int pos, String num);
    }
}
