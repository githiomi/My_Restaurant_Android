package com.moringaschool.myrestaurant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.models.Restaurant;
import com.moringaschool.myrestaurant.util.ItemTouchHelperAdapter;
import com.moringaschool.myrestaurant.util.OnStartDragListener;

public class FirebaseRestaurantListAdapter extends FirebaseRecyclerAdapter<Business, FirebaseRestaurantViewHolder> implements ItemTouchHelperAdapter {
//    Local variables
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;

    public FirebaseRestaurantListAdapter(FirebaseRecyclerOptions<Business> options,
                                         DatabaseReference ref,
                                         OnStartDragListener onStartDragListener,
                                         Context context){
        super(options);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FirebaseRestaurantViewHolder firebaseRestaurantViewHolder, int position, @NonNull Business restaurant) {
        firebaseRestaurantViewHolder.bindRestaurant(restaurant);
        firebaseRestaurantViewHolder.mRestaurantImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ( event.getActionMasked() == MotionEvent.ACTION_DOWN ){
                    mOnStartDragListener.onStartDrag(firebaseRestaurantViewHolder);
                }
                return false;
            }
        });
        firebaseRestaurantViewHolder.bindRestaurant(restaurant);
    }

    @NonNull
    @Override
    public FirebaseRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item_drag, parent, false);
        return new FirebaseRestaurantViewHolder(view);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition){
        return false;
    }

    @Override
    public void onItemDismiss(int position){

    }
}
