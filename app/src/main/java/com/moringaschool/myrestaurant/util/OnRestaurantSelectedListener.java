package com.moringaschool.myrestaurant.util;

import com.moringaschool.myrestaurant.models.Business;

import java.util.List;

public interface OnRestaurantSelectedListener {

    public void onRestaurantSelected(Integer position, List<Business> restaurants);

}
