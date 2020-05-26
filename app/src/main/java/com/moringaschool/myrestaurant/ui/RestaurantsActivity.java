package com.moringaschool.myrestaurant.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moringaschool.myrestaurant.Business;
import com.moringaschool.myrestaurant.Constants;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.YelpBusinessesSearchResponse;
import com.moringaschool.myrestaurant.adapters.RestaurantListAdapter;
import com.moringaschool.myrestaurant.network.YelpApi;
import com.moringaschool.myrestaurant.network.YelpClient;
import com.moringaschool.myrestaurant.network.YelpService;

import java.io.IOException;
import java.util.List;

import butterknife.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RestaurantsActivity extends AppCompatActivity {

    public static final String TAG = RestaurantsActivity.class.getSimpleName();

//    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
//    @BindView(R.id.errorTextView) TextView mErrorTextView;
//    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.nameTextView) TextView mNameTextView;
//
//    private RestaurantListAdapter mAdapter;
//
//    public List<Business> restaurants;

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

//        YelpApi client = YelpClient.getClient();
//
//        Call<YelpBusinessesSearchResponse> call = client.getRestaurants("Bearer " + Constants.YELP_TOKEN, location, "restaurants");
//
//        call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
//            @Override
//            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {
//                hideProgressBar();
//
//                if (response.isSuccessful()) {
//                    restaurants = response.body().getBusinesses();
//                    mAdapter = new RestaurantListAdapter(RestaurantsActivity.this, restaurants);
//                    mRecyclerView.setAdapter(mAdapter);
//                    RecyclerView.LayoutManager layoutManager =
//                            new LinearLayoutManager(RestaurantsActivity.this);
//                    mRecyclerView.setLayoutManager(layoutManager);
//                    mRecyclerView.setHasFixedSize(true);
//
//                    showRestaurants();
//                } else {
//                    showUnsuccessfulMessage();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
//                hideProgressBar();
//                showFailureMessage();
//            }
//
//        });
//    }
//
//    private void showFailureMessage() {
//        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
//        mErrorTextView.setVisibility(View.VISIBLE);
//    }
//
//    private void showUnsuccessfulMessage() {
//        mErrorTextView.setText("Something went wrong. Please try again later");
//        mErrorTextView.setVisibility(View.VISIBLE);
//    }
//
//    private void showRestaurants() {
//        mRecyclerView.setVisibility(View.VISIBLE);
//    }
//
//    private void hideProgressBar() {
//        mProgressBar.setVisibility(View.GONE);
//    }
    }

    private void getRestaurants(String location){
        final YelpService yelpService = new YelpService();
        yelpService.findRestaurants(location, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                }catch (IOException e){
                    System.out.println(e);
                }
            }
        });
    }
}