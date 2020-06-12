package com.moringaschool.myrestaurant.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;

import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.models.Constants;
import com.moringaschool.myrestaurant.util.OnRestaurantSelectedListener;

import org.parceler.Parcels;

import java.util.List;

import butterknife.*;

public class RestaurantListActivity
        extends AppCompatActivity
        implements OnRestaurantSelectedListener {
//    Shared preferences
    private SharedPreferences mSharedPreferences;

//    Local variables
    String savedName;
    String savedLocation;

//    TAG
    public static final String TAG = RestaurantListActivity.class.getSimpleName();

//    Variables to store the position and list of restaurants in the interface
    private List<Business> mAllBusinesses;
    private Integer mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        getSupportActionBar().setTitle("Welcome " + name);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        savedName = mSharedPreferences.getString(Constants.NAME_KEY, null);
        savedLocation = mSharedPreferences.getString(Constants.YELP_LOCATION_QUERY_PARAMETER, null);

        if ( savedInstanceState != null ){

            int mOrientation = getResources().getConfiguration().orientation;

            if ( mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                        mPosition = savedInstanceState.getInt(Constants.EXTRA_KEY_POSITION);
                        mAllBusinesses = Parcels.unwrap(savedInstanceState.getParcelable(Constants.EXTRA_KEY_RESTAURANTS));

                        if ( mPosition != null & mAllBusinesses != null ){
                            Intent toRestaurantDetailActivity = new Intent(this, RestaurantDetailActivity.class);
                            toRestaurantDetailActivity.putExtra(Constants.EXTRA_KEY_POSITION, mPosition);
                            toRestaurantDetailActivity.putExtra(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mAllBusinesses));
                        }
            }
        }
    }

//    Overriding the on destroy method in the activity so it does not return
//    to the list when orientation changes


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        if ( mPosition != null && mAllBusinesses !=  null ){
            outState.putInt(Constants.EXTRA_KEY_POSITION, mPosition);
            outState.putParcelable(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mAllBusinesses));
        }
    }

    //    Overriding the interface method to allow communication between fragment and activity
    @Override
    public void onRestaurantSelected(Integer position, List<Business> restaurants) {
        this.mPosition = position;
        this.mAllBusinesses = restaurants;
    }
}
