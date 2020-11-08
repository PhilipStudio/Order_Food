package com.philip.studio.orderfood.model;

import io.realm.RealmResults;

public class Saved {
    private String name;
    RealmResults<Cart> realmResults;

    public Saved(String name, RealmResults<Cart> realmResults) {
        this.name = name;
        this.realmResults = realmResults;
    }

    public RealmResults<Cart> getRealmResults() {
        return realmResults;
    }

    public String getName() {
        return name;
    }
}
