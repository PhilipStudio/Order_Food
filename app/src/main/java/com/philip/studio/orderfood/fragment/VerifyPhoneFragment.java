package com.philip.studio.orderfood.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.MainActivity;
import com.philip.studio.orderfood.model.User;
import com.philip.studio.orderfood.util.UserUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import info.hoang8f.widget.FButton;

public class VerifyPhoneFragment extends Fragment {

    String verificationID, phoneNumber, code;
    UserUtil userUtil;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataUserRef;

    FloatingActionButton fabBack;
    EditText edtVerify;
    FButton FBtnVerify;

    public VerifyPhoneFragment(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_phone, container, false);

        initView(view);

        sendVerificationCode(phoneNumber);

        fabBack.setOnClickListener(v -> getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_container, new SignInFragment())
                .commit());

        FBtnVerify.setOnClickListener(v -> {
            String data = edtVerify.getText().toString();
            if (data.equals(code)) {
                verifyCode(data, phoneNumber);
            } else {
                Log.i("code", data);
                Toast.makeText(getContext(), "Mã xác nhận không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void sendVerificationCode(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                Objects.requireNonNull(getActivity()),
                mCallback
        );
    }

    private void verifyCode(String code, String phoneNumber) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signInWithCredential(credential, phoneNumber);
    }

    private void signInWithCredential(PhoneAuthCredential credential, String phoneNumber) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                dataUserRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        {
                            if (snapshot.exists()) {
                                User user = snapshot.getValue(User.class);
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                intent.putExtra("user", user);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.frame_layout_container, new UpdateProfileFragment(phoneNumber))
                                        .commit();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationID = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            code = phoneAuthCredential.getSmsCode();
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getContext(), "Đã xảy ra lỗi với mã xác nhận của bạn" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void initView(View view) {
        edtVerify = view.findViewById(R.id.edit_text_code);
        FBtnVerify = view.findViewById(R.id.f_button_verify);
        fabBack = view.findViewById(R.id.fab_back);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataUserRef = firebaseDatabase.getReference().child("User");

        userUtil = new UserUtil(getContext());
    }
}
