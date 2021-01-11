package com.philip.studio.orderfood.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

import io.realm.RealmResults;

@IgnoreExtraProperties
public class Order {
    private String idOrder;
    private String paymentId;
    private String phone;
    private String name;
    private String address;
    private String total;
    private ArrayList<Cart> arrayList;
    private String status;

    public Order() {
    }

    public Order(String idOrder, String paymentId, String phone, String name, String address, String total, ArrayList<Cart> arrayList, String status) {
        this.idOrder = idOrder;
        this.paymentId = paymentId;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.arrayList = arrayList;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public String getPaymentId() {
        return paymentId;
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

    public ArrayList<Cart> getArrayList() {
        return arrayList;
    }

    public String getStatus() {
        return status;
    }
}
