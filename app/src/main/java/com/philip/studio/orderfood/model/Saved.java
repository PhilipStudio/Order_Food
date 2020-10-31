package com.philip.studio.orderfood.model;

public class Saved {
    String timeSaved;
    String idRes, idFood;
    int viewType;

    public Saved() {
    }

    public Saved(String timeSaved, String idRes, String idFood, int viewType) {
        this.timeSaved = timeSaved;
        this.idRes = idRes;
        this.idFood = idFood;
        this.viewType = viewType;
    }

    public Saved(String timeSaved, String idRes, int viewType) {
        this.timeSaved = timeSaved;
        this.idRes = idRes;
        this.viewType = viewType;
    }

    public String getIdRes() {
        return idRes;
    }

    public String getIdFood() {
        return idFood;
    }

    public String getTimeSaved() {
        return timeSaved;
    }

    public int getViewType() {
        return viewType;
    }
}
