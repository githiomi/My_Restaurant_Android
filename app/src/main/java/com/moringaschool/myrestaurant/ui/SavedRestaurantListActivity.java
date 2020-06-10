package com.moringaschool.myrestaurant.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.adapters.FirebaseRestaurantListAdapter;
import com.moringaschool.myrestaurant.adapters.FirebaseRestaurantViewHolder;
import com.moringaschool.myrestaurant.adapters.RestaurantListAdapter;
import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.models.Constants;
import com.moringaschool.myrestaurant.util.OnStartDragListener;
import com.moringaschool.myrestaurant.util.SimpleItemTouchHelperCallback;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRestaurantListActivity extends AppCompatActivity implements OnStartDragListener {

//    TAG
    private static final String TAG = SavedRestaurantListActivity.class.getSimpleName();

    private DatabaseReference mRestaurantReference;
    private FirebaseRestaurantListAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @BindView(R.id.nameTextView) TextView mTextVIew;
    @BindView(R.id.rvRestaurants) RecyclerView mRecyclerView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        // Custom method th get restaurants from firebase
        retrieveRestaurants();
    }

    public void retrieveRestaurants() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String username = user.getDisplayName();

        Log.d(TAG, "retrieveRestaurants: username " + username + "-------------------");

        mRestaurantReference = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_RESTAURANTS)
                .child(username);

        FirebaseRecyclerOptions<Business> options =
                new FirebaseRecyclerOptions.Builder<Business>()
                        .setQuery(mRestaurantReference, Business.class)
                        .build();

        mFirebaseAdapter = new FirebaseRestaurantListAdapter(options, mRestaurantReference, this, this);

                mRecyclerView.setAdapter(mFirebaseAdapter);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setHasFixedSize(true);

                ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
                mItemTouchHelper = new ItemTouchHelper(callback);
                mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        //        Custom method to hide the progress bar and show list
                showRestaurants();
    }

    private void showRestaurants() {
        mTextVIew.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void onStartDrag(RecyclerView.ViewHolder viewHolder){
        mItemTouchHelper.startDrag(viewHolder);
    }

}