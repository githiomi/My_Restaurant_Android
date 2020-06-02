package com.moringaschool.myrestaurant.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.android.material.snackbar.Snackbar;
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

//    Firebase authentication progress dialog
    private ProgressDialog mProgressDialog;

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

//        Launch the firebase authentication progress dialog
        createAuthProgressDialog();

    }

//    On click listeners
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

//    To add a new user to my firebase
    public void createUser() {

            final String name = mNameEditText.getText().toString().trim();
            final String email = mEmailEditText.getText().toString().trim();
            final String password = mPasswordEditText.getText().toString().trim();
            final String confirmedPassword = mConfirmPasswordEditText.getText().toString().trim();

                boolean isValidName = isValidName(name);
                boolean isValidEmail = isValidEmail(email);
                boolean isValidPassword = isValidPassword(password, confirmedPassword);

                if ( ! (isValidName) || ! (isValidEmail) || ! (isValidPassword)) return;

                mProgressDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mProgressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Log.d(TAG, name + ", Authentication successful");
                            Toast.makeText(CreateAccountActivity.this, "Authentication successful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateAccountActivity.this, name + ", Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

//    Methods to validate user input for sign up
    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEmailEditText.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            mNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            mPasswordEditText.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }

//    function to disable login if input is invalid
    public void disableLogin(){
        mCreateUserButton.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_disabled));
    }

//    Method that calls the firebase authentication progress dialog
    private void createAuthProgressDialog(){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Loading...");
            mProgressDialog.setMessage("Authenticating with Firebase... \n Please hold");
            mProgressDialog.setCancelable(false);

    }
}