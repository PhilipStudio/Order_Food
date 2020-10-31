package com.philip.studio.orderfood.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String restaurantID;//Dat ban tai nha hang nao
    private String idUser;
    private String dateTime;//Thoi gian dat ban
    private int numberPerson;//So luong nguoi
    private String event; //Dat ban cho su kien gi
    private String note; //Ghi chú

    //Vi du dat mot ban an tai nha hang A gom 10 cho su kien sinh nhat tu 20:30 - 22:30

    public Book(String restaurantID, String idUser, String dateTime, int numberPerson, String event, String note) {
        this.restaurantID = restaurantID;
        this.idUser = idUser;
        this.dateTime = dateTime;
        this.numberPerson = numberPerson;
        this.event = event;
        this.note = note;
    }

    protected Book(Parcel in) {
        restaurantID = in.readString();
        idUser = in.readString();
        dateTime = in.readString();
        numberPerson = in.readInt();
        event = in.readString();
        note = in.readString();
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

    public String getRestaurantID() {
        return restaurantID;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getNumberPerson() {
        return numberPerson;
    }

    public String getEvent() {
        return event;
    }

    public String getNote() {
        return note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(restaurantID);
        dest.writeString(idUser);
        dest.writeString(dateTime);
        dest.writeInt(numberPerson);
        dest.writeString(event);
        dest.writeString(note);
    }

    @Override
    public String toString() {
        return "Book{" +
                "restaurantID='" + restaurantID + '\'' +
                ", idUser='" + idUser + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", numberPerson=" + numberPerson +
                ", event='" + event + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
