package com.philip.studio.orderfood.event;/*
//
// Project: Order Food
// Created by ViettelStore on 1/10/2021.
// Copyright © 2021-2022 Philip Studio. All rights reserved.
//
*/

public class SearchMenuEvent {
    private int position;

    public SearchMenuEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
