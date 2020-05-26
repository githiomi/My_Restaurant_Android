package com.moringaschool.myrestaurant.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class MyRestaurantsArrayAdapter extends ArrayAdapter {

    private Context mContext;
    private String[] mRestaurants;
    private String[] mCuisines;

    public MyRestaurantsArrayAdapter(Context context, int resource, String[] restaurants, String[] cuisines) {
        super(context, resource);
        this.mContext = context;
        this.mRestaurants = restaurants;
        this.mCuisines = cuisines;
    }

    @Override
    public Object getItem(int position){
        String restaurant = mRestaurants[position];
        String cuisine = mCuisines[position];
        return String.format("%s \n\n It  has great %s! ", restaurant, cuisine);
    }

    @Override
    public int getCount(){
        return mRestaurants.length;
    }
}
