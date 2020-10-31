package com.philip.studio.orderfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nex3z.flowlayout.FlowLayout;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.MainActivity;

public class SuggestionInformationFragment extends Fragment {
    Button btnContinue;
    FlowLayout flowLayout;

    String[] arrayFoodSuggestion;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestion_information, container, false);
        btnContinue = view.findViewById(R.id.button_continue);
        flowLayout = view.findViewById(R.id.flow_layout);

        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }


}
