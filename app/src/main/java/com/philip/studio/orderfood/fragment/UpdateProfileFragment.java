package com.philip.studio.orderfood.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.User;
import com.philip.studio.orderfood.util.UserUtil;

public class UpdateProfileFragment extends Fragment {

    Button btnSignIn;
    EditText edtName, edtAddress;
    TextView txtBack, txtPhoneNumber;

    UserUtil userUtil;
    String data;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;

    public UpdateProfileFragment(String data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        initView(view);

        txtBack.setOnClickListener(v -> onBackScreen());

        txtPhoneNumber.setText(data);
        btnSignIn.setOnClickListener(v -> {
            String name = edtName.getText().toString();
            String address = edtAddress.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
                Toast.makeText(getContext(), "Dữ liệu là đang trống", Toast.LENGTH_SHORT).show();
            } else {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout_container, new SuggestionInformationFragment())
                        .commit();
//                User user = new User(data, name, address, "avatar");
//                userUtil.setUser(user);
//                insertUserFromDatabase(data, user);
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                startActivity(intent);
//                getActivity().finish();
            }
        });
        return view;
    }

    private void insertUserFromDatabase(String key, User user) {
        dataRef.child(key).setValue(user);
    }

    private void onBackScreen(){
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_container, new SignInFragment())
                .commit();
    }

    private void initView(View view) {
        btnSignIn = view.findViewById(R.id.button_sign_in);
        edtName = view.findViewById(R.id.edit_text_name);
        edtAddress = view.findViewById(R.id.edit_text_address);
        txtBack = view.findViewById(R.id.text_view_back);
        txtPhoneNumber = view.findViewById(R.id.text_view_phone_number);

        userUtil = new UserUtil(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("User");
    }
}
