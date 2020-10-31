package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.BookDetailActivity;
import com.philip.studio.orderfood.callback.OnItemBookClickListener;
import com.philip.studio.orderfood.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{
    ArrayList<Book> arrayList;
    Context context;

    OnItemBookClickListener onItemBookClickListener;
    TimeAdapter.OnItemTimeClickListener onItemTimeClickListener;
    String dateTimeBook, timeBook;

    public BookAdapter(ArrayList<Book> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public void setOnItemBookClickListener(OnItemBookClickListener onItemBookClickListener) {
        this.onItemBookClickListener = onItemBookClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtEvent.setText(arrayList.get(position).getEvent());
        Glide.with(context).load(arrayList.get(position).getImage()).into(holder.imgImage);
        holder.txtDiscount.setText(arrayList.get(position).getDiscount() + "%");
        holder.txtContent.setText(arrayList.get(position).getContent());

        holder.rVListTime.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        holder.rVListTime.setLayoutManager(layoutManager);

        List<String> listTime = new ArrayList<>();
        listTime.add("14:00");
        listTime.add("14:15");
        listTime.add("14:30");
        listTime.add("14:45");
        listTime.add("15:00");
        listTime.add("15:15");
        listTime.add("15:30");
        listTime.add("15:45");
        listTime.add("16:00");

        TimeAdapter timeAdapter = new TimeAdapter(listTime, context);
        holder.rVListTime.setAdapter(timeAdapter);

        timeAdapter.setOnItemTimeClickListener(text -> {
            timeBook = text;
        });

        holder.btnBook.setOnClickListener(v -> {
            if (onItemBookClickListener != null){
                onItemBookClickListener.onItemClick(true, holder.btnBook);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgImage;
        TextView txtEvent, txtDiscount, txtContent;
        RecyclerView rVListTime;
        Button btnBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgImage = itemView.findViewById(R.id.item_image);
            txtEvent = itemView.findViewById(R.id.item_event);
            txtContent = itemView.findViewById(R.id.item_content_promotion);
            txtDiscount = itemView.findViewById(R.id.item_discount);
            btnBook = itemView.findViewById(R.id.item_button_book);
            rVListTime = itemView.findViewById(R.id.item_recycler_view_list_time);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                dateTimeBook = btnBook.getText().toString();
                bundle.putParcelable("book_detail", arrayList.get(pos));
                String time = timeBook + " " + dateTimeBook;
                bundle.putString("time", time);
                intent.putExtra("data", bundle);
                context.startActivity(intent);
            });
        }
    }
}
