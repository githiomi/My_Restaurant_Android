package com.moringaschool.myrestaurant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.adapters.RestaurantPagerAdapter;
import com.moringaschool.myrestaurant.models.Constants;
import com.moringaschool.myrestaurant.models.Restaurant;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.*;

public class RestaurantDetailActivity extends AppCompatActivity {
    @BindView(R.id.viewPager) ViewPager mViewPager;
    private RestaurantPagerAdapter adapterViewPager;
    ArrayList<Business> mRestaurants = new ArrayList<>();

//    To find the users location
    private String mSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        ButterKnife.bind(this);

        int startingPosition = getIntent().getIntExtra(Constants.EXTRA_KEY_POSITION, 0);
        mRestaurants = Parcels.unwrap(getIntent().getParcelableExtra(Constants.EXTRA_KEY_RESTAURANTS));
        mSource = getIntent().getStringExtra(Constants.KEY_SOURCE);


        adapterViewPager = new RestaurantPagerAdapter(getSupportFragmentManager(), mRestaurants, mSource);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}