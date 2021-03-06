package com.philip.studio.orderfood.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Menu {
    private String idMenu;
    private String name;
    private ArrayList<Food> foods;

    public Menu(){}

    public String getIdMenu() {
        return idMenu;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public String getName() {
        return name;
    }
}
