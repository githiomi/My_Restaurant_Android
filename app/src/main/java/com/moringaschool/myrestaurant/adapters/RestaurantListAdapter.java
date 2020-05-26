package com.moringaschool.myrestaurant.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moringaschool.myrestaurant.Business;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {

    public List<Business> mBusinesses;
    public Context mContext;

    public RestaurantListAdapter(Context context, List<Business> businesses){
        this.mBusinesses = businesses;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RestaurantListAdapter.RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListAdapter.RestaurantViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mBusinesses.size();
    }
}
