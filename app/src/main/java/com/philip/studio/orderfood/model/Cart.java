package com.philip.studio.orderfood.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Cart extends RealmObject {
    @PrimaryKey
    private String productID;
    private String restaurantID;
    private String productName;
    private String productImage;
    private String quantity;
    private String price;

    public Cart() {
    }

    public Cart(String productID, String restaurantID, String productName, String productImage, String quantity, String price) {
        this.productID = productID;
        this.restaurantID = restaurantID;
        this.productName = productName;
        this.productImage = productImage;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public String getProductName() {
        return productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getProductImage() {
        return productImage;
    }
}
