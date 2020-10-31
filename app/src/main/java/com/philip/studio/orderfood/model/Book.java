package com.philip.studio.orderfood.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Book implements Parcelable {
    private Restaurant restaurant;//Dat ban tai nha hang nao
    private String dateTime;//Thoi gian dat ban
    private String image;
    private int numberPerson;//So luong nguoi
    private int isPromotion; //Nha hang co chuong trinh giam gia hay khong ?
    private double discount; //So phan tram giam gia
    private String event; //Dat ban cho su kien gi
    private String content; //Noi dung chuong trinh giam gia

    //Vi du dat mot ban an tai nha hang A gom 10 cho su kien sinh nhat tu 20:30 - 22:30
    public Book() {
    }

    public Book(Restaurant restaurant, String dateTime, String image, int numberPerson, int isPromotion, double discount, String event, String content) {
        this.restaurant = restaurant;
        this.dateTime = dateTime;
        this.image = image;
        this.numberPerson = numberPerson;
        this.isPromotion = isPromotion;
        this.discount = discount;
        this.event = event;
        this.content = content;
    }

    protected Book(Parcel in) {
        restaurant = in.readParcelable(Restaurant.class.getClassLoader());
        dateTime = in.readString();
        image = in.readString();
        numberPerson = in.readInt();
        isPromotion = in.readInt();
        discount = in.readDouble();
        event = in.readString();
        content = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getNumberPerson() {
        return numberPerson;
    }

    public int getIsPromotion() {
        return isPromotion;
    }

    public String getEvent() {
        return event;
    }

    public String getContent() {
        return content;
    }

    public double getDiscount() {
        return discount;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(restaurant, flags);
        dest.writeString(dateTime);
        dest.writeString(image);
        dest.writeInt(numberPerson);
        dest.writeInt(isPromotion);
        dest.writeDouble(discount);
        dest.writeString(event);
        dest.writeString(content);
    }
}
