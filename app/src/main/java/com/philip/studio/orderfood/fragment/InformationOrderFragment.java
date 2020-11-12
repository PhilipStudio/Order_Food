package com.philip.studio.orderfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.PaymentActivity;

public class InformationOrderFragment extends Fragment {

    Button btnConfirmOrder;
    TextView txtNameRes, txtNameUser, txtTotal;
    RadioButton rBAirPay, rBMoney;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_order, container, false);
        initView(view);

        btnConfirmOrder.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PaymentActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void initView(View view){
        btnConfirmOrder = view.findViewById(R.id.button_confirm_order);
        rBAirPay = view.findViewById(R.id.radio_button_airpay);
        rBMoney = view.findViewById(R.id.radio_button_money);
        txtNameRes = view.findViewById(R.id.text_view_name_restaurant);
        txtNameUser = view.findViewById(R.id.text_view_name_user);
        txtTotal = view.findViewById(R.id.text_view_total);
    }
}
