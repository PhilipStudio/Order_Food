package com.philip.studio.orderfood.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User implements Parcelable {
    private String phoneNumber;
    private String name;
    private String gender;
    private String birthday;
    private String address;
    private String avatar;

    public User() {
    }

    public User(String phoneNumber, String name, String gender, String birthday, String address, String avatar) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.avatar = avatar;
    }

    protected User(Parcel in) {
        phoneNumber = in.readString();
        name = in.readString();
        address = in.readString();
        avatar = in.readString();
        gender = in.readString();
        birthday = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phoneNumber);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(avatar);
        dest.writeString(gender);
        dest.writeString(birthday);
    }
}