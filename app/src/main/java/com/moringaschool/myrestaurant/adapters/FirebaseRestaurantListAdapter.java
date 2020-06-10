package com.moringaschool.myrestaurant.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringaschool.myrestaurant.R;
import com.moringaschool.myrestaurant.models.Business;
import com.moringaschool.myrestaurant.models.Constants;
import com.moringaschool.myrestaurant.models.Restaurant;
import com.moringaschool.myrestaurant.ui.RestaurantDetailActivity;
import com.moringaschool.myrestaurant.util.ItemTouchHelperAdapter;
import com.moringaschool.myrestaurant.util.ItemTouchHelperViewHolder;
import com.moringaschool.myrestaurant.util.OnStartDragListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseRestaurantListAdapter extends RecyclerView.Adapter<FirebaseRestaurantListAdapter.SavedRestaurantViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<Business> mRestaurants;
    private Context mContext;
    private OnStartDragListener mOnStartDragListener;
    private ChildEventListener mChildEventListener;


    public FirebaseRestaurantListAdapter(Context mContext, ArrayList<Business> mRestaurants) {
        this.mRestaurants = mRestaurants;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SavedRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item_drag, parent, false);
        FirebaseRestaurantListAdapter.SavedRestaurantViewHolder viewHolder = new FirebaseRestaurantListAdapter.SavedRestaurantViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull SavedRestaurantViewHolder holder, int position) {

        holder.mRestaurantImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
        holder.bindRestaurant(mRestaurants.get(position));

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

    private void setIndexInFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        for (Business restaurant : mRestaurants) {
            String restaurantId=restaurant.getPushId();
            DatabaseReference mRestaurantReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_RESTAURANTS).child(uid).child(restaurantId);
            int index = mRestaurants.indexOf(restaurant);
            mRestaurantReference.getRef();
            restaurant.setIndex(Integer.toString(index));
            mRestaurantReference.setValue(restaurant);
        }
    }

    @Override
    public void onItemDismiss(int position) {
//        for( DataSnapshot removeDataSnapshot:dataSnapshot.getChildren()){
//            if(removeDataSnapshot)
//        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        String restaurantId=mRestaurants.get(position).getPushId();
        DatabaseReference mRestaurantReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_RESTAURANTS).child(uid);
        mRestaurantReference.child(restaurantId).removeValue();

    }

    public class SavedRestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.restaurantImageView)
        public ImageView mRestaurantImageView;
        @BindView(R.id.restaurantNameTextView)
        TextView mNameTextView;
        @BindView(R.id.categoryTextView)
        TextView mCategoryTextView;
        @BindView(R.id.ratingTextView)
        TextView mRatingTextView;
        private Context mContext;
        public SavedRestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        public void bindRestaurant(Business restaurant) {
            Picasso.get().load(restaurant.getImageUrl()).into(mRestaurantImageView);
            mNameTextView.setText(restaurant.getName());
            mCategoryTextView.setText(restaurant.getCategories().get(0).getTitle());
            mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
        }


        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
            intent.putExtra("position",itemPosition);
            intent.putExtra("restaurants", Parcels.wrap(mRestaurants));
            mContext.startActivity(intent);

        }

//        @Override
//        public void onItemSelected() {
//            Log.d("Animation", "onItemSelected");
////            itemView.animate()
////                    .alpha(0.7f)
////                    .scaleX(0.9f)
////                    .scaleY(0.9f)
////                    .setDuration(500);
//            AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(mContext,
//                    R.animator.drag_scale_on);
//            set.setTarget(itemView);
//            set.start();
//
//        }
//
//        @Override
//        public void onItemClear() {
//            Log.d("Animation", "onItemClear");
////            itemView.animate()
////                    .alpha(1f)
////                    .scaleX(1f)
////                    .scaleY(1f);
//            AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(mContext,
//                    R.animator.drag_scale_off);
//            set.setTarget(itemView);
//            set.start();
//
//        }
    }
}
