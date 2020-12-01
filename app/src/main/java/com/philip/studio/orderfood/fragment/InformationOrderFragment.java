package com.philip.studio.orderfood.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.callback.OnButtonPaymentClickListener;
import com.philip.studio.orderfood.config.ConfigPayPal;
import com.philip.studio.orderfood.model.Cart;
import com.philip.studio.orderfood.model.Order;
import com.philip.studio.orderfood.model.Restaurant;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNamePayment;

public class InformationOrderFragment extends Fragment {

    Button btnConfirmOrder;
    TextView txtNameRes, txtNameUser, txtTotal;
    RadioButton rBPaypal, rBMomo;

    ArrayList<Cart> arrayList;
    Restaurant restaurant;
    OnButtonPaymentClickListener listener;
    PaymentConfirmation confirmation;
    Order order;
    double totalOrder = 0;
    public static final int PAYPAL_REQUEST_CODE = 123;
    boolean isPaymentMOMO = false;

    private static PayPalConfiguration palConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(ConfigPayPal.PAYPAL_CLIENT_ID);

    public InformationOrderFragment(Restaurant restaurant, ArrayList<Cart> arrayList) {
        this.restaurant = restaurant;
        this.arrayList = arrayList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_order, container, false);
        initView(view);

        //start service
        Intent intent = new Intent(getContext(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, palConfiguration);
        if (getContext() != null){
            getContext().startService(intent);
        }

        txtNameRes.setText(restaurant.getName());
        totalOrder = calculateTotalOrder(arrayList);

        btnConfirmOrder.setText("Đặt hàng - " + formatTextTotalOrder(totalOrder));

        rBPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaymentMOMO = false;
            }
        });

        rBMomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaymentMOMO = true;
            }
        });
        btnConfirmOrder.setOnClickListener(v -> {
//            if (isPaymentMOMO){
//                requestPayment();
//            }
//            else{
//                processPayment(totalOrder);
//            }
            });

        return view;
    }

//    //payment with momo
//    private void requestPayment() {
//        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
//        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

//        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() != 0)
//            amount = edAmount.getText().toString().trim();
//
//        Map<String, Object> eventValue = new HashMap<>();
//        //client Required
//        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME, merchantName);
//        eventValue.put(MoMoParameterNamePayment.MERCHANT_CODE, merchantCode);
//        eventValue.put(MoMoParameterNamePayment.AMOUNT, amount);
//        eventValue.put(MoMoParameterNamePayment.DESCRIPTION, description);
//        //client Optional
//        eventValue.put(MoMoParameterNamePayment.FEE, fee);
//        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME_LABEL, merchantNameLabel);
//
//        eventValue.put(MoMoParameterNamePayment.REQUEST_ID,  merchantCode+"-"+ UUID.randomUUID().toString());
//        eventValue.put(MoMoParameterNamePayment.PARTNER_CODE, "CGV19072017");
//
//        JSONObject objExtraData = new JSONObject();
//        try {
//            objExtraData.put("site_code", "008");
//            objExtraData.put("site_name", "CGV Cresent Mall");
//            objExtraData.put("screen_code", 0);
//            objExtraData.put("screen_name", "Special");
//            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
//            objExtraData.put("movie_format", "2D");
//            objExtraData.put("ticket", "{\"ticket\":{\"01\":{\"type\":\"std\",\"price\":110000,\"qty\":3}}}");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        eventValue.put(MoMoParameterNamePayment.EXTRA_DATA, objExtraData.toString());
//        eventValue.put(MoMoParameterNamePayment.REQUEST_TYPE, "payment");
//        eventValue.put(MoMoParameterNamePayment.LANGUAGE, "vi");
//        eventValue.put(MoMoParameterNamePayment.EXTRA, "");
//        //Request momo app
//        AppMoMoLib.getInstance().requestMoMoCallBack(getActivity(), eventValue);
//    }


    //method payment with paypal
    private void processPayment(double amount){
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD",
                "Payment for " + restaurant.getName(), PayPalPayment.PAYMENT_INTENT_ORDER);

        Intent intent = new Intent(getContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, palConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == PAYPAL_REQUEST_CODE && data != null){
            confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirmation != null){
                try {
                    String paymentDetails = confirmation.toJSONObject().toString(4);
                    JSONObject jsonObject = new JSONObject(paymentDetails);
                    String paymentId = jsonObject.getString("id");
                    String idOrder = String.valueOf(System.currentTimeMillis());
                    String phone = "0926471468";
                    String name = "Nguyen Hoang Long";
                    String address = restaurant.getAddress();
                    String status = jsonObject.getString("state");

              //      order = new Order(idOrder, paymentId, phone, name, address, String.valueOf(totalOrder), arrayList, status);
                    Toast.makeText(getContext(), paymentId, Toast.LENGTH_SHORT).show();
              //      listener.onPaymentSuccess(order);
                }
                catch (Exception e){
                    Log.d("error", e.getLocalizedMessage());
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(getContext(), "Result cancel", Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(getContext(), "Invalid", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (OnButtonPaymentClickListener) context;
    }

    private String formatTextTotalOrder(double total){
        NumberFormat numberFormat = new DecimalFormat("#,###");
        return numberFormat.format(total) + "đ";
    }

    private double calculateTotalOrder(ArrayList<Cart> arrayList) {
        double total = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            double a = Double.parseDouble(arrayList.get(i).getPrice()) * Double.parseDouble(arrayList.get(i).getQuantity());
            total = total + a;
        }
        return total;
    }

    private void initView(View view){
        btnConfirmOrder = view.findViewById(R.id.button_confirm_order);
        rBPaypal = view.findViewById(R.id.radio_button_paypal);
        rBMomo = view.findViewById(R.id.radio_button_momo);
        txtNameRes = view.findViewById(R.id.text_view_name_restaurant);
        txtNameUser = view.findViewById(R.id.text_view_name_user);
        txtTotal = view.findViewById(R.id.text_view_total);
    }
}
