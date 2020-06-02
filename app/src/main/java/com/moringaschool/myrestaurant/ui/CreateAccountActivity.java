package com.moringaschool.myrestaurant.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.moringaschool.myrestaurant.R;

import butterknife.*;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener{

//    TAG
    private static final String TAG = CreateAccountActivity.class.getSimpleName();

//    Firebase Authentication
    private FirebaseAuth mAuth;

//    Firebase Authentication state listener local member variable
    private FirebaseAuth.AuthStateListener mFirebaseAuth;

//    Butter knife binding
    @BindView(R.id.createUserButton) Button mCreateUserButton;
    @BindView(R.id.nameEditText) EditText mNameEditText;
    @BindView(R.id.emailEditText) EditText mEmailEditText;
    @BindView(R.id.passwordEditText) EditText mPasswordEditText;
    @BindView(R.id.confirmPasswordEditText) EditText mConfirmPasswordEditText;
    @BindView(R.id.loginTextView) TextView mLoginTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ButterKnife.bind(this);

        mLoginTextView.setOnClickListener(this);

//        To log in btn
        mCreateUserButton.setOnClickListener(this);

//        Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();

//        Firebase Authentication method
        createAuthenticationListener();
    }

    @Override
    public void onClick(View v) {

        if ( v == mLoginTextView ){
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        if ( v == mCreateUserButton) {
                createUser();
        }

    }

    public void createUser() {

        if ((mNameEditText.equals("")) || (mEmailEditText.equals("")) || (mPasswordEditText.equals("")) || (mConfirmPasswordEditText.equals(""))) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        } else {

            final String name = mNameEditText.getText().toString().trim();
            final String email = mEmailEditText.getText().toString().trim();
            final String password = mPasswordEditText.getText().toString().trim();
            final String confirmedPassword = mConfirmPasswordEditText.getText().toString().trim();


            if ( ! (password.equals(confirmedPassword)) ) {
                mCreateUserButton.setActivated(false);

                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                mPasswordEditText.setText("");
                mConfirmPasswordEditText.setText("");

            }else {
                mCreateUserButton.setActivated(true);

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, name + ", Authentication successful");
                            Toast.makeText(CreateAccountActivity.this, name + ", your data has been saved successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateAccountActivity.this, name + ", Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    public void createAuthenticationListener() {
        mFirebaseAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if ( firebaseUser != null ){
                    Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mFirebaseAuth);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mFirebaseAuth);
    }
}