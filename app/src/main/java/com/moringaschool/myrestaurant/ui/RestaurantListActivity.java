package com.moringaschool.myrestaurant.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.models.Constants;
import butterknife.*;

public class RestaurantListActivity extends AppCompatActivity {

//    Shared preferences
    private SharedPreferences mSharedPreferences;

//    Local variables
    String savedName;
    String savedLocation;

//    TAG
    public static final String TAG = RestaurantListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        getSupportActionBar().setTitle("Welcome " + name);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        savedName = mSharedPreferences.getString(Constants.NAME_KEY, null);
        savedLocation = mSharedPreferences.getString(Constants.YELP_LOCATION_QUERY_PARAMETER, null);
    }

}
