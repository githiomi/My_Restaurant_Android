package com.moringaschool.myrestaurant.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.YelpBusinessesSearchResponse;

import com.moringaschool.myrestaurant.adapters.RestaurantListAdapter;
import com.moringaschool.myrestaurant.models.Business;

import com.moringaschool.myrestaurant.models.Constants;
import com.moringaschool.myrestaurant.network.YelpApi;
import com.moringaschool.myrestaurant.network.YelpClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.*;
import retrofit2.*;

public class RestaurantListActivity extends AppCompatActivity {

//    Shared preferences
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    String savedName;
    String savedLocation;

    public static final String TAG = RestaurantListActivity.class.getSimpleName();

    @BindView(R.id.nameTextView) TextView mNameTextView;
    @BindView(R.id.rvRestaurants) RecyclerView mRestaurantsRecyclerView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.errorTextView) TextView mErrorTextView;

    public List<Business> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String location = intent.getStringExtra("location");

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        savedName = mSharedPreferences.getString(Constants.NAME_KEY, null);
        savedLocation = mSharedPreferences.getString(Constants.YELP_LOCATION_QUERY_PARAMETER, null);

        Log.v(TAG, "Saved information from " + savedName + " as " + savedLocation);

        mNameTextView.setText(name + ", these are the restaurants near and around " + location + "!");

        getRestaurants(location);


    }

    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showRestaurants() {
        mRestaurantsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void getRestaurants(String location) {

        String restaurants = "restaurants";

        YelpApi yelpClient = YelpClient.getClient();

        Call<YelpBusinessesSearchResponse> call = yelpClient.getRestaurants(location, restaurants);

        call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
            @Override
            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {

                if (response.isSuccessful()) {
                    mRestaurants = response.body().getBusinesses();

                    for (Business restaurant : mRestaurants) {
                        Log.v(TAG, restaurant.getName());
                    }

                    RestaurantListAdapter mAdapter;

                    mAdapter = new RestaurantListAdapter(RestaurantListActivity.this, mRestaurants);
                    mRestaurantsRecyclerView.setAdapter(mAdapter);
                    RecyclerView.LayoutManager layoutManager =
                            new LinearLayoutManager(RestaurantListActivity.this);
                    mRestaurantsRecyclerView.setLayoutManager(layoutManager);
                    mRestaurantsRecyclerView.setHasFixedSize(true);

                    hideProgressBar();
                    showRestaurants();
                } else {
                    hideProgressBar();
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                hideProgressBar();
                showFailureMessage();
            }
        });
    }
}
