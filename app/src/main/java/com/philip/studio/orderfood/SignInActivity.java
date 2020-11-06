package com.philip.studio.orderfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.philip.studio.orderfood.activity.MainActivity;
import com.philip.studio.orderfood.fragment.SignInFragment;
import com.philip.studio.orderfood.model.User;
import com.philip.studio.orderfood.util.UserUtil;

import io.realm.Realm;
import io.realm.RealmResults;

public class SignInActivity extends AppCompatActivity {

    FrameLayout frameLayout;

    UserUtil userUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);
        frameLayout = findViewById(R.id.frame_layout_container);
        userUtil = new UserUtil(this);

        if (userUtil.getUser() != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout_container, new SignInFragment())
                    .commit();
        }
    }

}