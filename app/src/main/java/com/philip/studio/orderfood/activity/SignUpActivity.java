package com.philip.studio.orderfood.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.model.User;
import com.philip.studio.orderfood.util.UserUtil;

import info.hoang8f.widget.FButton;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText inputEmail, inputPassword, inputFirstname, inputLastname;
    FButton fButtonSignUp;
    ImageView imgAvatar;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataUserRef;
    FirebaseStorage firebaseStorage;
    StorageReference stoRef;

    private static final int REQUEST_CODE = 123;
    String link = null;
    UserUtil userUtil;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();

        imgAvatar.setOnClickListener(v -> openGallery());
        fButtonSignUp.setOnClickListener(v -> {
            String firstname = setDataInput(inputFirstname);
            String lastname = setDataInput(inputLastname);
            String name = firstname + " " + lastname;
            String email = setDataInput(inputEmail);
            String pass = setDataInput(inputPassword);

            if (email != null && pass != null) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(authResult -> {
                    if (authResult != null) {
                        FirebaseUser firebaseUser = authResult.getUser();
                        String idUser = firebaseUser.getUid();
                        User user = new User(idUser, email, pass, name, link);
                        userUtil.setUser(user);
                        dataUserRef.child(idUser).setValue(user);
                        Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private String setDataInput(TextInputEditText input) {
        String data = input.getText().toString();
        if (TextUtils.isEmpty(data)) {
            input.setError("Data input is null");
        }
        return data;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            uri = data.getData();
            imgAvatar.setImageURI(uri);
            savedImageToStorageFirebase(uri);
        }
    }

    private void savedImageToStorageFirebase(Uri uri) {
        stoRef = firebaseStorage.getReference().child(uri.getLastPathSegment());
        stoRef.putFile(uri).addOnSuccessListener(taskSnapshot -> stoRef.getDownloadUrl()
                .addOnSuccessListener(uri1 -> link = uri1.toString()));
    }

    public void onBack(View view) {
        finish();
    }

    private void initView() {
        fButtonSignUp = findViewById(R.id.f_button_sign_up);
        inputFirstname = findViewById(R.id.text_input_firstname);
        inputLastname = findViewById(R.id.text_input_lastname);
        inputEmail = findViewById(R.id.text_input_email);
        inputPassword = findViewById(R.id.text_input_password);
        imgAvatar = findViewById(R.id.image_view_avatar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        dataUserRef = firebaseDatabase.getReference().child("User");
        userUtil = new UserUtil(this);
    }
}