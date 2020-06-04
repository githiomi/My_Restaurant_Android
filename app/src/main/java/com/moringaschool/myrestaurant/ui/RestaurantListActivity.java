package com.moringaschool.myrestaurant.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.SearchView;
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

//    Local variables
    String savedName;
    String savedLocation;

//    TAG
    public static final String TAG = RestaurantListActivity.class.getSimpleName();

    @BindView(R.id.nameTextView) TextView mNameTextView;
    @BindView(R.id.rvRestaurants) RecyclerView mRestaurantsRecyclerView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.errorTextView) TextView mErrorTextView;

//    Class data type model
    public List<Business> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        getSupportActionBar().setTitle("Welcome " + name );

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        savedName = mSharedPreferences.getString(Constants.NAME_KEY, null);
        savedLocation = mSharedPreferences.getString(Constants.YELP_LOCATION_QUERY_PARAMETER, null);

        Log.v(TAG, "Saved information from " + savedName + " as " + savedLocation);

        mNameTextView.setText(name + ", these are the restaurants near and around " + savedLocation + "!");

        if (savedLocation != null) {
            getRestaurants(savedLocation);
        }else {
            hideProgressBar();
            mNameTextView.setText("We could not find any location to look up!");
            showUnsuccessfulMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSharedPreferences(savedName, query);
                getRestaurants(query);
                mNameTextView.setText(savedName + ", these are the restaurants near and around " + query + "!");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }


    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
        mRestaurantsRecyclerView.setVisibility(View.GONE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later");
        mErrorTextView.setVisibility(View.VISIBLE);
        mRestaurantsRecyclerView.setVisibility(View.GONE);
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

                    mErrorTextView.setVisibility(View.GONE);

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

//    Shared preferences variables
    public void addToSharedPreferences(String name, String location){
        mEditor.putString(Constants.NAME_KEY, name).apply();
        mEditor.putString(Constants.YELP_LOCATION_QUERY_PARAMETER, location).apply();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
