package com.moringaschool.myrestaurant.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.adapters.FirebaseRestaurantListAdapter;
import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.models.Constants;
import com.moringaschool.myrestaurant.util.OnStartDragListener;
import com.moringaschool.myrestaurant.util.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRestaurantFragment
        extends Fragment
        implements OnStartDragListener {

//    TAG
    private static final String TAG = SavedRestaurantFragment.class.getSimpleName();

//    widgets
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.onlyTextView) TextView mInCase;

//    Local variables
    private FirebaseRestaurantListAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ArrayList<Business> restaurants;
    private Query mQuery;
    private OnStartDragListener mOnStartDragListener;

//    Global context variable
    private FragmentActivity mContext;

    public SavedRestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Assigning context
        mContext = getActivity();

//        Setting the on start drag listener
        mOnStartDragListener = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_restaurant, container, false);

//        Binding using butter knife
        ButterKnife.bind(this, view);

//        Method to set up firebase and get saved restaurants
        setUpFirebase();

        return view;
    }

//    custom method to get firebase database restaurants
    private void setUpFirebase() {

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

        mFirebaseAdapter = new FirebaseRestaurantListAdapter(restaurants, options, mQuery, mOnStartDragListener, mContext);

        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    restaurants.add(snapshot.getValue(Business.class));
                }

                if ( restaurants.size() > 0 ) {

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);

                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setAdapter(mFirebaseAdapter);
                    mRecyclerView.setHasFixedSize(true);

                    mFirebaseAdapter.notifyDataSetChanged();

                    mItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(mFirebaseAdapter));
                    mItemTouchHelper.attachToRecyclerView(mRecyclerView);

                    showRestaurants();

                }else {
                    mInCase.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                showRestaurants();
                mContext.getActionBar().setTitle("Error!");

            }
        });
    }

    private void showRestaurants() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

//    Custom method for the on start drag (implementation)
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

    }
}