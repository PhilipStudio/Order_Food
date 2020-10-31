package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.philip.studio.orderfood.R;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {
    List<String> stringList;
    Context context;

    OnItemTimeClickListener onItemTimeClickListener;

    public TimeAdapter(List<String> stringList, Context context) {
        this.stringList = stringList;
        this.context = context;
    }

    public void setOnItemTimeClickListener(OnItemTimeClickListener onItemTimeClickListener) {
        this.onItemTimeClickListener = onItemTimeClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btnTime.setText(stringList.get(position));

        holder.btnTime.setOnClickListener(v -> {
            if (onItemTimeClickListener != null){
                onItemTimeClickListener.onItemClick(holder.btnTime.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Button btnTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnTime = itemView.findViewById(R.id.item_button_time);
        }
    }

    public interface OnItemTimeClickListener{
        void onItemClick(String text);
    }
}
