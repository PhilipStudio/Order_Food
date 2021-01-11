package com.philip.studio.orderfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.MainActivity;
import com.philip.studio.orderfood.activity.SignUpActivity;
import com.philip.studio.orderfood.model.User;
import com.philip.studio.orderfood.util.UserUtil;

import info.hoang8f.widget.FButton;

public class SignInWithEmailFragment extends Fragment {

    FButton fBtnSignIn;
    TextInputEditText inputEditTextEmail, inputEditTextPassword;
    ImageView imgBack;
    TextView txtSignUp;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataUserRef;

    UserUtil util;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in_with_email, container, false);

        initView(view);

        fBtnSignIn.setOnClickListener(v -> {
            String email = inputEditTextEmail.getText().toString();
            String password = inputEditTextPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Bạn chưa nhập dữ liệu", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(authResult -> {
                            if (authResult != null) {
                                String idUser = authResult.getUser().getUid();
                                getDataUser(idUser);
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show());
            }
        });

        imgBack.setOnClickListener(v -> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_container, new SignInFragment())
                .commit());

        txtSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SignUpActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void getDataUser(String id) {
        dataUserRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    util.setUser(user);
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        fBtnSignIn = view.findViewById(R.id.f_button_sign_in);
        inputEditTextEmail = view.findViewById(R.id.input_edit_text_email);
        inputEditTextPassword = view.findViewById(R.id.input_edit_text_password);
        imgBack = view.findViewById(R.id.image_view_back);
        txtSignUp = view.findViewById(R.id.text_view_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataUserRef = firebaseDatabase.getReference().child("Account").child("User");

        util = new UserUtil(getContext());
    }
}
