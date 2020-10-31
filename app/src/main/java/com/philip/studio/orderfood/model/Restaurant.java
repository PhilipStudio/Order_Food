package com.philip.studio.orderfood.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Restaurant implements Parcelable {
    private String idRes;
    private String name;
    private String address;
    private Location location;
    private String logo;
    private String category;
    private String locationCategory;
    private BusinessHours businessHours;
    private float star;
    private PriceRange priceRange;
    private Promotion promotion;
    private int isBook;

    public Restaurant(){}

    protected Restaurant(Parcel in) {
        idRes = in.readString();
        name = in.readString();
        address = in.readString();
        location = (Location) in.readSerializable();
        logo = in.readString();
        category = in.readString();
        businessHours = (BusinessHours) in.readSerializable();
        star = in.readFloat();
        priceRange = (PriceRange) in.readSerializable();
        locationCategory = in.readString();
        promotion = (Promotion) in.readSerializable();
        isBook = in.readInt();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {

        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getIdRes() {
        return idRes;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLogo() {
        return logo;
    }

    public float getStar() {
        return star;
    }

    public BusinessHours getBusinessHours() {
        return businessHours;
    }

    public String getCategory() {
        return category;
    }

    public Location getLocation() {
        return location;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public String getLocationCategory() {
        return locationCategory;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getIsBook() {
        return isBook;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idRes);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeSerializable(location);
        dest.writeString(logo);
        dest.writeString(category);
        dest.writeSerializable(businessHours);
        dest.writeFloat(star);
        dest.writeSerializable(priceRange);
        dest.writeString(locationCategory);
        dest.writeSerializable(promotion);
        dest.writeInt(isBook);
    }
}
