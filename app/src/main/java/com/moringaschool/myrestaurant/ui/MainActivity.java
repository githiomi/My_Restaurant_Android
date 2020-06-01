package com.moringaschool.myrestaurant.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.models.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

//    Shared preferences
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public static final String TAG = MainActivity.class.getSimpleName();
    private DatabaseReference mDatabaseLocationReference;
    private DatabaseReference mDatabaseUsernameReference;

//    Value Event Listeners
    private ValueEventListener mValueEventListener;

    @BindView(R.id.nameEditText) EditText mUsername;
    @BindView(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @BindView(R.id.savedRestaurantsButton) Button mSavedRestaurantsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        Shared Preferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();


//        Firebase
        DatabaseReference ref = Constants.firebaseDatabase.getReference();

        mDatabaseUsernameReference = ref.child(Constants.FIREBASE_USERNAME_KEY);
        mDatabaseLocationReference = ref.child(Constants.FIREBASE_LOCATION_KEY);

//        mValueEventListener = mDatabaseLocationReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot localSnapshot : dataSnapshot.getChildren()){
//                    String locationName = localSnapshot.getValue().toString();
//                    Log.d(TAG, "onDataChange: "+ locationName);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(MainActivity.this, "On value event listener failed", Toast.LENGTH_SHORT).show();
//            }
//        });

        mFindRestaurantsButton.setOnClickListener(this);
        mSavedRestaurantsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if (v == mFindRestaurantsButton){
            String name = mUsername.getText().toString();

            Toast.makeText(this, "Welcome, " + name, Toast.LENGTH_LONG).show();

            if (!(name.equals(""))){
                addToSharedPreferences(name);
                Toast.makeText(this, name + " , you have just logged in!", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(MainActivity.this, RestaurantListActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }

        if ( v == mSavedRestaurantsButton){
            Intent intent = new Intent(MainActivity.this, SavedRestaurantListActivity.class);
            startActivity(intent);
        }
    }

    public void addToSharedPreferences(String name){
        mEditor.putString(Constants.NAME_KEY, name).apply();
    }

    public void storeNewInFirebaseDatabase(String name){
        mDatabaseUsernameReference.setValue(name);
    }

    public void appendDataInFirebaseDatabase(String name){
        mDatabaseUsernameReference.push().setValue(name);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDatabaseLocationReference.removeEventListener(mValueEventListener);
    }
}
