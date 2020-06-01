package com.moringaschool.myrestaurant.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.moringaschool.myrestaurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

//    TAG
    private static final String TAG = LoginActivity.class.getSimpleName();

//    Butter knife binding views
    @BindView(R.id.registerTextView) TextView mOptionToRegisterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mOptionToRegisterTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        if (view == mOptionToRegisterTextView){
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
            finish();
        }
    }
}