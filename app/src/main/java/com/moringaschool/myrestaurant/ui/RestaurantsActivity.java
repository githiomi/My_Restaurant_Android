package com.moringaschool.myrestaurant.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.moringaschool.myrestaurant.Business;
import com.moringaschool.myrestaurant.Category;
import com.moringaschool.myrestaurant.MyRestaurantsArrayAdapter;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.network.YelpApi;
import com.moringaschool.myrestaurant.YelpBusinessesSearchResponse;
import com.moringaschool.myrestaurant.models.Restaurant;
import com.moringaschool.myrestaurant.network.YelpClient;


import java.util.ArrayList;
import java.util.List;

import butterknife.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestaurantsActivity extends AppCompatActivity {

    public static final String TAG = RestaurantsActivity.class.getSimpleName();

    @BindView(R.id.nameTextView) TextView mNameView;
    @BindView(R.id.lvRestaurants) ListView mRestaurants;

    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    private ArrayList<Restaurant> mRestaurantsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        final Intent intent = getIntent();
        final String location = intent.getStringExtra("location");
        final String name = intent.getStringExtra("name");
        mNameView.setText(name + ", these are all the restaurants closest to " + location + "!");

        mRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String restaurant = ((TextView) view).getText().toString();
                Toast.makeText(RestaurantsActivity.this, restaurant, Toast.LENGTH_SHORT).show();
            }
        });

//        YelpApi client = YelpClient.getClient();
//
//        Call<YelpBusinessesSearchResponse> call = client.getRestaurants(location, "restaurants");
//
//        call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
//
//            @Override
//            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response){
//
//                hideProgressBar();
//
//                if (response.isSuccessful()) {
//                    List<Business> restaurantsList = response.body().getBusinesses();
//                    String[] restaurants = new String[restaurantsList.size()];
//                    String[] categories = new String[restaurantsList.size()];
//
//                    for (int i = 0; i < restaurants.length; i++) {
//                        restaurants[i] = restaurantsList.get(i).getName();
//                    }
//
//                    for (int i = 0; i < categories.length; i++) {
//                        Category category = restaurantsList.get(i).getCategories().get(0);
//                        categories[i] = category.getTitle();
//                    }
//
//                    ArrayAdapter adapter
//                            = new MyRestaurantsArrayAdapter(RestaurantsActivity.this, android.R.layout.simple_list_item_1, restaurants, categories);
//                    mRestaurants.setAdapter(adapter);
//
//                    showRestaurants();
//                } else {
//                    showUnsuccessfulMessage();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t){
//                showFailureMessage();
//                hideProgressBar();
//            }
//
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
        mRestaurants.setVisibility(View.VISIBLE);
        mNameView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}