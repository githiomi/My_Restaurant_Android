package com.moringaschool.myrestaurant.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.moringaschool.myrestaurant.adapters.FirebaseRestaurantViewHolder;
import com.moringaschool.myrestaurant.adapters.RestaurantListAdapter;
import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.models.Constants;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRestaurantListActivity extends AppCompatActivity {
    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter<Business, FirebaseRestaurantViewHolder> mFirebaseAdapter;
    private RestaurantListAdapter mAdapter;

    @BindView(R.id.nameTextView)
    TextView mTextVIew;
    @BindView(R.id.rvRestaurants)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        mTextVIew.setVisibility(View.GONE);
        final ArrayList<Business> restaurants = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String username = user.getDisplayName();

        mRestaurantReference = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_RESTAURANTS)
                .child(username);

        mRestaurantReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    restaurants.add(snapshot.getValue(Business.class));
                }

                RestaurantListAdapter mAdapter;

                mAdapter = new RestaurantListAdapter(SavedRestaurantListActivity.this,restaurants);
                mRecyclerView.setAdapter(mAdapter);
                RecyclerView.LayoutManager layoutManager =
                        new LinearLayoutManager(SavedRestaurantListActivity.this);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setHasFixedSize(true);


                showRestaurants();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SavedRestaurantListActivity.this, "Error " + databaseError , Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void showRestaurants() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

}