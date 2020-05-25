package com.moringaschool.myrestaurant.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.network.YelpService;
import com.moringaschool.myrestaurant.models.Restaurant;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RestaurantsActivity extends AppCompatActivity {

    public static final String TAG = RestaurantsActivity.class.getSimpleName();

    @BindView(R.id.nameTextView) TextView mNameView;
    @BindView(R.id.lvRestaurants) ListView mRestaurants;

    private ArrayList<Restaurant> mRestaurantsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        final Intent intent = getIntent();
        final String location = intent.getStringExtra("location");
        final String name = intent.getStringExtra("name");
        mNameView.setText(name + ", these are all the restaurants closest you!");

        getRestaurantsInZipCode(location);

    }

    public void getRestaurantsInZipCode(String location){
        final YelpService yelpService = new YelpService();
        yelpService.findRestaurants(location, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                Toast.makeText(RestaurantsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mRestaurantsList = yelpService.processResults(response);

                RestaurantsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] restaurantNames = new String[mRestaurantsList.size()];
                        for (int i = 0; i < restaurantNames.length; i +=1 ){
                            restaurantNames[i] = mRestaurantsList.get(i).getName();
                        }

                        ArrayAdapter adapter = new ArrayAdapter(RestaurantsActivity.this,
                                android.R.layout.simple_list_item_1, restaurantNames);
                        mRestaurants.setAdapter(adapter);

                        for (Restaurant restaurant : mRestaurantsList) {
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
