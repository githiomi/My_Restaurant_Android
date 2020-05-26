package com.moringaschool.myrestaurant.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moringaschool.myrestaurant.Business;
import com.moringaschool.myrestaurant.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item, parent, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListAdapter.RestaurantViewHolder holder, int position) {
        holder.bindRestaurant(mBusinesses.get(position));
    }

    @Override
    public int getItemCount() {
        return mBusinesses.size();
    }


    public class RestaurantViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.restaurantImageView) ImageView mRestaurantImageView;
        @BindView(R.id.restaurantNameTextView) TextView mNameTextView;
        @BindView(R.id.categoryTextView) TextView mCategoryTextView;
        @BindView(R.id.ratingTextView) TextView mRatingTextView;
        private Context mContext;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mContext = itemView.getContext();
        }

        public void bindRestaurant(Business restaurant){
            mRestaurantImageView.setImageResource(R.drawable.ic_launcher_foreground);
            mNameTextView.setText(restaurant.getName());
            mCategoryTextView.setText(restaurant.getCategories().get(0).getTitle());
            mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
        }

    }

}
