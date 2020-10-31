package com.philip.studio.orderfood.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class BusinessHours implements Serializable {
    private String openTime;
    private String closeTime;

    public BusinessHours() {
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }
}
