package com.philip.studio.orderfood.model;

import java.util.ArrayList;

public class Category {
    private String nameCategory;
    private ArrayList<Restaurant> restaurants;
    private int viewType;

    public Category(String nameCategory, ArrayList<Restaurant> restaurants, int viewType) {
        this.nameCategory = nameCategory;
        this.restaurants = restaurants;
        this.viewType = viewType;
    }

    public ArrayList<Restaurant> getRestaurants() {
        return restaurants;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public int getViewType() {
        return viewType;
    }
}
