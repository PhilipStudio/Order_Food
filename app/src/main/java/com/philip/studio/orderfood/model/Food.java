package com.philip.studio.orderfood.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@IgnoreExtraProperties
public class Food implements Parcelable {
    @PrimaryKey
    private String id;
    private String name, image;
    private double price, like;

    public Food() {
    }

    public Food(String id, String name, String image, double price) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.id = id;
    }

    protected Food(Parcel in) {
        name = in.readString();
        image = in.readString();
        id = in.readString();
        price = in.readDouble();
        like = in.readDouble();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public double getLike() {
        return like;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(id);
        dest.writeDouble(price);
        dest.writeDouble(like);
    }
}
