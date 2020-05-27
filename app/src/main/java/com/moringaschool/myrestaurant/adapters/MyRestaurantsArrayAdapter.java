package com.moringaschool.myrestaurant.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.squareup.picasso.Picasso;

public class MyRestaurantsArrayAdapter extends ArrayAdapter {

    private Context mContext;
    private String[] mRestaurants;
    private String[] mCuisines;
    private String[] mImg;

    public MyRestaurantsArrayAdapter(Context context, int resource, String[] restaurants, String[] cuisines, String[] images) {
        super(context, resource);
        this.mContext = context;
        this.mRestaurants = restaurants;
        this.mCuisines = cuisines;
        this.mImg = images;
    }

    @Override
    public Object getItem(int position){
        String restaurant = mRestaurants[position];
        String cuisine = mCuisines[position];
        String imageURL = mImg[position];
        Picasso.get().load(mImg[position]);
        return String.format("%s \n It  has great %s! \n\n  %s", restaurant, cuisine, imageURL);
    }

    @Override
    public int getCount(){
        return mRestaurants.length;
    }
}
