package com.moringaschool.myrestaurant.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.models.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

//    firebase username
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mfirebaseAuthStateListener;

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

//        Firebase Authentication
        mFirebaseAuth = FirebaseAuth.getInstance();
        mfirebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if ( firebaseUser != null ){
                    getSupportActionBar().setTitle("Welcome " + firebaseUser.getDisplayName() + "!");
                    mUsername.setText(firebaseUser.getDisplayName());
                    mUsername.setCursorVisible(false);
                }else{
                    getSupportActionBar().setTitle("Welcome User");
                }
            }
        };

//        Shared Preferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

//        Firebase
        DatabaseReference ref = Constants.firebaseDatabase.getReference();

        mDatabaseUsernameReference = ref.child(Constants.FIREBASE_USERNAME_KEY);
        mDatabaseLocationReference = ref.child(Constants.FIREBASE_LOCATION_KEY);

        mValueEventListener = mDatabaseLocationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot localSnapshot : dataSnapshot.getChildren()){
                    String locationName = localSnapshot.getValue().toString();
                    Log.d(TAG, "onDataChange: "+ locationName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "On value event listener failed", Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if ( id == R.id.action_logout ){
            logout();
            return true;
        }
        if ( id == R.id.profileOption){
            Toast.makeText(this, "You cannot see your profile at this time", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mfirebaseAuthStateListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mfirebaseAuthStateListener);
    }

}
