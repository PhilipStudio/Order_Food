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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.adapter.OrderAdapter;
import com.philip.studio.orderfood.model.Order;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    RecyclerView rVListOrder;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataOrderRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        rVListOrder = view.findViewById(R.id.recycler_view_list_order);
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataOrderRef = firebaseDatabase.getReference().child("Order");

        rVListOrder.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rVListOrder.setLayoutManager(layoutManager);

        dataOrderRef.orderByChild("phone").equalTo("0385035105").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Order> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                    arrayList.add(order);
                }

                OrderAdapter adapter = new OrderAdapter(arrayList, getContext());
                rVListOrder.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}
