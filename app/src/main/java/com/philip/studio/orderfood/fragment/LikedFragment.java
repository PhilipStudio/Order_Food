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

import io.realm.Realm;

public class LikedFragment extends Fragment {

    RecyclerView rVListLiked;

    Realm realm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liked, container, false);
        Realm.init(getContext());
        rVListLiked = view.findViewById(R.id.recycler_view_list_liked);
        rVListLiked.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rVListLiked.setLayoutManager(layoutManager);

        loadListSaved();
        return view;
    }

    private void loadListSaved(){

    }
}
