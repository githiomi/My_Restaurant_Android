package com.moringaschool.myrestaurant.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.adapters.MyRestaurantsArrayAdapter;
import com.moringaschool.myrestaurant.models.Restaurant;
import com.moringaschool.myrestaurant.network.YelpService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.*;
//import retrofit2.*;

import okhttp3.*;

public class RestaurantListActivity extends AppCompatActivity {

    public static final String TAG = RestaurantListActivity.class.getSimpleName();

    @BindView(R.id.nameTextView)
    TextView mNameTextView;
//    @BindView(R.id.rvRestaurants) RecyclerView mRestaurantsRecyclerView;
    @BindView(R.id.lvRestaurants) ListView mRestaurantsListView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.errorTextView)
    TextView mErrorTextView;

//    public List<Business> mRestaurants = new ArrayList<>();
    public List<Restaurant> mRestaurants = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String location = intent.getStringExtra("location");

        mNameTextView.setText(name + ", these are the restaurants near and around " + location + "!");

        getRestaurants(location);

//        String restaurants = "restaurants";
//
//        YelpApi yelpClient = YelpClient.getClient();
//
//        Call<YelpBusinessesSearchResponse> call = yelpClient.getRestaurants(location, restaurants);
//
//        call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
//            @Override
//            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {
//
//                if (response.isSuccessful()) {
//                    mRestaurants = response.body().getBusinesses();
//
//                    for (Business restaurant : mRestaurants){
//                        Log.v(TAG, restaurant.getName());
//                    }
//
//                    RestaurantListAdapter mAdapter;
//
//                    mAdapter = new RestaurantListAdapter(RestaurantListActivity.this, mRestaurants);
//                    mRestaurantsRecyclerView.setAdapter(mAdapter);
//                    RecyclerView.LayoutManager layoutManager =
//                            new LinearLayoutManager(RestaurantListActivity.this);
//                    mRestaurantsRecyclerView.setLayoutManager(layoutManager);
//                    mRestaurantsRecyclerView.setHasFixedSize(true);
//
//                    hideProgressBar();
//                    showRestaurants();
//                }
//                else {
//                    hideProgressBar();
//                    showUnsuccessfulMessage();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
//                Log.e(TAG, "onFailure: ", t);
//                hideProgressBar();
//                showFailureMessage();
//            }
//        });
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
//        mRestaurantsRecyclerView.setVisibility(View.VISIBLE);
        mRestaurantsListView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

        private void getRestaurants (String location) {
            final YelpService yelpService = new YelpService();
            yelpService.findRestaurants(location, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) {

                    mRestaurants = yelpService.processResults(response);

                    RestaurantListActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            hideProgressBar();
                            showRestaurants();

                            String[] restaurantNames = new String[mRestaurants.size()];
                            String[] categories = new String[mRestaurants.size()];
                            String[] images = new String[mRestaurants.size()];

                            for (int i = 0; i < restaurantNames.length; i++) {
                                restaurantNames[i] = mRestaurants.get(i).getName();
                            }

                            for (int z = 0; z < categories.length; z += 1){
                                String category = mRestaurants.get(z).getCategories().get(0);
                                categories[z] = category;
                            }

                            for (int a = 0; a < images.length; a += 1 ){
                                String image = mRestaurants.get(a).getImageUrl();
                                images[a] = image;
                            }

                            ArrayAdapter<String> adapter = new MyRestaurantsArrayAdapter(RestaurantListActivity.this,
                                    android.R.layout.simple_list_item_1, restaurantNames, categories, images );
                            mRestaurantsListView.setAdapter(adapter);

                            for (Restaurant restaurant : mRestaurants) {
                                Log.d(TAG, "Name: " + restaurant.getName());
                                Log.d(TAG, "Phone: " + restaurant.getPhone());
                                Log.d(TAG, "Website: " + restaurant.getWebsite());
                                Log.d(TAG, "Image url: " + restaurant.getImageUrl());
                                Log.d(TAG, "Rating: " + Double.toString(restaurant.getRating()));
                                Log.d(TAG, "Address: " + android.text.TextUtils.join(", ", restaurant.getAddress()));
                                Log.d(TAG, "Categories: " + restaurant.getCategories().toString());
                            }

                        }
                    });
                }
            });
        }
}
