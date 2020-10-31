package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mikelau.views.shimmer.ShimmerRecyclerViewX;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Food;
import com.philip.studio.orderfood.model.Menu;

import java.util.ArrayList;

import io.realm.Realm;

public class MenuAdapter extends ShimmerRecyclerViewX.Adapter<MenuAdapter.ViewHolder> {

    ArrayList<Menu> arrayList;
    Context context;
    String idRes;

    Realm realm = Realm.getDefaultInstance();

    public MenuAdapter(ArrayList<Menu> arrayList, Context context, String idRes) {
        this.arrayList = arrayList;
        this.context = context;
        this.idRes = idRes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtNameMenu.setText(arrayList.get(position).getName());
        holder.sRVListFood.showShimmerAdapter();
        holder.sRVListFood.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, ShimmerRecyclerViewX.VERTICAL, false);
        holder.sRVListFood.setLayoutManager(layoutManager);

        ArrayList<Food> foods = arrayList.get(position).getFoods();
        FoodAdapter foodAdapter = new FoodAdapter(foods, context, idRes);
        holder.sRVListFood.setAdapter(foodAdapter);
        foodAdapter.setOnItemFoodClickListener(cart -> {
            realm.executeTransaction(realm2 -> {
                realm2.insert(cart);
                Toast.makeText(context, "Insert food success", Toast.LENGTH_SHORT).show();
            });
        });

        if (position == 0) {
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(context, ShimmerRecyclerViewX.HORIZONTAL, false);
            holder.sRVListFood.setLayoutManager(layoutManager1);
            FoodFavoriteAdapter foodFavoriteAdapter = new FoodFavoriteAdapter(foods, context, idRes);
            holder.sRVListFood.setAdapter(foodFavoriteAdapter);

            foodFavoriteAdapter.setOnItemFoodClickListener(cart -> {
                realm.executeTransaction(realm1 -> {
                    realm1.insert(cart);
                    Toast.makeText(context, "Insert favorite success", Toast.LENGTH_SHORT).show();
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends ShimmerRecyclerViewX.ViewHolder {

        TextView txtNameMenu;
        ShimmerRecyclerViewX sRVListFood;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameMenu = itemView.findViewById(R.id.item_name_menu);
            sRVListFood = itemView.findViewById(R.id.item_list_food);
        }
    }
}
