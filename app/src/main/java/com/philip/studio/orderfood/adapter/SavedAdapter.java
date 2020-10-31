package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Restaurant;
import com.philip.studio.orderfood.model.Saved;

import java.util.ArrayList;

import io.realm.RealmResults;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder> {

    RealmResults<Saved> realmResults;
    Context context;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dataRef;


    public SavedAdapter(RealmResults<Saved> realmResults, Context context) {
        this.realmResults = realmResults;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return realmResults.get(position).getViewType();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_tuesday, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rVList.setHasFixedSize(true);
        switch (realmResults.get(position).getViewType()){
            case 0:
                holder.txtNameCategory.setText("Món ăn đã thích");
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                holder.rVList.setLayoutManager(layoutManager1);
                dataRef = firebaseDatabase.getReference().child("");
                break;
            case 1:
                holder.txtNameCategory.setText("Các cửa hàng đã thích");
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                holder.rVList.setLayoutManager(layoutManager2);

                dataRef = firebaseDatabase.getReference().child("Restaurant");
                dataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Restaurant> arrayList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                            arrayList.add(restaurant);
                        }

                        RestaurantSavedAdapter adapter = new RestaurantSavedAdapter(arrayList, context);
                        holder.rVList.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtNameCategory;
        RecyclerView rVList;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameCategory = itemView.findViewById(R.id.item_name_category);
            rVList = itemView.findViewById(R.id.item_list);
        }
    }
}
