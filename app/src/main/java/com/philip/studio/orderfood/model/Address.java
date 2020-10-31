package com.philip.studio.orderfood.model;

public class Address {
    private long latitude;
    private long longitude;
    private String street;
    private String ward;
    private String province;

    public Address(String street, String ward, String province) {
        this.street = street;
        this.ward = ward;
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public String getWard() {
        return ward;
    }

    public String getProvince() {
        return province;
    }

    @Override
    public String toString() {
        return street + ", " + ward + ", " + province;
    }
}
