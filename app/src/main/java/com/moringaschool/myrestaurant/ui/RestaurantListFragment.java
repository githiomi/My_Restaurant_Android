package com.moringaschool.myrestaurant.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.adapters.RestaurantListAdapter;
import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.models.Constants;
import com.moringaschool.myrestaurant.models.YelpBusinessesSearchResponse;
import com.moringaschool.myrestaurant.network.YelpApi;
import com.moringaschool.myrestaurant.network.YelpClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListFragment extends Fragment {

//    TAG
    private static final String TAG = RestaurantListFragment.class.getSimpleName();

//    Binding views using Butter Knife
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.onlyTextView) TextView mInCase;

//    Local variable
    private RestaurantListAdapter mAdapter;
    public List<Business> mRestaurants = new ArrayList<>();
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentAddress;
    private FragmentActivity mContext;

    public RestaurantListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext());
            mEditor = mSharedPreferences.edit();

//            This will get the options menu we created in the parent activity
            setHasOptionsMenu(true);
    }

//    Setting the fragment global context
    private Context mContext() {
        return mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

//        Binding view using butter knife
        ButterKnife.bind(this, view);

        mRecentAddress = mSharedPreferences.getString(Constants.YELP_LOCATION_QUERY_PARAMETER, null);

        if ( mRecentAddress != null ) {
            getRestaurants(mRecentAddress);
        }else{
            mInCase.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

        return view;
    }

//    Custom method meant to retrieve restaurants using the location passed in
    private void getRestaurants(String mRecentAddress) {

        String restaurants = "restaurants";

        YelpApi yelpClient = YelpClient.getClient();

        Call<YelpBusinessesSearchResponse> call = yelpClient.getRestaurants(mRecentAddress, restaurants);

        call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
            @Override
            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {

                if (response.isSuccessful()) {
                    mRestaurants = response.body().getBusinesses();

                    for (Business restaurant : mRestaurants) {
                        Log.v(TAG, restaurant.getName());
                    }

                    RestaurantListAdapter mAdapter;

                    mAdapter = new RestaurantListAdapter( mContext, mRestaurants);
                    mRecyclerView.setAdapter(mAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);

                    showRestaurants();
                } else {
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                showFailureMessage();
            }
        });

    }

//    Search view widget overriden methods
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){

//        Call the super class
        super.onCreateOptionsMenu(menu, menuInflater);

//        Inflate the menu
        menuInflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSharedPreferences(query);
                getRestaurants(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

//    Takes the searched location and puts in into the shared preference for data persistence
    private void addToSharedPreferences(String query) {
        mEditor.putString(Constants.YELP_LOCATION_QUERY_PARAMETER, query).apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //    Custom methods to show user that data is being searched
    private void showRestaurants() {
    }

    private void showUnsuccessfulMessage(){
    }

    private void showFailureMessage() {
    }
}