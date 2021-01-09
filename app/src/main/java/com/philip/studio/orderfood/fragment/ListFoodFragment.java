package com.philip.studio.orderfood.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.adapter.FoodInCartAdapter;
import com.philip.studio.orderfood.model.Cart;

import java.util.ArrayList;

public class ListFoodFragment extends Fragment {

    RecyclerView rVListFood;

    ArrayList<Cart> arrayList;

    public ListFoodFragment(ArrayList<Cart> arrayList) {
        this.arrayList = arrayList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_food, container, false);
        rVListFood = view.findViewById(R.id.recycler_view_list_food);
        rVListFood.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rVListFood.setLayoutManager(layoutManager);

        FoodInCartAdapter adapter = new FoodInCartAdapter(arrayList, getContext());
        rVListFood.setAdapter(adapter);



        return view;
    }
}
