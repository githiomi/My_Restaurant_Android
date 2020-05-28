package com.moringaschool.myrestaurant.adapters;

import android.view.View;

import com.moringaschool.myrestaurant.models.Business;

interface RestaurantViewHolder {
    void onClick(View v);

    void bindRestaurant(Business restaurant);
}
