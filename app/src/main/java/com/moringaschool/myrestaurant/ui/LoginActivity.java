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
import com.google.firebase.database.FirebaseDatabase;
import com.moringaschool.myrestaurant.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

//    Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

//    TAG
    private static final String TAG = LoginActivity.class.getSimpleName();

//    Butter knife binding views
    @BindView(R.id.registerTextView) TextView mOptionToRegisterTextView;
    @BindView(R.id.passwordLoginButton) Button mPasswordLoginButton;
    @BindView(R.id.emailEditText) EditText mEmailEditText;
    @BindView(R.id.passwordEditText) EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mOptionToRegisterTextView.setOnClickListener(this);

//        Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if ( firebaseUser != null ){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mPasswordLoginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        if (view == mOptionToRegisterTextView){
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
            finish();
        }

        if ( view == mPasswordLoginButton ){
            login();
        }

    }

//    Custom Method to allow the user to login
    public void login() {
        String user_email = mEmailEditText.getText().toString().trim();
        String user_password = mPasswordEditText.getText().toString().trim();

        if ( user_email.equals("") ){
            mEmailEditText.setError("Invalid user email");
            return;
        }
        if ( user_password.equals("") ){
            mPasswordEditText.setError("Invalid user password");
            return;
        }

        mAuth.signInWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ){
                    String authentication = "Successful!";
                    Log.d(TAG, "onComplete: " +  authentication);
                    Toast.makeText(LoginActivity.this, authentication, Toast.LENGTH_SHORT).show();
                }
                else{
                    String authentication = "Authentication failed";
                    Toast.makeText(LoginActivity.this, authentication, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if ( mAuth == null ){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}