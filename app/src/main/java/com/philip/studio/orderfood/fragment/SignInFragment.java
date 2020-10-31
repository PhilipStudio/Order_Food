package com.philip.studio.orderfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.util.UserUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;

import java.util.Arrays;

import info.hoang8f.widget.FButton;

public class SignInFragment extends Fragment {

    FButton fButtonContinue;
    MaterialEditText materialEdtPhone;
    TextView txtCountryCode;
    Button btnSignInFacebook, btnSignInGoogle;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataUserRef;

    UserUtil userUtil;
    String phoneNumber;
    CallbackManager callbackManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_ONLY);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            try {
                                String id = object.getString("id");
                                String firstName = object.getString("first_name");
                                String lastName = object.getString("last_name");
                                String nickname = firstName + " " + lastName;
                                String profile_pic = "http://graph.facebook.com/" + id + "/picture?type=normal";
                                Log.i("avatar", profile_pic);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                Bundle parameter = new Bundle();
                parameter.putString("fields", "first_name, last_name, email, id");
                request.setParameters(parameter);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        initView(view);

        fButtonContinue.setOnClickListener(v -> {
            phoneNumber = materialEdtPhone.getText().toString();
            String data = "+84" + phoneNumber;
            if (TextUtils.isEmpty(phoneNumber)) {
                materialEdtPhone.setError("Phone number is required");
                materialEdtPhone.requestFocus();
            } else {
                if (isPhoneNumber(data)) {
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frame_layout_container, new VerifyPhoneFragment(data))
                            .commit();
                } else {
                    materialEdtPhone.setError("This isn't phone number");
                    materialEdtPhone.requestFocus();
                }
            }
        });

        btnSignInFacebook.setOnClickListener(v -> {
            LoginManager.getInstance()
                    .logInWithReadPermissions(SignInFragment.this,
                            Arrays.asList("user_photos", "email", "user_birthday", "public_profile"));
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isPhoneNumber(String text) {
        boolean isTrue = false;
        String[] array = {"086", "096", "097", "098", "032", "033", "034", "035", "036", "037", "038", "039", "088", "091", "094"
                , "083", "084", "085", "081", "082", "092", "056", "058", "089", "090", "093", "070", "079", "077", "076", "078", "099", "059"};

        String firstNumber = text.substring(0, 5);
        String result = firstNumber.replace("+84", "0");
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(result)) {
                isTrue = true;
                break;
            }
        }
        return isTrue;
    }

    private void initView(View view) {
        fButtonContinue = view.findViewById(R.id.f_button_continue);
        materialEdtPhone = view.findViewById(R.id.material_edit_text_phone_number);
        txtCountryCode = view.findViewById(R.id.text_view_country_code);
        btnSignInFacebook = view.findViewById(R.id.button_sign_in_with_facebook);
        btnSignInGoogle = view.findViewById(R.id.button_sign_in_with_google);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataUserRef = firebaseDatabase.getReference().child("User");

        userUtil = new UserUtil(getContext());
    }
}
