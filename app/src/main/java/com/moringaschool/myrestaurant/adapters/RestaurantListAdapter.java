package com.moringaschool.myrestaurant.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.models.Constants;
import com.moringaschool.myrestaurant.ui.RestaurantDetailActivity;
import com.moringaschool.myrestaurant.ui.RestaurantDetailFragment;
import com.moringaschool.myrestaurant.util.OnRestaurantSelectedListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {

    private List<Business> mRestaurants;
    private Context mContext;

//        Interface initialization
    private OnRestaurantSelectedListener mOnRestaurantSelectedListener;

    public RestaurantListAdapter(Context context, List<Business> restaurants, OnRestaurantSelectedListener onRestaurantSelectedListener){
        mContext = context;
        mRestaurants = restaurants;
        mOnRestaurantSelectedListener = onRestaurantSelectedListener;
    }

    @Override
    public RestaurantListAdapter.RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item_drag, parent, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view, mRestaurants, mOnRestaurantSelectedListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantListAdapter.RestaurantViewHolder holder, int position){
        holder.bindRestaurant(mRestaurants.get(position));
    }

    @Override
    public int getItemCount(){
        return mRestaurants.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        Widget binding using butter knife
        @BindView(R.id.restaurantImageView) ImageView mRestaurantImageView;
        @BindView(R.id.restaurantNameTextView) TextView mNameTextView;
        @BindView(R.id.categoryTextView) TextView mCategoryTextView;
        @BindView(R.id.ratingTextView) TextView mRatingTextView;

//        Local variables
        private int mOrientation;
        private  Context mContext;
        private List<Business> mAllRestaurants;
        private OnRestaurantSelectedListener mOnRestaurantSelectedListener;

        public RestaurantViewHolder(View itemView, List<Business> restaurants, OnRestaurantSelectedListener onRestaurantSelectedListener){
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            this.mOrientation = itemView.getResources().getConfiguration().orientation;
            this.mAllRestaurants = restaurants;
            this.mOnRestaurantSelectedListener = onRestaurantSelectedListener;

            if ( mOrientation == Configuration.ORIENTATION_LANDSCAPE ){
                createDetailFragment(0);
            }
        }

        public void bindRestaurant(Business restaurant) {

            final int MAX_WIDTH = 200;
            final int MAX_HEIGHT = 200;

            mNameTextView.setText(restaurant.getName());
            mCategoryTextView.setText(restaurant.getCategories().get(0).getTitle());
            mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
            if (mRestaurantImageView == null) {
                mRestaurantImageView.setImageResource(R.drawable.background);
            } else{
                Picasso.get().load(restaurant.getImageUrl())
                        .resize(MAX_WIDTH, MAX_HEIGHT)
                        .centerCrop()
                        .into(mRestaurantImageView);
            }
        }

//        Custom method
        private void createDetailFragment(int position) {

            RestaurantDetailFragment restaurantDetailFragment = RestaurantDetailFragment.newInstance(mRestaurants, position, Constants.SOURCE_FIND);
            FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.restaurantDetailContainer, restaurantDetailFragment);
            fragmentTransaction.commit();

        }

//        Overriding the on click listener for the item view
        @Override
        public void onClick(View v){
            int itemPosition = getLayoutPosition();

            // calling the interface method to to pass int the position and the arraylist
            mOnRestaurantSelectedListener.onRestaurantSelected(itemPosition, mRestaurants, Constants.SOURCE_FIND);

            if ( mOrientation == Configuration.ORIENTATION_LANDSCAPE){
                createDetailFragment(itemPosition);
            }else {
                Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                intent.putExtra("position", itemPosition);
                intent.putExtra("restaurants", Parcels.wrap(mRestaurants));
                intent.putExtra(Constants.KEY_SOURCE, Constants.SOURCE_FIND);
                mContext.startActivity(intent);
            }
        }
    }
}
