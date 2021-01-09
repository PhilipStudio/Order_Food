package com.philip.studio.orderfood.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Cart extends RealmObject implements Parcelable {
    @PrimaryKey
    private String productID;
    private String restaurantID;
    private String nameRestaurant;
    private String address;
    private String productName;
    private String productImage;
    private String quantity;
    private String price;

    public Cart() {
    }

    public Cart(String productID, String restaurantID, String nameRestaurant, String address, String productName, String productImage, String quantity, String price) {
        this.productID = productID;
        this.restaurantID = restaurantID;
        this.nameRestaurant = nameRestaurant;
        this.address = address;
        this.productName = productName;
        this.productImage = productImage;
        this.quantity = quantity;
        this.price = price;
    }

    protected Cart(Parcel in) {
        productID = in.readString();
        restaurantID = in.readString();
        nameRestaurant = in.readString();
        address = in.readString();
        productName = in.readString();
        productImage = in.readString();
        quantity = in.readString();
        price = in.readString();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

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

    public String getAddress() {
        return address;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productID);
        dest.writeString(restaurantID);
        dest.writeString(nameRestaurant);
        dest.writeString(address);
        dest.writeString(productName);
        dest.writeString(productImage);
        dest.writeString(quantity);
        dest.writeString(price);
    }
}
