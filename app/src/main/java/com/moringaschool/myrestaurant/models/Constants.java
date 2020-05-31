package com.moringaschool.myrestaurant.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringaschool.myrestaurant.BuildConfig;

public class Constants {
    public static final String YELP_TOKEN = BuildConfig.YELP_API_KEY;
    public static final String YELP_BASE_URL = "https://api.yelp.com/v3/";
    public static final String YELP_LOCATION_QUERY_PARAMETER = "location";
    public static final String NAME_KEY = "name";

//    Firebase
    public static final String FIREBASE_USERNAME_KEY = "Username";
    public static final String FIREBASE_LOCATION_KEY = "Location";


    public static DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
}
