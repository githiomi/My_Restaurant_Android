package com.moringaschool.myrestaurant.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.models.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference mDatabaseLocationReference;
    private DatabaseReference mDatabaseUsernameReference;

    @BindView(R.id.nameEditText) EditText mUsername;
    @BindView(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @BindView(R.id.tvLocation) EditText mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDatabaseUsernameReference = Constants.ref.child(Constants.FIREBASE_USERNAME_KEY);
        mDatabaseLocationReference = Constants.ref.child(Constants.FIREBASE_LOCATION_KEY);

        mFindRestaurantsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v == mFindRestaurantsButton){
            String name = mUsername.getText().toString();
            String location = mLocation.getText().toString();


            Toast.makeText(this, "Welcome, " + name, Toast.LENGTH_LONG).show();

            if (! (location.equals("")) && !(name.equals("")) ){
                storeInFirebaseDatabase(name, location);
                mUsername.setText(name);
                mLocation.setText(location);
            }

            Intent intent = new Intent(MainActivity.this, RestaurantListActivity.class);
            intent.putExtra("location", location);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    }

    public void storeInFirebaseDatabase(String name, String location){
        mDatabaseUsernameReference.setValue(name);
        mDatabaseLocationReference.setValue(location);
    }
}
