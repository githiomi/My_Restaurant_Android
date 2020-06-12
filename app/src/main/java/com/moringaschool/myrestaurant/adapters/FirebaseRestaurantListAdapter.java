package com.moringaschool.myrestaurant.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.internal.$Gson$Preconditions;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.models.Constants;
import com.moringaschool.myrestaurant.models.Restaurant;
import com.moringaschool.myrestaurant.ui.RestaurantDetailActivity;
import com.moringaschool.myrestaurant.ui.RestaurantDetailFragment;
import com.moringaschool.myrestaurant.util.ItemTouchHelperAdapter;
import com.moringaschool.myrestaurant.util.ItemTouchHelperViewHolder;
import com.moringaschool.myrestaurant.util.OnStartDragListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseRestaurantListAdapter
        extends FirebaseRecyclerAdapter<Business, FirebaseRestaurantViewHolder>
        implements ItemTouchHelperAdapter {

    private ArrayList<Business> mRestaurants;
    private ChildEventListener mChildEventListener;
    private Context mContext;
    private OnStartDragListener mOnStartDragListener;
    private DatabaseReference mRef;
    private Query mQuery;

    public FirebaseRestaurantListAdapter(ArrayList<Business> restaurants,
                                         FirebaseRecyclerOptions<Business> options,
                                         Query query,
                                         OnStartDragListener onStartDragListener,
                                         Context context) {
        super(options);
        this.mRestaurants = restaurants;
        this.mQuery = query;
        this.mRef = query.getRef();
        this.mOnStartDragListener = onStartDragListener;
        this.mContext = context;

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mRestaurants.add(dataSnapshot.getValue(Business.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }

    @NonNull
    @Override
    public FirebaseRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item_drag, parent, false);
        return new FirebaseRestaurantViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull FirebaseRestaurantViewHolder viewHolder, int position, @NonNull Business business) {

        viewHolder.bindRestaurant(business);
        viewHolder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toRestaurantDetailFragment = new Intent(mContext, RestaurantDetailActivity.class);
                toRestaurantDetailFragment.putExtra("position", viewHolder.getAdapterPosition());
                toRestaurantDetailFragment.putExtra("restaurants", Parcels.wrap(mRestaurants));
                mContext.startActivity(toRestaurantDetailFragment);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mRestaurants, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        setIndexInFirebase();
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

        mRestaurants.remove(position);
        getRef(position).removeValue();
    }


//    This custom method is meant to update the index of each restaurant in the array list
    private void setIndexInFirebase() {

        for (Business restaurant : mRestaurants) {
            int index = mRestaurants.indexOf(restaurant);
            DatabaseReference ref = getRef(index);
            restaurant.setIndex(Integer.toString(index));
            ref.setValue(restaurant);
        }

    }

    public class SavedRestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        Binding widgets using Butter knife
        @BindView(R.id.restaurantImageView) public ImageView mRestaurantImageView;
        @BindView(R.id.restaurantNameTextView) TextView mNameTextView;
        @BindView(R.id.categoryTextView) TextView mCategoryTextView;
        @BindView(R.id.ratingTextView) TextView mRatingTextView;

//        Local variables
        private int mOrientation;
        private Context mContext;

        public SavedRestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
            mOrientation = itemView.getResources().getConfiguration().orientation;

            if ( mOrientation == Configuration.ORIENTATION_LANDSCAPE ){
                createSavedDetailFragment(0);
            }
        }

        public void bindRestaurant(Business restaurant) {
            Picasso.get().load(restaurant.getImageUrl()).into(mRestaurantImageView);
            mNameTextView.setText(restaurant.getName());
            mCategoryTextView.setText(restaurant.getCategories().get(0).getTitle());
            mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
        }

//        custom method to allow the flip orientation
        private void createSavedDetailFragment(int position) {

            if ( mOrientation == Configuration.ORIENTATION_LANDSCAPE ){
                RestaurantDetailFragment restaurantDetailFragment = RestaurantDetailFragment.newInstance(mRestaurants, position);
                FragmentTransaction fragmentTransaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.restaurantDetailContainer, restaurantDetailFragment);
                fragmentTransaction.commit();
            }
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();

            if ( mOrientation == Configuration.ORIENTATION_LANDSCAPE ){
                createSavedDetailFragment(itemPosition);
            }else {
                Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                intent.putExtra("position", itemPosition);
                intent.putExtra("restaurants", Parcels.wrap(mRestaurants));
                mContext.startActivity(intent);
            }
        }
    }

    @Override
    public void stopListening() {
        super.stopListening();
        mQuery.removeEventListener(mChildEventListener);
    }
}
