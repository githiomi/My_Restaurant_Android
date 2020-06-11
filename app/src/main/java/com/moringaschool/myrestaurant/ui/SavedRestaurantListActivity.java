package com.moringaschool.myrestaurant.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRestaurantListActivity
        extends AppCompatActivity
        implements OnStartDragListener {

    private DatabaseReference mRestaurantReference;
    private FirebaseRestaurantListAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private OnStartDragListener onStartDragListener = this;

    public ArrayList<Business> restaurants;

//    indexing
    private Query mQuery;

    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    @BindView(R.id.nameTextView) TextView mNameTextView;
    @BindView(R.id.rvRestaurants) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        mNameTextView.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Saved Restaurants");

        restaurants = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = user.getDisplayName();

        mQuery = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_RESTAURANTS)
                .child(username)
                .orderByChild(Constants.FIREBASE_QUERY_INDEX);

        FirebaseRecyclerOptions<Business> options = new FirebaseRecyclerOptions.Builder<Business>()
                .setQuery(mQuery, Business.class)
                .build();

        mFirebaseAdapter = new FirebaseRestaurantListAdapter(restaurants, options, mQuery, onStartDragListener, SavedRestaurantListActivity.this);

        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        restaurants.add(snapshot.getValue(Business.class));
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SavedRestaurantListActivity.this);

                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setAdapter(mFirebaseAdapter);
                    mRecyclerView.setHasFixedSize(true);

                    mItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(mFirebaseAdapter));
                    mItemTouchHelper.attachToRecyclerView(mRecyclerView);

                    showRestaurants();
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                showRestaurants();
                getSupportActionBar().setTitle("Error!");

            }
        });
    }

    private void showRestaurants() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFirebaseAdapter!= null) {
            mFirebaseAdapter.stopListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.stopListening();
    }


}