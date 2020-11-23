package com.philip.studio.orderfood.callback;

import com.philip.studio.orderfood.model.Route;

import java.util.List;

public interface DirectionFinderListener {
    void onDirectionStart();
    void onDirectionSuccess(List<Route> routeList);
}
