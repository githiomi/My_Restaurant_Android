package com.moringaschool.myrestaurant.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.moringaschool.myrestaurant.R;

public class SavedRestaurantListActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_restaurants);

//        Change the content of the app bar
        getSupportActionBar().setTitle("Saved Restaurants");

    }

}