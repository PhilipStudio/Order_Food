package com.philip.studio.orderfood.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikelau.views.shimmer.ShimmerRecyclerViewX;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.adapter.CommentAdapter;
import com.philip.studio.orderfood.model.Comment;
import com.philip.studio.orderfood.util.UserUtil;

import java.util.ArrayList;

public class CommentFragment extends Fragment {

    ShimmerRecyclerViewX sRVListComment;
    TextView txtNumberComment, txtStars;

    String idRes;
    float stars;
    UserUtil userUtil;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataCommentRef;

    public CommentFragment(String idRes, float stars) {
        this.idRes = idRes;
        this.stars = stars;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        initView(view);

        txtStars.setText(String.valueOf(stars));

        setUpShimmerRecyclerView(idRes);

        return view;
    }

    private void setUpShimmerRecyclerView(String idRes){
        sRVListComment.showShimmerAdapter();
        sRVListComment.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), ShimmerRecyclerViewX.VERTICAL, false);
        sRVListComment.setLayoutManager(layoutManager);

        loadListComment(idRes);
    }

    private void loadListComment(String idRes) {
        dataCommentRef.child(idRes).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Comment> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    arrayList.add(comment);
                }
                CommentAdapter adapter = new CommentAdapter(arrayList, getContext());
                sRVListComment.setAdapter(adapter);

                txtNumberComment.setText(String.valueOf(arrayList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView(View view){
        sRVListComment = view.findViewById(R.id.shimmer_recycler_view_list_comment);
        txtNumberComment = view.findViewById(R.id.text_view_number_comment);
        txtStars = view.findViewById(R.id.text_view_stars_restaurant);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataCommentRef = firebaseDatabase.getReference().child("Comment");

        userUtil = new UserUtil(getContext());
    }
}
