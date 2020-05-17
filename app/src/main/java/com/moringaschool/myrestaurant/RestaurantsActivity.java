package com.moringaschool.myrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RestaurantsActivity extends AppCompatActivity {

    public static final String TAG = RestaurantsActivity.class.getSimpleName();
    private TextView mNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        mNameView = (TextView)findViewById(R.id.nameTextView);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        mNameView.setText("Confirm that your name is: " + name);
    }
}
