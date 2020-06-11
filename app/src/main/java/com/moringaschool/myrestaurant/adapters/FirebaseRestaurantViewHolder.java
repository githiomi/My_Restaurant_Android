package com.moringaschool.myrestaurant.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.models.Constants;
import com.moringaschool.myrestaurant.models.Restaurant;
import com.moringaschool.myrestaurant.ui.RestaurantDetailActivity;
import com.moringaschool.myrestaurant.util.ItemTouchHelperViewHolder;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

public class FirebaseRestaurantViewHolder
        extends RecyclerView.ViewHolder
        implements ItemTouchHelperViewHolder {

//    TAG
    private static final String TAG = FirebaseRestaurantViewHolder.class.getSimpleName();

//    Local variables
    View mView;
    Context mContext;
    public ImageView imageView;

//    vie holder constructor
    public FirebaseRestaurantViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindRestaurant(Business restaurant) {
        imageView = (ImageView) mView.findViewById(R.id.restaurantImageView);
        TextView nameTextView = (TextView) mView.findViewById(R.id.restaurantNameTextView);
        TextView categoryTextView = (TextView) mView.findViewById(R.id.categoryTextView);
        TextView ratingTextView = (TextView) mView.findViewById(R.id.ratingTextView);

        Picasso.get().load(restaurant.getImageUrl()).into(imageView);

        nameTextView.setText(restaurant.getName());
        categoryTextView.setText(restaurant.getCategories().get(0).getTitle());
        ratingTextView.setText("Rating: " + restaurant.getRating() + "/5");
    }

    @Override
    public void onItemSelected() {
        Log.d(TAG, "onItemSelected: Selected -------------------");
    }

    @Override
    public void onItemClear() {
        Log.d(TAG, "onItemClear: Cleared -----------------------");

    }
}