package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.mikelau.views.shimmer.ShimmerRecyclerViewX;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.callback.OnItemCategoryClickListener;
import com.philip.studio.orderfood.model.Category;
import com.philip.studio.orderfood.model.Restaurant;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MenuViewHolder> {

    ArrayList<Category> categories;
    Context context;
    OnItemCategoryClickListener onItemCategoryClickListener;

    public CategoryAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    public void setOnItemCategoryClickListener(OnItemCategoryClickListener onItemCategoryClickListener) {
        this.onItemCategoryClickListener = onItemCategoryClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return categories.get(position).getViewType();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_first, parent, false);
                return new MenuViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_second, parent, false);
                return new MenuViewHolder(view);

            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position) {
        switch (categories.get(position).getViewType()) {
            case 0:
                holder.txtNameCategory.setText(categories.get(position).getNameCategory());
                holder.sRVListRestaurant1.showShimmerAdapter();
                holder.sRVListRestaurant1.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                holder.sRVListRestaurant1.setLayoutManager(layoutManager);

                RestaurantAdapter menuAdapter = new RestaurantAdapter(categories.get(position).getRestaurants(), context);
                holder.sRVListRestaurant1.setAdapter(menuAdapter);

                holder.txtViewMore.setOnClickListener(v -> {
                    if (onItemCategoryClickListener != null) {
                        onItemCategoryClickListener.onItemClick(categories.get(position).getNameCategory());
                    }
                });
                break;
            case 1:
                loadListRestaurant(holder.sRVListRestaurant2, "Thức ăn nhanh", context);
                holder.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        switch (tab.getPosition()) {
                            case 0:
                                loadListRestaurant(holder.sRVListRestaurant2, "Thức ăn nhanh", context);
                                break;
                            case 1:
                                loadListRestaurant(holder.sRVListRestaurant2, "Đồ uống", context);
                                break;
                            case 2:
                                loadListRestaurant(holder.sRVListRestaurant2, "Món ăn Việt", context);
                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameCategory, txtViewMore;
        ShimmerRecyclerViewX sRVListRestaurant1, sRVListRestaurant2;
        TabLayout tabLayout;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameCategory = itemView.findViewById(R.id.item_name_category);
            txtViewMore = itemView.findViewById(R.id.item_view_more);
            sRVListRestaurant1 = itemView.findViewById(R.id.item_list);
            tabLayout = itemView.findViewById(R.id.tab_layout);
            sRVListRestaurant2 = itemView.findViewById(R.id.item_shimmer_recycler_view_list_restaurant);
        }
    }

    private void loadListRestaurant(ShimmerRecyclerViewX shimmerRecyclerViewX, String text, Context context) {
        shimmerRecyclerViewX.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, ShimmerRecyclerViewX.VERTICAL, false);
        shimmerRecyclerViewX.setLayoutManager(layoutManager);
        ArrayList<Restaurant> arrayList = categories.get(2).getRestaurants();
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        for (Restaurant restaurant : arrayList) {
            if (restaurant.getCategory().equals(text)) {
                restaurants.add(restaurant);
            }
        }

        RestaurantCategoryAdapter adapter = new RestaurantCategoryAdapter(restaurants, context);
        shimmerRecyclerViewX.setAdapter(adapter);
    }
}
