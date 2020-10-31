package com.philip.studio.orderfood.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class PriceRange implements Serializable {
    private long startPrice;
    private long endPrice;

    public PriceRange() {
    }

    public PriceRange(long startPrice, long endPrice) {
        this.startPrice = startPrice;
        this.endPrice = endPrice;
    }

    public long getStartPrice() {
        return startPrice;
    }

    public long getEndPrice() {
        return endPrice;
    }
}
