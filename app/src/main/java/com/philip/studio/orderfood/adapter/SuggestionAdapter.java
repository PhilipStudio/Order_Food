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
import com.philip.studio.orderfood.model.Food;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

    ArrayList<Food> arrayList;
    Context context;
    OnItemSuggestionClickListener onItemSuggestionClickListener;

    public SuggestionAdapter(ArrayList<Food> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public void setOnItemSuggestionClickListener(OnItemSuggestionClickListener onItemSuggestionClickListener) {
        this.onItemSuggestionClickListener = onItemSuggestionClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtFoodName.setText(arrayList.get(position).getName());
        Glide.with(context).load(arrayList.get(position).getImage()).into(holder.imgImage);

        NumberFormat formatter = new DecimalFormat("#,###");
        double price = arrayList.get(position).getPrice();
        String formattedPrice = formatter.format(price);
        holder.txtFoodPrice.setText(formattedPrice + "đ");
        double favorite = arrayList.get(position).getLike();
        if (favorite > 100){
            holder.txtFoodLike.setText("100+");
        }
        else{
            holder.txtFoodLike.setText(String.valueOf(arrayList.get(position).getLike()));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtFoodName, txtFoodPrice, txtFoodLike;
        ImageView imgImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFoodName = itemView.findViewById(R.id.item_name);
            txtFoodPrice = itemView.findViewById(R.id.item_price);
            imgImage = itemView.findViewById(R.id.item_image);
            txtFoodLike = itemView.findViewById(R.id.item_like);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (onItemSuggestionClickListener != null){
                    onItemSuggestionClickListener.onItemClick(arrayList.get(pos));
                }
            });
        }
    }

    public interface OnItemSuggestionClickListener{
        void onItemClick(Food data);
    }
}
