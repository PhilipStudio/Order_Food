package com.philip.studio.orderfood.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.abdularis.civ.AvatarImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.philip.studio.orderfood.SignInActivity;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.util.UserUtil;

public class UserFragment extends Fragment {

    Button btnSignOut;
    TextView txtNameUser;
    AvatarImageView avatarImageView;

    UserUtil userUtil;
    static final int REQUESTCODE = 123;

    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference stoRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initView(view);

        userUtil = new UserUtil(getContext());

        String name = userUtil.getUser().getName();
        txtNameUser.setText(name);

        if (userUtil.getUser().getAvatar().equals(" ")){
            avatarImageView.setState(AvatarImageView.SHOW_INITIAL);
            avatarImageView.setText(userUtil.getUser().getName());
        }
        else{
            avatarImageView.setState(AvatarImageView.SHOW_IMAGE);
            Glide.with(getContext()).load(userUtil.getUser().getAvatar()).into(avatarImageView);
        }

        avatarImageView.setOnClickListener(v -> openGallery());

        btnSignOut.setOnClickListener(v -> {
            firebaseAuth.signOut();
            userUtil.setUser(null);
            Intent intent = new Intent(getContext(), SignInActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == REQUESTCODE && data != null){
            Uri uri = data.getData();
            avatarImageView.setState(AvatarImageView.SHOW_IMAGE);
            avatarImageView.setImageURI(uri);
            updateAvatarForUser(uri);
        }
    }

    private void updateAvatarForUser(Uri uri){
        StorageReference imageFilePath = stoRef.child(uri.getLastPathSegment());
        imageFilePath.putFile(uri).addOnSuccessListener(taskSnapshot -> imageFilePath.getDownloadUrl().addOnSuccessListener(uri1 -> {
            String link = uri1.toString();
            dataRef.child(userUtil.getUser().getPhoneNumber()).child("avatar").setValue(link);
        }));
    }

    private void initView(View view){
        btnSignOut = view.findViewById(R.id.button_sign_out);
        txtNameUser = view.findViewById(R.id.text_view_name_user);
        avatarImageView = view.findViewById(R.id.avatar_image_view);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        stoRef = firebaseStorage.getReference().child("image_avatar");
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("User");
    }
}
