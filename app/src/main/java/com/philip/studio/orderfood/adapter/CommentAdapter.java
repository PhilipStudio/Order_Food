package com.philip.studio.orderfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.github.abdularis.civ.AvatarImageView;
import com.mikelau.views.shimmer.ShimmerRecyclerViewX;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.Comment;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;

public class CommentAdapter extends ShimmerRecyclerViewX.Adapter<CommentAdapter.ViewHolder> {

    ArrayList<Comment> arrayList;
    Context context;

    public CommentAdapter(ArrayList<Comment> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtNickname.setText(arrayList.get(position).getNickname());
        holder.txtTime.setText(arrayList.get(position).getTime());
        if (arrayList.get(position).getAvatar().equals(" ")){
            holder.avatarImageView.setState(AvatarImageView.SHOW_INITIAL);
            holder.avatarImageView.setText(arrayList.get(position).getNickname());
        }
        else{
            holder.avatarImageView.setState(AvatarImageView.SHOW_IMAGE);
            Glide.with(context).load(arrayList.get(position).getAvatar()).into(holder.avatarImageView);
        }

        holder.txtContent.setText(arrayList.get(position).getContent());
        holder.ratingBar.setRating(arrayList.get(position).getStar());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends ShimmerRecyclerViewX.ViewHolder{

        AvatarImageView avatarImageView;
        TextView txtNickname, txtContent, txtTime;
        BaseRatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            avatarImageView = itemView.findViewById(R.id.item_avatar);
            txtNickname = itemView.findViewById(R.id.item_nickname);
            txtContent = itemView.findViewById(R.id.item_content_comment);
            txtTime = itemView.findViewById(R.id.item_time);
            ratingBar = itemView.findViewById(R.id.item_base_rating_bar);
        }
    }
}
