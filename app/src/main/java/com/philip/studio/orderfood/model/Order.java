package com.philip.studio.orderfood.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

import io.realm.RealmResults;

@IgnoreExtraProperties
public class Order {
    private String idOrder;
    private String phone;
    private String name;
    private String address;
    private String total;
    private RealmResults<Cart> results;
    private String status;

    public Order() {
    }

    public Order(String idOrder, String phone, String name, String address, String total, RealmResults<Cart> results) {
        this.idOrder = idOrder;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.results = results;
        this.status = "0"; //default is 0; 0:order; 1:shipping; 2:shipped
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTotal() {
        return total;
    }

    public RealmResults<Cart> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }
}
