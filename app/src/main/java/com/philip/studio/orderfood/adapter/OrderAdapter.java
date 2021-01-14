package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Order;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    ArrayList<Order> arrayList;
    Context context;

    public OrderAdapter(ArrayList<Order> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtIdOrder.setText(arrayList.get(position).getIdOrder());
        holder.txtNameOrder.setText(arrayList.get(position).getName());
        holder.txtPhoneNumberOrder.setText(arrayList.get(position).getPhone());
        holder.txtAddressOrder.setText(arrayList.get(position).getAddress());
        holder.txtStatusOrder.setText(arrayList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtIdOrder, txtNameOrder, txtAddressOrder, txtPhoneNumberOrder, txtStatusOrder, txtOrderDetail;
        Button btnOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIdOrder = itemView.findViewById(R.id.item_id_order);
            txtAddressOrder = itemView.findViewById(R.id.item_address_order);
            txtNameOrder = itemView.findViewById(R.id.item_name_order);
            txtPhoneNumberOrder = itemView.findViewById(R.id.item_phone_number_order);
            txtStatusOrder = itemView.findViewById(R.id.item_status_order);
            btnOrder = itemView.findViewById(R.id.button_order);
            txtOrderDetail = itemView.findViewById(R.id.text_view_order_detail);
        }
    }
}
