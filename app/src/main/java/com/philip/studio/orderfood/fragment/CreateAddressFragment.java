package com.philip.studio.orderfood.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.philip.studio.orderfood.R;

public class CreateAddressFragment extends Fragment {

    String data;

    Button btnHouse, btnCompany, btnOther;
    Toolbar toolbar;


    public CreateAddressFragment(String data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_address, container, false);

        initView(view);

        toolbar.setNavigationOnClickListener(v -> getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_container, new AddressFragment())
                .commit());

        if (data.equals("Nhà")) {
            btnHouse.setBackgroundColor(Color.parseColor("#FFF69A7E"));
        } else if (data.equals("Công ty")) {
            btnCompany.setBackgroundColor(Color.parseColor("#FFF69A7E"));
        } else {
            btnOther.setBackgroundColor(Color.parseColor("#FFF69A7E"));
        }


        return view;
    }

    private void initView(View view){
        btnHouse = view.findViewById(R.id.button_house);
        btnCompany = view.findViewById(R.id.button_company);
        btnOther = view.findViewById(R.id.button_other);
        toolbar = view.findViewById(R.id.toolbar);
    }
}
