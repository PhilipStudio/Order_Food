package com.philip.studio.orderfood.fragment;

import android.app.Activity;
import android.app.Dialog;
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

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.config.ConfigPayPal;
import com.philip.studio.orderfood.model.Cart;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import io.realm.RealmResults;

public class InformationOrderFragment extends Fragment {

    Button btnConfirmOrder;
    TextView txtNameRes, txtNameUser, txtTotal;
    RadioButton rBAirPay, rBMoney;

    ArrayList<Cart> arrayList;
    String name;
    double totalOrder = 0;
    public static final int PAYPAL_REQUEST_CODE = 123;

    private static PayPalConfiguration palConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(ConfigPayPal.PAYPAL_CLIENT_ID);

    public InformationOrderFragment(String name, ArrayList<Cart> arrayList) {
        this.name = name;
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

        txtNameRes.setText(name);
        totalOrder = calculateTotalOrder(arrayList);

        btnConfirmOrder.setText("Đặt hàng - " + formatTextTotalOrder(totalOrder));
        btnConfirmOrder.setOnClickListener(v -> {
            processPayment(totalOrder);
            });

        return view;
    }

    private void processPayment(double amount){
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD",
                "Payment for " + name, PayPalPayment.PAYMENT_INTENT_ORDER);

        Intent intent = new Intent(getContext(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, palConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == PAYPAL_REQUEST_CODE && data != null){
            PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirmation != null){
                try {
                    String paymentDetails = confirmation.toJSONObject().toString(4);
                    JSONObject jsonObject = new JSONObject(paymentDetails);
                    showDialogPaymentSuccess(jsonObject.getJSONObject("response"), String.valueOf(totalOrder));
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

    private void showDialogPaymentSuccess(JSONObject jsonObject, String amount) throws JSONException {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_dialog_paypal);

        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        dialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtAmount, txtPaymentID, txtStatus;
        txtAmount = dialog.findViewById(R.id.text_view_amount);
        txtPaymentID = dialog.findViewById(R.id.text_view_payment_id);
        txtStatus = dialog.findViewById(R.id.text_view_status);

        String paymentID = jsonObject.getString("id");
        txtPaymentID.setText(paymentID);
        txtAmount.setText("$" + amount);
        txtStatus.setText(jsonObject.getString("state"));

        dialog.show();
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
        rBAirPay = view.findViewById(R.id.radio_button_airpay);
        rBMoney = view.findViewById(R.id.radio_button_money);
        txtNameRes = view.findViewById(R.id.text_view_name_restaurant);
        txtNameUser = view.findViewById(R.id.text_view_name_user);
        txtTotal = view.findViewById(R.id.text_view_total);
    }
}
