package com.philip.studio.orderfood.callback;

import com.philip.studio.orderfood.model.Order;

public interface OnButtonPaymentClickListener {
    void onPaymentSuccess(Order order);
    void onPaymentFailure();
}
