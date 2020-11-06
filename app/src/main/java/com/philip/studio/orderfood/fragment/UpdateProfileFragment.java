package com.philip.studio.orderfood.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.abdularis.civ.AvatarImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.philip.studio.orderfood.R;
import com.philip.studio.orderfood.activity.MainActivity;
import com.philip.studio.orderfood.model.User;
import com.philip.studio.orderfood.util.UserUtil;

import io.realm.Realm;

public class UpdateProfileFragment extends Fragment {

    Button btnSignIn;
    EditText edtName, edtAddress;
    TextView txtBack, txtPhoneNumber;
    AvatarImageView avatarImageView;
    RadioButton rBMale, rBFemale;
    ImageButton imgButtonBirthday;
    EditText edtBirthday;

    UserUtil userUtil;
    String data, sex, birthday, link;
    Uri uri;
    static final int REQUESTCODE = 123;
    Realm realm;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;
    FirebaseStorage firebaseStorage;
    StorageReference stoRef;

    public UpdateProfileFragment(String data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        initView(view);

        txtBack.setOnClickListener(v -> onBackScreen());

        avatarImageView.setState(AvatarImageView.SHOW_IMAGE);
        Glide.with(getContext()).load(R.drawable.ic_baseline_account_circle).into(avatarImageView);
        txtPhoneNumber.setText(data);

        rBMale.setOnClickListener(listener);
        rBFemale.setOnClickListener(listener);

        btnSignIn.setOnClickListener(v -> {
            String name = edtName.getText().toString();
            String address = edtAddress.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) ||
                    TextUtils.isEmpty(sex) || TextUtils.isEmpty(birthday) || TextUtils.isEmpty(link)) {
                Toast.makeText(getContext(), "Dữ liệu là đang trống", Toast.LENGTH_SHORT).show();
            } else {
                User user = new User(data, name, address, sex, birthday, link);
                userUtil.setUser(user);
                insertUserFromDatabase(data, user);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
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
            uri = data.getData();
            updateAvatarForUser(uri, avatarImageView);
        }
    }

    private void updateAvatarForUser(Uri uri, AvatarImageView avatarImageView){
        StorageReference imageFilePath = stoRef.child(uri.getLastPathSegment());
        imageFilePath.putFile(uri).addOnSuccessListener(taskSnapshot -> imageFilePath.getDownloadUrl().addOnSuccessListener(uri1 -> {
            link = uri1.toString();
            avatarImageView.setState(AvatarImageView.SHOW_IMAGE);
            Glide.with(getContext()).load(link).into(avatarImageView);
            dataRef.child(userUtil.getUser().getPhoneNumber()).child("avatar").setValue(link);
        }));
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

    private View.OnClickListener listener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.radio_button_male:
                    sex = rBMale.getText().toString();
                    break;
                case R.id.radio_button_female:
                    sex = rBFemale.getText().toString();
                    break;
                case R.id.image_button_choose_datetime:
                    showDateTimePickerDialog();
                    break;
                case R.id.avatar_image_view:
                    openGallery();
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDateTimePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            birthday = dayOfMonth + "/" + month + "/" + year;
            edtBirthday.setText(birthday);
        });
        datePickerDialog.show();
    }

    private void initView(View view) {
        btnSignIn = view.findViewById(R.id.button_sign_in);
        edtName = view.findViewById(R.id.edit_text_name);
        edtAddress = view.findViewById(R.id.edit_text_address);
        txtBack = view.findViewById(R.id.text_view_back);
        txtPhoneNumber = view.findViewById(R.id.text_view_phone_number);
        avatarImageView = view.findViewById(R.id.avatar_image_view);
        rBMale = view.findViewById(R.id.radio_button_male);
        rBFemale = view.findViewById(R.id.radio_button_female);
        imgButtonBirthday = view.findViewById(R.id.image_button_choose_datetime);
        edtBirthday = view.findViewById(R.id.edit_text_birthday);

        userUtil = new UserUtil(getContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("User");
        firebaseStorage = FirebaseStorage.getInstance();
        stoRef = firebaseStorage.getReference().child("image_avatar");
        realm = Realm.getDefaultInstance();
    }
}
