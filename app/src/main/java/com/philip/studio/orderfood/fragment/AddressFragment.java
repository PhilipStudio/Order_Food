package com.philip.studio.orderfood.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.MapsActivity;

import java.util.Objects;

public class AddressFragment extends Fragment {
    ImageView imgBack, imgAddAddress;
    TextView txtAddHouseAddress, txtAddCompanyAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        initView(view);

        imgBack.setOnClickListener(listener);
        txtAddHouseAddress.setOnClickListener(listener);
        txtAddCompanyAddress.setOnClickListener(listener);
        imgAddAddress.setOnClickListener(listener);
        return view;
    }

    private View.OnClickListener listener = v -> {
        switch (v.getId()) {
            case R.id.image_view_back:
                Objects.requireNonNull(getActivity()).finish();
                break;
            case R.id.text_view_add_company_address:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout_container, new CreateAddressFragment("Công ty"))
                        .commit();
                break;
            case R.id.text_view_add_house_address:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout_container, new CreateAddressFragment("Nhà"))
                        .commit();
                break;
            case R.id.image_view_add_address:
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
                break;
        }
    };

    private void initView(View view) {
        imgBack = view.findViewById(R.id.image_view_back);
        imgAddAddress = view.findViewById(R.id.image_view_add_address);
        txtAddCompanyAddress = view.findViewById(R.id.text_view_add_company_address);
        txtAddHouseAddress = view.findViewById(R.id.text_view_add_house_address);
    }
}
