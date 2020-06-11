package com.moringaschool.myrestaurant.ui;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.adapters.RestaurantPagerAdapter;
import com.moringaschool.myrestaurant.models.Restaurant;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.*;



public class RestaurantDetailActivity extends AppCompatActivity {
    @BindView(R.id.viewPager) ViewPager mViewPager;
    private RestaurantPagerAdapter adapterViewPager;
    ArrayList<Business> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        ButterKnife.bind(this);

        int startingPosition = getIntent().getIntExtra("position", 0);
        mRestaurants = Parcels.unwrap(getIntent().getParcelableExtra("restaurants"));

        adapterViewPager = new RestaurantPagerAdapter(getSupportFragmentManager(), mRestaurants);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
    }
}